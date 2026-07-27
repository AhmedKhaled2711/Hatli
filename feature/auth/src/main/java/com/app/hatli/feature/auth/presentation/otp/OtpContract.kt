package com.app.hatli.feature.auth.presentation.otp

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class OtpUiState(
    val phone: String = "",
    val verificationId: String = "",
    val code: String = "",
    val secondsLeft: Int = 60,
    val isLoading: Boolean = false,
    val canResend: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface OtpUiEvent : UiEvent {
    data class CodeChanged(val value: String) : OtpUiEvent
    data object Verify : OtpUiEvent
    data object Resend : OtpUiEvent
    data object Tick : OtpUiEvent
}

sealed interface OtpUiEffect : UiEffect {
    data object GoRoleSelect : OtpUiEffect
    data object GoHome : OtpUiEffect
    data object GoCreateProfile : OtpUiEffect
}
