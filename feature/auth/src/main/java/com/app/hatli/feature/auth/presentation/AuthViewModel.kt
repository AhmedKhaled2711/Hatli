package com.app.hatli.feature.auth.presentation

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() :
    BaseViewModel<AuthUiState, AuthUiEvent, AuthUiEffect>(AuthUiState()) {

    override fun handleEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailChanged -> setState { copy(email = event.value, errorMessage = null) }
            is AuthUiEvent.PasswordChanged -> setState { copy(password = event.value, errorMessage = null) }
            AuthUiEvent.Back -> sendEffect(AuthUiEffect.NavigateBack)
            AuthUiEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val state = uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            setState { copy(errorMessage = "Email and password are required") }
            return
        }
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            delay(500) // Phase 0 stub — replace with AuthUseCase
            setState { copy(isLoading = false) }
            sendEffect(AuthUiEffect.LoginSuccess)
        }
    }
}
