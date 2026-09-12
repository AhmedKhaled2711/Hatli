package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.lifecycleSteps
import com.app.hatli.core.model.shortLabelAr
import com.app.hatli.core.model.shortLabelEn
import com.app.hatli.core.model.stepperIndex
import com.app.hatli.feature.request.R

@Composable
fun DetailStatusTrack(req: Request, isAr: Boolean) {
    if (req.status == RequestStatus.Pending || req.status == RequestStatus.Cancelled) return
    val extendedColors = LocalHatliExtendedColors.current
    val steps = lifecycleSteps(req.category)
    val current = req.status.stepperIndex(req.category).coerceIn(0, steps.lastIndex)

    val infiniteTransition = rememberInfiniteTransition(label = "RadarPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)
        ) {
            Text(
                text = stringResource(R.string.request_detail_order_progress),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, _ ->
                    val reached = index <= current
                    val isCurrent = index == current

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCurrent) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .graphicsLayer {
                                        scaleX = pulseScale
                                        scaleY = pulseScale
                                        alpha = pulseAlpha
                                    }
                                    .clip(CircleShape)
                                    .background(HatliColors.BrandTeal)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(if (isCurrent) 14.dp else 10.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> HatliColors.BrandTeal
                                        reached -> HatliColors.Teal700
                                        else -> extendedColors.border
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (reached && !isCurrent) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(7.dp)
                                )
                            }
                        }
                    }

                    if (index < steps.lastIndex) {
                        val barColor by animateColorAsState(
                            targetValue = if (index < current) HatliColors.Teal700 else extendedColors.border,
                            animationSpec = tween(500),
                            label = "progressBarColor"
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.dp)
                                .background(barColor)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                steps.forEachIndexed { index, step ->
                    val label = if (isAr) step.shortLabelAr() else step.shortLabelEn()
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (index <= current) MaterialTheme.colorScheme.onSurface else extendedColors.onSurfaceVariant,
                        fontWeight = if (index == current) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
