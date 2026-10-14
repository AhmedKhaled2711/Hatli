package com.app.hatli.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.luminance

/**
 * Shimmer effect modifier that provides a synchronized fluid linear gradient sweep across
 * all skeleton elements on screen.
 *
 * Automatically adapts between Light (#E9ECEF -> #F8F9FA) and Dark (#24262A -> #35393F) themes
 * anchored directly to the active MaterialTheme surface luminance.
 */
fun Modifier.shimmerEffect(
    shape: Shape = RoundedCornerShape(8.dp),
    baseColor: Color? = null,
    highlightColor: Color? = null,
): Modifier = composed {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val isDark = surfaceColor.luminance() < 0.5f
    val defaultBase = if (isDark) Color(0xFF24262A) else Color(0xFFE9ECEF)
    val defaultHighlight = if (isDark) Color(0xFF35393F) else Color(0xFFF8F9FA)

    val actualBase = baseColor ?: defaultBase
    val actualHighlight = highlightColor ?: defaultHighlight

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }

    val transition = rememberInfiniteTransition(label = "HatliShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = -screenWidthPx,
        targetValue = screenWidthPx * 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "HatliShimmerTranslate",
    )

    val brush = Brush.linearGradient(
        colors = listOf(actualBase, actualHighlight, actualBase),
        start = Offset(translateAnim - screenWidthPx * 0.45f, translateAnim - screenWidthPx * 0.45f),
        end = Offset(translateAnim, translateAnim),
    )

    this
        .clip(shape)
        .background(brush)
}

/**
 * Convenience composable for a skeleton shimmer box placeholder with rounded corners.
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    baseColor: Color? = null,
    highlightColor: Color? = null,
) {
    Box(
        modifier = modifier.shimmerEffect(
            shape = shape,
            baseColor = baseColor,
            highlightColor = highlightColor,
        ),
    )
}
