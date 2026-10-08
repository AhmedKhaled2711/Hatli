package com.app.hatli.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.HatliTheme

/**
 * Custom tactile Hatli pill switch matching the brand identity:
 * - Vivid BrandCoral track when checked (ON)
 * - Clean neutral track when unchecked (OFF)
 * - Soft elevated pure white circular thumb knob with smooth sliding transition
 */
@Composable
fun HatliSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    width: Dp = 48.dp,
    height: Dp = 28.dp,
    thumbSize: Dp = 22.dp,
    trackPadding: Dp = 3.dp,
    checkedTrackColor: Color = BrandCoral,
    uncheckedTrackColor: Color = if (HatliTheme.isDark) Color(0xFF2C3036) else Color(0xFFE2E4E8),
    thumbColor: Color = Color.White,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val isDark = HatliTheme.isDark
    val actualUncheckedTrackColor = if (isDark) Color(0xFF2C3036) else Color(0xFFE2E4E8)

    val animatedTrackColor by animateColorAsState(
        targetValue = if (checked) checkedTrackColor else actualUncheckedTrackColor,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "HatliSwitchTrackColor",
    )

    val maxTravel = width - thumbSize - (trackPadding * 2)
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) maxTravel else 0.dp,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "HatliSwitchThumbOffset",
    )

    val clickableModifier = if (onCheckedChange != null && enabled) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            role = Role.Switch,
            onClick = { onCheckedChange(!checked) },
        )
    } else {
        Modifier
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .size(width = width, height = height)
                .clip(CircleShape)
                .background(animatedTrackColor)
                .then(clickableModifier)
                .padding(trackPadding),
            contentAlignment = Alignment.CenterStart,
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset)
                    .size(thumbSize)
                    .shadow(elevation = 3.dp, shape = CircleShape)
                    .background(thumbColor, CircleShape),
            )
        }
    }
}
