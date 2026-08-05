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
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeRequest: ObserveRequestUseCase,
    private val authRepository: AuthRepository,
    private val cancelRequest: CancelRequestUseCase,
    private val advanceStatus: AdvanceRequestStatusUseCase,
    private val confirmDelivery: ConfirmDeliveryUseCase,
) : BaseViewModel<RequestDetailUiState, RequestDetailUiEvent, RequestDetailUiEffect>(
    RequestDetailUiState(),
) {
    private val requestId: String = checkNotNull(savedStateHandle["requestId"])

    init {
        observeRequest(requestId)
            .onEach { req ->
                val uid = authRepository.currentUser()?.uid.orEmpty()
                val isRequester = req != null && req.requesterId == uid
                val isRunner = req != null && req.runnerId == uid
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
                        runnerNextCta = if (isRunner) nextCta else null,
                        errorMessage = null,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun handleEvent(event: RequestDetailUiEvent) {
        when (event) {
            RequestDetailUiEvent.Refresh -> Unit
            RequestDetailUiEvent.OpenChat ->
                sendEffect(RequestDetailUiEffect.NavigateChat(requestId))
            RequestDetailUiEvent.OpenRate ->
                sendEffect(RequestDetailUiEffect.NavigateRate(requestId))
            RequestDetailUiEvent.AdvanceStatus -> viewModelScope.launch {
                val req = uiState.value.request ?: return@launch
                val next = req.nextRunnerStatus() ?: return@launch
                setState { copy(isAdvancing = true) }
                when (val result = advanceStatus(requestId, next)) {
                    is Result.Success -> {
                        setState { copy(isAdvancing = false) }
                        sendEffect(RequestDetailUiEffect.ShowMessage("Status updated"))
                    }
                    is Result.Error -> {
                        setState { copy(isAdvancing = false) }
                        sendEffect(RequestDetailUiEffect.ShowMessage(result.userMessage()))
                    }
                    Result.Loading -> Unit
                }
            }
            RequestDetailUiEvent.ConfirmDelivery -> viewModelScope.launch {
                setState { copy(isConfirming = true) }
                when (val result = confirmDelivery(requestId)) {
                    is Result.Success -> {
                        setState { copy(isConfirming = false) }
                        sendEffect(RequestDetailUiEffect.NavigateRate(requestId))
                    }
                    is Result.Error -> {
                        setState { copy(isConfirming = false) }
                        sendEffect(RequestDetailUiEffect.ShowMessage(result.userMessage()))
                    }
                    Result.Loading -> Unit
                }
            }
            RequestDetailUiEvent.ShowCancel -> setState { copy(showCancelSheet = true) }
            RequestDetailUiEvent.HideCancel ->
                setState { copy(showCancelSheet = false, cancelReason = "") }
            is RequestDetailUiEvent.CancelReasonChanged ->
                setState { copy(cancelReason = event.value) }
            RequestDetailUiEvent.ConfirmCancel -> viewModelScope.launch {
                setState { copy(isCancelling = true) }
                when (val result = cancelRequest(requestId, uiState.value.cancelReason)) {
                    is Result.Success -> {
                        setState { copy(isCancelling = false, showCancelSheet = false) }
                        sendEffect(RequestDetailUiEffect.ShowMessage("Request cancelled"))
                        sendEffect(RequestDetailUiEffect.NavigateBack)
                    }
                    is Result.Error -> {
                        setState { copy(isCancelling = false) }
                        sendEffect(RequestDetailUiEffect.ShowMessage(result.userMessage()))
                    }
                    Result.Loading -> Unit
                }
            }
            RequestDetailUiEvent.Back -> sendEffect(RequestDetailUiEffect.NavigateBack)
        }
    }
}
