package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliTheme

/**
 * Onboarding chrome: full-bleed page art, with drawn teal + circle fallback.
 */
@Composable
fun OnboardingBackground(
    modifier: Modifier = Modifier,
    @DrawableRes backgroundRes: Int? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(
            targetState = backgroundRes,
            label = "onboardingBackground",
            modifier = Modifier.fillMaxSize(),
        ) { res ->
            if (res != null) {
                Image(
                    painter = painterResource(res),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                DefaultOnboardingHeaderBackground()
            }
        }
        content()
    }
}

/** Teal header band + oversized dark circle (Hatli Primary / Teal900). */
@Composable
private fun DefaultOnboardingHeaderBackground() {
    val isDark = HatliTheme.isDark
    val headerTeal = HatliColors.Teal500
    val circleDark = if (isDark) HatliColors.Gray800 else HatliColors.Teal900

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val circleSize = screenWidth * 1.95f

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(headerTeal),
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 88.dp)
                .size(circleSize)
                .clip(CircleShape)
                .background(circleDark),
        )
    }
}
