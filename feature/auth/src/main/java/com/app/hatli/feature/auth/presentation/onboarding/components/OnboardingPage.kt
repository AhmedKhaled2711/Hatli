package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingPageType

/**
 * Phone hero for one page. Aligned toward the top so the lower third
 * sits behind the overlay bottom sheet (FlexBox reference).
 */
@Composable
fun OnboardingPage(
    page: OnboardingPageType,
    modifier: Modifier = Modifier,
    screenContent: (@Composable BoxScope.() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = HatliSpacing.md, vertical = HatliSpacing.xs),
        contentAlignment = Alignment.Center,
    ) {
        PhoneMockup {
            if (screenContent != null) {
                screenContent()
            } else {
                OnboardingPhoneScreen(page = page)
            }
        }
    }
}
