package com.app.hatli.feature.auth.presentation.splash

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class SplashUiState(
    val isResolving: Boolean = true,
) : UiState

sealed interface SplashUiEvent : UiEvent {
    data object Resolve : SplashUiEvent
}

sealed interface SplashUiEffect : UiEffect {
    data object GoOnboarding : SplashUiEffect
    data object GoStart : SplashUiEffect
    data object GoLogin : SplashUiEffect
    data object GoRoleSelect : SplashUiEffect
    data object GoCreateProfile : SplashUiEffect
    data object GoHome : SplashUiEffect
}
