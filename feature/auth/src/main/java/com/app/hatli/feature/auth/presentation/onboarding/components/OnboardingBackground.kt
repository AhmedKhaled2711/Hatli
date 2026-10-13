package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliTheme

/**
 * Clean, modern background for onboarding:
 * Premium light-first canvas with an organic Warm Coral ambient aura at the top,
 * blending seamlessly into the base theme background.
 */
@Composable
fun OnboardingBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val isDark = HatliTheme.isDark
    val baseBackground = MaterialTheme.colorScheme.background

    val auraColor = if (isDark) {
        HatliColors.BrandCoral.copy(alpha = 0.08f)
    } else {
        HatliColors.CoralContainer.copy(alpha = 0.65f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(baseBackground),
    ) {
        // Soft ambient brand glow at top header/visual area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            auraColor,
                            auraColor.copy(alpha = 0.2f),
                            Color.Transparent,
                        ),
                    ),
                ),
        )

        content()
    }
}
