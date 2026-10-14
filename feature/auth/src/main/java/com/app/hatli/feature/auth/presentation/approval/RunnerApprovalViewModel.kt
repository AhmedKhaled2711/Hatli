package com.app.hatli.feature.auth.presentation.approval

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.usecase.auth.LogoutUseCase
import com.app.hatli.core.domain.usecase.user.GetUserProfileUseCase
import com.app.hatli.core.domain.usecase.user.ObserveUserProfileUseCase
import com.app.hatli.core.model.VerificationStatus
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunnerApprovalViewModel @Inject constructor(
    private val observeUserProfile: ObserveUserProfileUseCase,
    private val getUserProfile: GetUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<RunnerApprovalUiState, RunnerApprovalUiEvent, RunnerApprovalUiEffect>(
    RunnerApprovalUiState(),
) {

    private var refreshJob: Job? = null
    private var logoutJob: Job? = null
    private var hasNavigatedHome = false

    init {
        observeProfile()
    }

    override fun handleEvent(event: RunnerApprovalUiEvent) {
        when (event) {
            RunnerApprovalUiEvent.Refresh -> refreshStatus()
            RunnerApprovalUiEvent.Logout -> logout()
            RunnerApprovalUiEvent.ClearError -> setState { copy(errorMessage = null) }
        }
    }

    private fun observeProfile() {
        observeUserProfile()
            .catch { e ->
                setState {
                    copy(
                        isLoading = false,
                        errorMessage = e.message,
                    )
                }
            }
            .onEach { profile ->
                if (profile != null) {
                    val isApproved = profile.verificationStatus == VerificationStatus.Approved
                    setState {
                        copy(
                            isLoading = false,
                            status = profile.verificationStatus,
                            runnerName = profile.fullName,
                            phone = profile.phone,
                            transportType = profile.transportType,
                            vehicleInfo = profile.vehicleInfo,
                        )
                    }
                    if (isApproved && !hasNavigatedHome) {
                        hasNavigatedHome = true
                        sendEffect(RunnerApprovalUiEffect.GoHome)
                    }
                } else {
                    setState { copy(isLoading = false) }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun refreshStatus() {
        if (uiState.value.isRefreshing || refreshJob?.isActive == true) return
        setState { copy(isRefreshing = true, errorMessage = null) }
        refreshJob = viewModelScope.launch {
            try {
                when (val result = getUserProfile(forceRefresh = true)) {
                    is Result.Success -> {
                        val profile = result.data
                        val isApproved = profile?.verificationStatus == VerificationStatus.Approved
                        setState {
                            copy(
                                isRefreshing = false,
                                status = profile?.verificationStatus,
                                runnerName = profile?.fullName ?: runnerName,
                                phone = profile?.phone ?: phone,
                                transportType = profile?.transportType ?: transportType,
                                vehicleInfo = profile?.vehicleInfo ?: vehicleInfo,
                                lastCheckedEpochMs = System.currentTimeMillis(),
                            )
                        }
                        if (isApproved && !hasNavigatedHome) {
                            hasNavigatedHome = true
                            sendEffect(RunnerApprovalUiEffect.GoHome)
                        }
                    }

                    is Result.Error -> {
                        val msg = result.userMessage()
                        setState {
                            copy(
                                isRefreshing = false,
                                errorMessage = msg,
                                lastCheckedEpochMs = System.currentTimeMillis(),
                            )
                        }
                        sendEffect(RunnerApprovalUiEffect.ShowMessage(msg))
                    }

                    Result.Loading -> Unit
                }
            } catch (t: Throwable) {
                val msg = t.message ?: "Failed to refresh status"
                setState {
                    copy(
                        isRefreshing = false,
                        errorMessage = msg,
                    )
                }
                sendEffect(RunnerApprovalUiEffect.ShowMessage(msg))
            }
        }
    }

    private fun logout() {
        if (logoutJob?.isActive == true) return
        logoutJob = viewModelScope.launch {
            logoutUseCase()
            sendEffect(RunnerApprovalUiEffect.GoAuth)
        }
    }
}
