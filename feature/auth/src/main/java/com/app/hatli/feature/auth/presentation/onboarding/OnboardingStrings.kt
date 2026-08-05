package com.app.hatli.feature.auth.presentation.onboarding

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.app.hatli.feature.auth.R

/**
 * Single place for onboarding [R.string] / [R.drawable] IDs.
 * Keeps Compose screens free of direct R access (helps IDE indexing).
 */
internal object OnboardingStrings {
    val brand: Int get() = R.string.onboarding_brand
    val skip: Int get() = R.string.onboarding_skip
    val next: Int get() = R.string.onboarding_next
    val getStarted: Int get() = R.string.onboarding_get_started

    val mockRequestTitle: Int get() = R.string.onboarding_mock_request_title
    val mockItemMeat: Int get() = R.string.onboarding_mock_item_meat
    val mockItemPepsi: Int get() = R.string.onboarding_mock_item_pepsi
    val mockItemBread: Int get() = R.string.onboarding_mock_item_bread
    val mockAcceptedBy: Int get() = R.string.onboarding_mock_accepted_by
    val mockPage2Hint: Int get() = R.string.onboarding_mock_page2_hint
    val mockPage3Hint: Int get() = R.string.onboarding_mock_page3_hint

    /** Full-bleed background art per onboarding page. */
    val bgPage1: Int get() = R.drawable.onboarding_bg_page1
    val bgPage2: Int get() = R.drawable.onboarding_bg_page2
    val bgPage3: Int get() = R.drawable.onboarding_bg_page3
}

@StringRes
internal fun OnboardingPageType.titleRes(): Int = when (this) {
    OnboardingPageType.OrderNearby -> R.string.onboarding_page1_title
    OnboardingPageType.CreateRequest -> R.string.onboarding_page2_title
    OnboardingPageType.NearbyRunners -> R.string.onboarding_page3_title
}

@StringRes
internal fun OnboardingPageType.bodyRes(): Int = when (this) {
    OnboardingPageType.OrderNearby -> R.string.onboarding_page1_body
    OnboardingPageType.CreateRequest -> R.string.onboarding_page2_body
    OnboardingPageType.NearbyRunners -> R.string.onboarding_page3_body
}

@Composable
internal fun OnboardingPageType.titleText(): String = stringResource(titleRes())

@Composable
internal fun OnboardingPageType.bodyText(): String = stringResource(bodyRes())

@Composable
internal fun onboardingPrimaryLabel(isLast: Boolean): String = stringResource(
    if (isLast) OnboardingStrings.getStarted else OnboardingStrings.next,
)
