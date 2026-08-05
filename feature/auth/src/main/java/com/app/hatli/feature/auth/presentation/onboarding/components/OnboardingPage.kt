package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
            .padding(top = HatliSpacing.sm),
        contentAlignment = Alignment.TopCenter,
    ) {
        PhoneMockup {
            if (screenContent != null) {
                screenContent()
            } else {
                AnimatedContent(
                    targetState = page,
                    transitionSpec = {
                        (fadeIn() + scaleIn(initialScale = 0.94f)) togetherWith
                            (fadeOut() + scaleOut(targetScale = 0.94f))
                    },
                    label = "onboardingPhoneScreen",
                    modifier = Modifier.fillMaxSize(),
                ) { current ->
                    OnboardingPhoneScreen(page = current)
                }
            }
        }
    }
}
