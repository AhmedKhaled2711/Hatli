package com.app.hatli.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.usecase.home.ObserveRequesterHomeUseCase
import com.app.hatli.core.domain.usecase.home.ObserveRequesterRequestsUseCase
import com.app.hatli.core.domain.usecase.home.ObserveRunnerHomeUseCase
import com.app.hatli.core.domain.usecase.home.ObserveRunnerJobsUseCase
import com.app.hatli.core.domain.usecase.home.SetRunnerOnlineUseCase
import com.app.hatli.core.domain.usecase.request.AcceptRequestUseCase
import com.app.hatli.core.domain.usecase.request.AdvanceRequestStatusUseCase
import com.app.hatli.core.domain.usecase.request.ConfirmDeliveryUseCase
import com.app.hatli.core.domain.usecase.user.ObserveUserProfileUseCase
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainShellViewModel @Inject constructor(
    observeUserProfile: ObserveUserProfileUseCase,
    private val observeRequesterHome: ObserveRequesterHomeUseCase,
    private val observeRunnerHome: ObserveRunnerHomeUseCase,
    private val observeRequesterRequests: ObserveRequesterRequestsUseCase,
    private val observeRunnerJobs: ObserveRunnerJobsUseCase,
    private val setRunnerOnline: SetRunnerOnlineUseCase,
    private val acceptRequest: AcceptRequestUseCase,
    private val advanceStatus: AdvanceRequestStatusUseCase,
    private val confirmDelivery: ConfirmDeliveryUseCase,
) : BaseViewModel<MainShellUiState, MainShellUiEvent, MainShellUiEffect>(MainShellUiState()) {

    init {
        observeUserProfile()
            .onEach { profile ->
                setState {
                    copy(
                        isLoadingProfile = false,
                        role = profile?.role,
                    )
                }
                when (profile?.role) {
                    UserRole.Requester -> startRequesterFeeds()
                    UserRole.Runner -> startRunnerFeeds()
                    null -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    private var requesterStarted = false
    private var runnerStarted = false

    private fun startRequesterFeeds() {
        if (requesterStarted) return
        requesterStarted = true
        observeRequesterHome()
            .onEach { snap -> setState { copy(requesterHome = snap) } }
            .launchIn(viewModelScope)
        observeRequesterRequests()
            .onEach { list -> setState { copy(allRequests = list) } }
            .launchIn(viewModelScope)
    }

    private fun startRunnerFeeds() {
        if (runnerStarted) return
        runnerStarted = true
        observeRunnerHome()
            .onEach { snap -> setState { copy(runnerHome = snap) } }
            .launchIn(viewModelScope)
        observeRunnerJobs()
            .onEach { list -> setState { copy(allJobs = list) } }
            .launchIn(viewModelScope)
    }

    override fun handleEvent(event: MainShellUiEvent) {
        when (event) {
            is MainShellUiEvent.TabSelected -> setState { copy(selectedTab = event.tab) }
            MainShellUiEvent.CreateRequest -> sendEffect(MainShellUiEffect.NavigateCreateRequest)
            MainShellUiEvent.OpenNotifications ->
                sendEffect(MainShellUiEffect.ShowMessage("Notifications coming soon"))
            MainShellUiEvent.ToggleRunnerOnline -> viewModelScope.launch {
                val online = uiState.value.runnerHome?.presence?.isOnline == true
                setRunnerOnline(!online)
            }
            MainShellUiEvent.ViewAllJobs -> setState { copy(selectedTab = MainTab.RequestsOrJobs) }
            is MainShellUiEvent.RequestFilterSelected ->
                setState { copy(requestFilter = event.filter) }
            is MainShellUiEvent.JobFilterSelected ->
                setState { copy(jobFilter = event.filter) }
            is MainShellUiEvent.QuickActionClicked ->
                sendEffect(MainShellUiEffect.NavigateCreateRequest)
            is MainShellUiEvent.AcceptRequest -> viewModelScope.launch {
                when (val result = acceptRequest(event.requestId)) {
                    is Result.Success -> {
                        sendEffect(MainShellUiEffect.ShowMessage("Job accepted — it's now your Active Job"))
                        sendEffect(MainShellUiEffect.NavigateDetail(event.requestId))
                    }
                    is Result.Error ->
                        sendEffect(MainShellUiEffect.ShowMessage(result.userMessage()))
                    Result.Loading -> Unit
                }
            }
            is MainShellUiEvent.AdvanceStatus -> viewModelScope.launch {
                when (val result = advanceStatus(event.requestId, event.toStatus)) {
                    is Result.Success ->
                        sendEffect(MainShellUiEffect.ShowMessage("Status updated"))
                    is Result.Error ->
                        sendEffect(MainShellUiEffect.ShowMessage(result.userMessage()))
                    Result.Loading -> Unit
                }
            }
            is MainShellUiEvent.ConfirmDelivery -> viewModelScope.launch {
                when (val result = confirmDelivery(event.requestId)) {
                    is Result.Success ->
                        sendEffect(MainShellUiEffect.NavigateRate(event.requestId))
                    is Result.Error ->
                        sendEffect(MainShellUiEffect.ShowMessage(result.userMessage()))
                    Result.Loading -> Unit
                }
            }
            is MainShellUiEvent.OpenChat ->
                sendEffect(MainShellUiEffect.NavigateChat(event.requestId))
            is MainShellUiEvent.OpenDetail ->
                sendEffect(MainShellUiEffect.NavigateDetail(event.requestId))
            is MainShellUiEvent.OpenRate ->
                sendEffect(MainShellUiEffect.NavigateRate(event.requestId))
        }
    }
}
