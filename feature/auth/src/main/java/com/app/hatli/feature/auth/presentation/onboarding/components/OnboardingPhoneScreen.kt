package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingPageType
import com.app.hatli.feature.auth.presentation.onboarding.components.preview.CreateSupermarketRequestPreview
import com.app.hatli.feature.auth.presentation.onboarding.components.preview.FeeReviewRequestPreview
import com.app.hatli.feature.auth.presentation.onboarding.components.preview.TrackPendingRequestPreview

/**
 * Authentic, production-grade phone preview dispatcher mirroring Hatli's actual client-side UI:
 * - Screen 1: Create Supermarket Request (CreateRequestScreen Step 2 - DetailsStep)
 * - Screen 2: Review Request & Captain Delivery Fee (CreateRequestScreen Step 3 - FeeReviewStep)
 * - Screen 3: Track Pending Request (RequestDetailScreen - Searching for a Captain)
 */
@Composable
fun OnboardingPhoneScreen(
    page: OnboardingPageType,
    modifier: Modifier = Modifier,
) {
    when (page) {
        OnboardingPageType.OrderNearby -> CreateSupermarketRequestPreview(modifier)
        OnboardingPageType.CreateRequest -> FeeReviewRequestPreview(modifier)
        OnboardingPageType.NearbyRunners -> TrackPendingRequestPreview(modifier)
    }
}
