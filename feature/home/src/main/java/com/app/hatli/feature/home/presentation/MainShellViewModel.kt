package com.app.hatli.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.repository.SessionPreferencesRepository
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
import com.app.hatli.core.model.VerificationStatus
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainShellViewModel @Inject constructor(
    private val observeUserProfile: ObserveUserProfileUseCase,
    private val observeRequesterHome: ObserveRequesterHomeUseCase,
    private val observeRunnerHome: ObserveRunnerHomeUseCase,
    private val observeRequesterRequests: ObserveRequesterRequestsUseCase,
    private val observeRunnerJobs: ObserveRunnerJobsUseCase,
    private val setRunnerOnline: SetRunnerOnlineUseCase,
    private val acceptRequest: AcceptRequestUseCase,
    private val advanceStatus: AdvanceRequestStatusUseCase,
    private val confirmDelivery: ConfirmDeliveryUseCase,
    private val sessionPreferences: SessionPreferencesRepository,
) : BaseViewModel<MainShellUiState, MainShellUiEvent, MainShellUiEffect>(MainShellUiState()) {

    init {
        loadProfileAndFeeds()
    }

    private var requesterStarted = false
    private var runnerStarted = false
    private var runnerBannerCountedForSession = false
    private var toggleOnlineJob: Job? = null
    private val inFlightAcceptIds = java.util.Collections.synchronizedSet(mutableSetOf<String>())
    private val inFlightAdvanceIds = java.util.Collections.synchronizedSet(mutableSetOf<String>())
    private val inFlightConfirmIds = java.util.Collections.synchronizedSet(mutableSetOf<String>())

    private fun loadProfileAndFeeds() {
        observeUserProfile()
            .catch { e ->
                setState {
                    copy(
                        isLoadingProfile = false,
                        isError = true,
                        errorMessage = e.message ?: "Failed to load user profile",
                    )
                }
            }
            .onEach { profile ->
                setState {
                    copy(
                        isLoadingProfile = false,
                        role = profile?.role,
                        isError = false,
                        errorMessage = null,
                    )
                }
                when (profile?.role) {
                    UserRole.Requester -> startRequesterFeeds()
                    UserRole.Runner -> {
                        if (profile.verificationStatus == VerificationStatus.Approved) {
                            startRunnerFeeds()
                        } else {
                            sendEffect(MainShellUiEffect.NavigateRunnerApproval)
                        }
                    }
                    null -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    private fun startRequesterFeeds() {
        if (requesterStarted) return
        requesterStarted = true
        observeRequesterHome()
            .catch { e ->
                setState { copy(isError = true, errorMessage = e.message) }
            }
            .onEach { snap -> setState { copy(requesterHome = snap) } }
            .launchIn(viewModelScope)
        observeRequesterRequests()
            .catch { e ->
                setState { copy(isError = true, errorMessage = e.message) }
            }
            .onEach { list -> setState { copy(allRequests = list) } }
            .launchIn(viewModelScope)
    }

    private fun startRunnerFeeds() {
        if (runnerStarted) return
        runnerStarted = true
        combine(
            observeRunnerHome(),
            sessionPreferences.runnerOnlineBannerDismissed,
            sessionPreferences.runnerOnlineBannerShownCount,
        ) { snap, dismissed, count ->
            val isOnline = snap.presence.isOnline
            val shouldShow = isOnline && !dismissed && count < 6
            if (isOnline) {
                if (!runnerBannerCountedForSession && !dismissed && count < 6) {
                    runnerBannerCountedForSession = true
                    viewModelScope.launch {
                        sessionPreferences.incrementRunnerOnlineBannerCount()
                    }
                }
            } else {
                runnerBannerCountedForSession = false
            }
            Pair(snap, shouldShow)
        }
            .catch { e ->
                setState { copy(isError = true, errorMessage = e.message) }
            }
            .onEach { (snap, shouldShow) ->
                setState {
                    copy(
                        runnerHome = snap,
                        showRunnerOnlineInfoBanner = shouldShow,
                    )
                }
            }
            .launchIn(viewModelScope)

        observeRunnerJobs()
            .catch { e ->
                setState { copy(isError = true, errorMessage = e.message) }
            }
            .onEach { list -> setState { copy(allJobs = list) } }
            .launchIn(viewModelScope)
    }

    private val isAr: Boolean get() = java.util.Locale.getDefault().language == "ar"

    override fun handleEvent(event: MainShellUiEvent) {
        when (event) {
            MainShellUiEvent.Retry -> {
                requesterStarted = false
                runnerStarted = false
                runnerBannerCountedForSession = false
                setState { copy(isError = false, errorMessage = null, isLoadingProfile = true) }
                loadProfileAndFeeds()
            }
            is MainShellUiEvent.TabSelected -> setState { copy(selectedTab = event.tab) }
            MainShellUiEvent.CreateRequest -> sendEffect(MainShellUiEffect.NavigateCreateRequest(null))
            MainShellUiEvent.OpenNotifications ->
                sendEffect(MainShellUiEffect.ShowMessage(if (isAr) "ميزة الإشعارات قريباً" else "Notifications coming soon"))
            MainShellUiEvent.ToggleRunnerOnline -> {
                if (toggleOnlineJob?.isActive == true) return
                toggleOnlineJob = viewModelScope.launch {
                    val online = uiState.value.runnerHome?.presence?.isOnline == true
                    setRunnerOnline(!online)
                }
            }
            MainShellUiEvent.DismissRunnerOnlineInfoBanner -> viewModelScope.launch {
                sessionPreferences.dismissRunnerOnlineBanner()
                setState { copy(showRunnerOnlineInfoBanner = false) }
            }
            MainShellUiEvent.ViewAllJobs -> setState { copy(selectedTab = MainTab.RequestsOrJobs) }
            is MainShellUiEvent.RequestFilterSelected ->
                setState { copy(requestFilter = event.filter) }
            is MainShellUiEvent.JobFilterSelected ->
                setState { copy(jobFilter = event.filter) }
            is MainShellUiEvent.QuickActionClicked ->
                sendEffect(MainShellUiEffect.NavigateCreateRequest(event.id))
            is MainShellUiEvent.AcceptRequest -> {
                if (!inFlightAcceptIds.add(event.requestId)) return
                viewModelScope.launch {
                    try {
                        when (val result = acceptRequest(event.requestId)) {
                            is Result.Success -> {
                                sendEffect(
                                    MainShellUiEffect.ShowMessage(
                                        if (isAr) "تم قبول المهمة — أصبحت مهمتك الحالية" else "Job accepted — added to your active jobs"
                                    )
                                )
                                sendEffect(MainShellUiEffect.NavigateDetail(event.requestId))
                            }
                            is Result.Error ->
                                sendEffect(MainShellUiEffect.ShowMessage(result.userMessage()))
                            Result.Loading -> Unit
                        }
                    } finally {
                        inFlightAcceptIds.remove(event.requestId)
                    }
                }
            }
            is MainShellUiEvent.AdvanceStatus -> {
                if (!inFlightAdvanceIds.add(event.requestId)) return
                viewModelScope.launch {
                    try {
                        when (val result = advanceStatus(event.requestId, event.toStatus)) {
                            is Result.Success ->
                                sendEffect(MainShellUiEffect.ShowMessage(if (isAr) "تم تحديث الحالة" else "Status updated"))
                            is Result.Error ->
                                sendEffect(MainShellUiEffect.ShowMessage(result.userMessage()))
                            Result.Loading -> Unit
                        }
                    } finally {
                        inFlightAdvanceIds.remove(event.requestId)
                    }
                }
            }
            is MainShellUiEvent.ConfirmDelivery -> {
                if (!inFlightConfirmIds.add(event.requestId)) return
                viewModelScope.launch {
                    try {
                        when (val result = confirmDelivery(event.requestId)) {
                            is Result.Success ->
                                sendEffect(MainShellUiEffect.NavigateRate(event.requestId))
                            is Result.Error ->
                                sendEffect(MainShellUiEffect.ShowMessage(result.userMessage()))
                            Result.Loading -> Unit
                        }
                    } finally {
                        inFlightConfirmIds.remove(event.requestId)
                    }
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
