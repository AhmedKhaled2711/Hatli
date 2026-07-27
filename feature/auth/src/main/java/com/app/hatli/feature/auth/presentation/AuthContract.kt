package com.app.hatli.feature.auth.presentation

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface AuthUiEvent : UiEvent {
    data class EmailChanged(val value: String) : AuthUiEvent
    data class PasswordChanged(val value: String) : AuthUiEvent
    data object Submit : AuthUiEvent
    data object Back : AuthUiEvent
}

sealed interface AuthUiEffect : UiEffect {
    data object NavigateBack : AuthUiEffect
    data object LoginSuccess : AuthUiEffect
}
