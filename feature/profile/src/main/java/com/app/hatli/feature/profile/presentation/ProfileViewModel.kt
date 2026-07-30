package com.app.hatli.feature.profile.presentation

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.usecase.auth.LogoutUseCase
import com.app.hatli.core.domain.usecase.user.GetUserProfileUseCase
import com.app.hatli.core.domain.usecase.user.ObserveUserProfileUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    observeUserProfile: ObserveUserProfileUseCase,
    private val getUserProfile: GetUserProfileUseCase,
    private val logout: LogoutUseCase,
) : BaseViewModel<ProfileUiState, ProfileUiEvent, ProfileUiEffect>(ProfileUiState()) {

    private var observeJob: Job? = null

    init {
        observeJob = observeUserProfile()
            .catch { emit(null) }
            .onEach { profile ->
                setState { copy(isLoading = false, profile = profile) }
            }
            .launchIn(viewModelScope)
        onEvent(ProfileUiEvent.Refresh)
    }

    override fun handleEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.Refresh -> refresh()
            ProfileUiEvent.Logout -> signOut()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            when (val result = getUserProfile()) {
                is Result.Success -> setState {
                    copy(isLoading = false, profile = result.data)
                }
                is Result.Error -> setState {
                    copy(isLoading = false, errorMessage = result.userMessage())
                }
                Result.Loading -> Unit
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            // Stop Firestore listen before Auth.signOut() to avoid PERMISSION_DENIED crash.
            observeJob?.cancel()
            observeJob = null
            logout()
            sendEffect(ProfileUiEffect.LoggedOut)
        }
    }
}
