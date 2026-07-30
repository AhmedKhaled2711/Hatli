package com.app.hatli.feature.auth.presentation.onboarding

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

enum class OnboardingIllustration {
    Roles,
    Track,
    Pay,
}

data class OnboardingPage(
    val title: String,
    val body: String,
    val illustration: OnboardingIllustration,
)

data class OnboardingUiState(
    val pageIndex: Int = 0,
    val pages: List<OnboardingPage> = listOf(
        OnboardingPage(
            title = "Request or deliver",
            body = "One marketplace for two roles — post a delivery or earn by running nearby jobs.",
            illustration = OnboardingIllustration.Roles,
        ),
        OnboardingPage(
            title = "Track every step",
            body = "Follow pickups and drop-offs in real time so you always know where things stand.",
            illustration = OnboardingIllustration.Track,
        ),
        OnboardingPage(
            title = "Pay and get paid simply",
            body = "Clear pricing for requesters and fast settlement for runners — no guessing.",
            illustration = OnboardingIllustration.Pay,
        ),
    ),
) : UiState {
    val isLast: Boolean get() = pageIndex >= pages.lastIndex
}

sealed interface OnboardingUiEvent : UiEvent {
    data object Next : OnboardingUiEvent
    data object Skip : OnboardingUiEvent
    data object GetStarted : OnboardingUiEvent
    data class PageSelected(val index: Int) : OnboardingUiEvent
}

sealed interface OnboardingUiEffect : UiEffect {
    data object GoLogin : OnboardingUiEffect
}
