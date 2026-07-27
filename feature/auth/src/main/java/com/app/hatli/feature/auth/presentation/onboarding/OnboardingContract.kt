package com.app.hatli.feature.auth.presentation.onboarding

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class OnboardingPage(
    val title: String,
    val body: String,
)

data class OnboardingUiState(
    val pageIndex: Int = 0,
    val pages: List<OnboardingPage> = listOf(
        OnboardingPage("Welcome to Hatli", "Request and deliver with confidence."),
        OnboardingPage("Track deliveries", "Follow every order in real time."),
        OnboardingPage("Fast payments", "Pay securely and get paid faster."),
    ),
) : UiState {
    val isLast: Boolean get() = pageIndex >= pages.lastIndex
}

sealed interface OnboardingUiEvent : UiEvent {
    data object Next : OnboardingUiEvent
    data object Skip : OnboardingUiEvent
    data object GetStarted : OnboardingUiEvent
}

sealed interface OnboardingUiEffect : UiEffect {
    data object GoLogin : OnboardingUiEffect
}
