package com.app.hatli.feature.auth.presentation.onboarding

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

/**
 * Onboarding page identity. Copy lives in `res/values` / `values-ar` strings
 * and is resolved in UI via [onboardingTitle] / [onboardingBody].
 */
enum class OnboardingPageType {
    OrderNearby,
    CreateRequest,
    NearbyRunners,
}

data class OnboardingUiState(
    val pageIndex: Int = 0,
    val pages: List<OnboardingPageType> = OnboardingPageType.entries,
) : UiState {
    val isLast: Boolean get() = pageIndex >= pages.lastIndex
    val currentPage: OnboardingPageType get() = pages[pageIndex]
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
