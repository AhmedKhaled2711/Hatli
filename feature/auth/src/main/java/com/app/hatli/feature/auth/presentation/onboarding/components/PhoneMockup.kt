package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliTheme

/**
 * Polished, modern mobile frame showcase that displays a clean, realistic preview
 * of Hatli app capabilities without clutter or awkward clipping.
 */
@Composable
fun PhoneMockup(
    modifier: Modifier = Modifier,
    screenContent: @Composable BoxScope.() -> Unit,
) {
    val isDark = HatliTheme.isDark
    val bezelShape = RoundedCornerShape(30.dp)
    val innerScreenShape = RoundedCornerShape(24.dp)

    val frameBg = if (isDark) Color(0xFF23262A) else Color.White
    val frameBorder = if (isDark) Color(0xFF383B40) else HatliColors.Gray200

    Box(
        modifier = modifier
            .fillMaxHeight(0.93f)
            .aspectRatio(9f / 18.5f, matchHeightConstraintsFirst = true)
            .shadow(
                elevation = HatliElevation.md,
                shape = bezelShape,
                spotColor = if (isDark) Color.Black.copy(alpha = 0.5f) else HatliColors.BrandCoral.copy(alpha = 0.15f),
            )
            .clip(bezelShape)
            .background(frameBg)
            .border(width = 1.5.dp, color = frameBorder, shape = bezelShape)
            .padding(all = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Minimal subtle camera pill at top center
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 3.dp)
                .width(36.dp)
                .height(3.5.dp)
                .clip(CircleShape)
                .background(if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.18f)),
        )

        // Screen area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(innerScreenShape)
                .background(MaterialTheme.colorScheme.background),
            content = screenContent,
        )
    }
}
