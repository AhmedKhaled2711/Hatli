package com.app.hatli.feature.auth.presentation.onboarding

/**
 * Optional Lottie paths per page (assets under `lottie/onboarding/`).
 * Missing files fall back to Compose mock screens inside [components.PhoneMockup].
 */
object OnboardingLottieAssets {
    private const val BASE = "lottie/onboarding"

    fun pathFor(page: OnboardingPageType): String = when (page) {
        OnboardingPageType.OrderNearby -> "$BASE/onboarding_order.json"
        OnboardingPageType.CreateRequest -> "$BASE/onboarding_create.json"
        OnboardingPageType.NearbyRunners -> "$BASE/onboarding_runners.json"
    }
}
