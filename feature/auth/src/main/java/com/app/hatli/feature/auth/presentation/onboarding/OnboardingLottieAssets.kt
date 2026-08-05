package com.app.hatli.feature.auth.presentation.onboarding

/**
 * Drop Lottie JSON files under:
 *
 * `feature/auth/src/main/assets/lottie/onboarding/`
 *
 * File names must match [pathFor] below. Until files exist, onboarding shows the icon fallback.
 */
object OnboardingLottieAssets {
    private const val BASE = "lottie/onboarding"

    fun pathFor(illustration: OnboardingIllustration): String = when (illustration) {
        OnboardingIllustration.Roles -> "$BASE/onboarding_roles.json"
        OnboardingIllustration.Track -> "$BASE/onboarding_track.json"
        OnboardingIllustration.Pay -> "$BASE/onboarding_pay.json"
    }
}
