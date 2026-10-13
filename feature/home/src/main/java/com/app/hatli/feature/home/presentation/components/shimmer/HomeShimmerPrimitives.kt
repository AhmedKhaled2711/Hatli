package com.app.hatli.feature.home.presentation.components.shimmer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.ShimmerBox
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliTheme

/**
 * Common reusable skeleton primitives for Home feature shimmers.
 */
@Composable
fun ShimmerCardContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    content: @Composable () -> Unit,
) {
    val extended = HatliTheme.extendedColors
    Surface(
        modifier = modifier,
        shape = shape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.sm,
        shadowElevation = HatliElevation.sm,
        border = BorderStroke(1.dp, extended.border),
        content = content,
    )
}

@Composable
fun ShimmerHeaderRow(
    pillWidth: Dp,
    pillHeight: Dp = 38.dp,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ShimmerBox(
            modifier = Modifier
                .width(pillWidth)
                .height(pillHeight),
            shape = RoundedCornerShape(20.dp),
        )
        ShimmerBox(
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
        )
    }
}

@Composable
fun ShimmerSectionHeader(
    titleWidth: Dp,
    actionWidth: Dp? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ShimmerBox(
            modifier = Modifier
                .width(titleWidth)
                .height(18.dp),
            shape = RoundedCornerShape(4.dp),
        )
        if (actionWidth != null) {
            ShimmerBox(
                modifier = Modifier
                    .width(actionWidth)
                    .height(14.dp),
                shape = RoundedCornerShape(4.dp),
            )
        }
    }
}

@Composable
fun ShimmerLine(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp = 14.dp,
    cornerRadius: Dp = 4.dp,
) {
    val mod = if (width != null) modifier.width(width).height(height) else modifier.height(height)
    ShimmerBox(
        modifier = mod,
        shape = RoundedCornerShape(cornerRadius),
    )
}

@Composable
fun ShimmerCircle(
    size: Dp,
    modifier: Modifier = Modifier,
) {
    ShimmerBox(
        modifier = modifier.size(size),
        shape = CircleShape,
    )
}
