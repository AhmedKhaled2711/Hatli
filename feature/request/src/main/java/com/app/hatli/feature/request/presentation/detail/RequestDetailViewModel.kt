package com.app.hatli.feature.request.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.usecase.request.AdvanceRequestStatusUseCase
import com.app.hatli.core.domain.usecase.request.CancelRequestUseCase
import com.app.hatli.core.domain.usecase.request.ConfirmDeliveryUseCase
import com.app.hatli.core.domain.usecase.request.ObserveRequestUseCase
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.nextRunnerStatus
import com.app.hatli.core.domain.usecase.request.ObserveRunnerAssignedRequestsUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeRequest: ObserveRequestUseCase,
    private val observeRunnerAssigned: ObserveRunnerAssignedRequestsUseCase,
    private val authRepository: AuthRepository,
    private val cancelRequest: CancelRequestUseCase,
    private val advanceStatus: AdvanceRequestStatusUseCase,
    private val confirmDelivery: ConfirmDeliveryUseCase,
) : BaseViewModel<RequestDetailUiState, RequestDetailUiEvent, RequestDetailUiEffect>(
    RequestDetailUiState(),
) {
    private val requestId: String = checkNotNull(savedStateHandle["requestId"])
    private var runnerRatingJob: Job? = null
    private var currentObservedRunnerId: String? = null

    init {
        observeRequest(requestId)
            .catch { e ->
                setState {
                    copy(
                        isLoading = false,
                        errorMessage = e.message ?: if (isAr) "تعذر تحميل الطلب" else "Failed to load request",
                    )
                }
            }
            .onEach { req ->
                val uid = authRepository.currentUser()?.uid.orEmpty()
                val isRequester = req != null && req.requesterId == uid
                val isRunner = req != null && req.runnerId == uid

                val assignedRunnerId = req?.runnerId
                if (!assignedRunnerId.isNullOrBlank() && assignedRunnerId != currentObservedRunnerId) {
                    currentObservedRunnerId = assignedRunnerId
                    runnerRatingJob?.cancel()
                    runnerRatingJob = observeRunnerAssigned(assignedRunnerId)
                        .catch { emit(emptyList()) }
                        .onEach { assigned ->
                            val ratedOrders = assigned.mapNotNull { it.rating?.stars }
                            val avgRating = if (ratedOrders.isNotEmpty()) ratedOrders.average() else null
                            setState { copy(runnerRating = avgRating) }
                        }
                        .launchIn(viewModelScope)
                }

                val next = req?.nextRunnerStatus()
                val nextCta = when (next) {
                    RequestStatus.InProgress -> "Start work"
                    RequestStatus.Purchased -> "Mark purchased"
                    RequestStatus.OnTheWay ->
                        if (req?.status == RequestStatus.InProgress &&
                            req.category != RequestCategory.Shopping
                        ) {
                            "On the way"
                        } else {
                            "On the way"
                        }
                    RequestStatus.Delivered -> "Mark delivered"
                    else -> null
                }
                setState {
                    copy(
                        isLoading = false,
                        request = req,
                        isRequester = isRequester,
                        isRunner = isRunner,
                        runnerNextStatus = if (isRunner) next else null,
                        runnerNextCta = if (isRunner) nextCta else null,
                        errorMessage = null,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private val isAr: Boolean get() = java.util.Locale.getDefault().language == "ar"

    override fun handleEvent(event: RequestDetailUiEvent) {
        when (event) {
            RequestDetailUiEvent.Refresh -> Unit
            RequestDetailUiEvent.OpenChat ->
                sendEffect(RequestDetailUiEffect.NavigateChat(requestId))
            RequestDetailUiEvent.OpenRate ->
                sendEffect(RequestDetailUiEffect.NavigateRate(requestId))
            RequestDetailUiEvent.AdvanceStatus -> {
                if (uiState.value.isAdvancing) return
                viewModelScope.launch {
                    val req = uiState.value.request ?: return@launch
                    val next = req.nextRunnerStatus() ?: return@launch
                    setState { copy(isAdvancing = true) }
                    try {
                        when (val result = advanceStatus(requestId, next)) {
                            is Result.Success -> {
                                sendEffect(RequestDetailUiEffect.ShowMessage(if (isAr) "تم تحديث الحالة" else "Status updated"))
                            }
                            is Result.Error -> {
                                sendEffect(RequestDetailUiEffect.ShowMessage(result.userMessage()))
                            }
                            Result.Loading -> Unit
                        }
                    } finally {
                        setState { copy(isAdvancing = false) }
                    }
                }
            }
            RequestDetailUiEvent.ConfirmDelivery -> {
                if (uiState.value.isConfirming) return
                viewModelScope.launch {
                    setState { copy(isConfirming = true) }
                    try {
                        when (val result = confirmDelivery(requestId)) {
                            is Result.Success -> {
                                sendEffect(RequestDetailUiEffect.NavigateRate(requestId))
                            }
                            is Result.Error -> {
                                sendEffect(RequestDetailUiEffect.ShowMessage(result.userMessage()))
                            }
                            Result.Loading -> Unit
                        }
                    } finally {
                        setState { copy(isConfirming = false) }
                    }
                }
            }
            RequestDetailUiEvent.ShowCancel -> setState { copy(showCancelSheet = true) }
            RequestDetailUiEvent.HideCancel ->
                setState { copy(showCancelSheet = false, cancelReason = "") }
            is RequestDetailUiEvent.CancelReasonChanged ->
                setState { copy(cancelReason = event.value) }
            RequestDetailUiEvent.ConfirmCancel -> {
                if (uiState.value.isCancelling) return
                viewModelScope.launch {
                    setState { copy(isCancelling = true) }
                    try {
                        when (val result = cancelRequest(requestId, uiState.value.cancelReason)) {
                            is Result.Success -> {
                                setState { copy(showCancelSheet = false) }
                                sendEffect(RequestDetailUiEffect.ShowMessage(if (isAr) "تم إلغاء الطلب" else "Request cancelled"))
                                sendEffect(RequestDetailUiEffect.NavigateBack)
                            }
                            is Result.Error -> {
                                sendEffect(RequestDetailUiEffect.ShowMessage(result.userMessage()))
                            }
                            Result.Loading -> Unit
                        }
                    } finally {
                        setState { copy(isCancelling = false) }
                    }
                }
            }
            RequestDetailUiEvent.Back -> sendEffect(RequestDetailUiEffect.NavigateBack)
        }
    }
}
