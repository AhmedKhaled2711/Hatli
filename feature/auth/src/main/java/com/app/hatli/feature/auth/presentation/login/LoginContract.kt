package com.app.hatli.feature.auth.presentation.login

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class LoginUiState(
    val phone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface LoginUiEvent : UiEvent {
    data class PhoneChanged(val value: String) : LoginUiEvent
    data object Submit : LoginUiEvent
}

sealed interface LoginUiEffect : UiEffect {
    data class GoOtp(val verificationId: String, val phone: String) : LoginUiEffect
}
