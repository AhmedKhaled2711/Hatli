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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.lifecycleSteps
import com.app.hatli.core.model.stepperIndex
import com.app.hatli.feature.request.R

private val SuccessGreen = Color(0xFF0F9F55)

@Composable
private fun RequestStatus.stepperLabel(): String = when (this) {
    RequestStatus.InProgress -> stringResource(R.string.request_status_track_in_progress)
    RequestStatus.OnTheWay -> stringResource(R.string.request_status_track_on_the_way)
    else -> timelineLabel()
}

@Composable
fun DetailStatusTrack(req: Request, isAr: Boolean = false) {
    if (req.status == RequestStatus.Cancelled) return

    val steps = lifecycleSteps(req.category)
    val current = if (req.status == RequestStatus.Pending) {
        0
    } else {
        req.status.stepperIndex(req.category).coerceIn(0, steps.lastIndex)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "PulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulseScale",
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulseAlpha",
    )

    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
        ) {
            Text(
                text = stringResource(R.string.request_detail_order_progress),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
            )

            // Coordinated Horizontal Visual Track and Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                steps.forEachIndexed { index, step ->
                    val reached = index <= current
                    val isCurrent = index == current

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Indicator row with left line, circle, right line
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val leftColor by animateColorAsState(
                                targetValue = if (index > 0) {
                                    if (index <= current) SuccessGreen else MaterialTheme.colorScheme.outlineVariant
                                } else {
                                    Color.Transparent
                                },
                                animationSpec = tween(400),
                                label = "leftBarColor_$index",
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.5.dp)
                                    .background(leftColor),
                            )

                            // Circle Indicator
                            Box(
                                modifier = Modifier.size(24.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (isCurrent) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .graphicsLayer {
                                                scaleX = pulseScale
                                                scaleY = pulseScale
                                                alpha = pulseAlpha
                                            }
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary),
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(if (isCurrent) 16.dp else 12.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isCurrent -> MaterialTheme.colorScheme.primary
                                                reached -> SuccessGreen
                                                else -> MaterialTheme.colorScheme.outlineVariant
                                            },
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    if (reached && !isCurrent) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(8.dp),
                                        )
                                    }
                                }
                            }

                            val rightColor by animateColorAsState(
                                targetValue = if (index < steps.lastIndex) {
                                    if (index < current) SuccessGreen else MaterialTheme.colorScheme.outlineVariant
                                } else {
                                    Color.Transparent
                                },
                                animationSpec = tween(400),
                                label = "rightBarColor_$index",
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.5.dp)
                                    .background(rightColor),
                            )
                        }

                        Spacer(modifier = Modifier.height(HatliSpacing.xs))

                        // Step text label perfectly aligned under the circle
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            Text(
                                text = step.stepperLabel(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp,
                                    letterSpacing = (-0.2).sp,
                                ),
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                softWrap = true,
                                color = when {
                                    index == current -> MaterialTheme.colorScheme.primary
                                    index < current -> SuccessGreen
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                fontWeight = if (index == current) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.wrapContentWidth(unbounded = true),
                            )
                        }
                    }
                }
            }
        }
    }
}
