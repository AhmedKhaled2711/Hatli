package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.TaskAlt
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
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.feature.request.R

private val SuccessGreen = Color(0xFF0F9F55)
private val SuccessGreenContainer = Color(0xFFE8F8F0)

@Composable
fun StatusHeroCard(req: Request, isAr: Boolean) {
    val isPending = req.status == RequestStatus.Pending
    val isAcceptedOrProgress = req.status in setOf(RequestStatus.Accepted, RequestStatus.InProgress, RequestStatus.Purchased)
    val isOnTheWay = req.status == RequestStatus.OnTheWay
    val isDelivered = req.status == RequestStatus.Delivered
    val isCompleted = req.status == RequestStatus.Completed
    val isCancelled = req.status == RequestStatus.Cancelled

    val containerBg by animateColorAsState(
        targetValue = when {
            isPending -> MaterialTheme.colorScheme.tertiaryContainer
            isAcceptedOrProgress -> MaterialTheme.colorScheme.primaryContainer
            isOnTheWay -> MaterialTheme.colorScheme.secondaryContainer
            isDelivered || isCompleted -> SuccessGreenContainer
            isCancelled -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "heroColorMorph",
    )

    val accentColor = when {
        isPending -> MaterialTheme.colorScheme.tertiary
        isAcceptedOrProgress -> MaterialTheme.colorScheme.primary
        isOnTheWay -> MaterialTheme.colorScheme.secondary
        isDelivered || isCompleted -> SuccessGreen
        isCancelled -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }

    val infiniteTransition = rememberInfiniteTransition(label = "PendingPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseAlpha",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(containerBg)
            .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .padding(HatliSpacing.lg),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = when {
                                isPending -> Icons.Default.HourglassTop
                                isAcceptedOrProgress -> Icons.AutoMirrored.Filled.DirectionsBike
                                isOnTheWay -> Icons.Default.LocalShipping
                                else -> Icons.Default.TaskAlt
                            },
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    Text(
                        text = when {
                            isPending -> stringResource(R.string.status_hero_pending_title)
                            req.status == RequestStatus.Accepted -> stringResource(R.string.status_hero_accepted_title)
                            isAcceptedOrProgress -> stringResource(R.string.status_hero_in_progress_title)
                            isOnTheWay -> stringResource(R.string.status_hero_on_the_way_title)
                            isDelivered -> stringResource(R.string.status_hero_delivered_title)
                            isCompleted -> stringResource(R.string.status_hero_completed_title)
                            isCancelled -> stringResource(R.string.status_hero_cancelled_title)
                            else -> stringResource(R.string.request_detail_current_status)
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            fontSize = 15.sp,
                        ),
                        modifier = Modifier.graphicsLayer {
                            if (isPending) alpha = pulseAlpha
                        },
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = req.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = when {
                        isPending -> stringResource(R.string.status_hero_pending_desc)
                        req.status == RequestStatus.Accepted -> stringResource(
                            R.string.status_hero_accepted_desc,
                            req.runnerName ?: "",
                        )
                        isAcceptedOrProgress -> stringResource(
                            R.string.status_hero_in_progress_desc,
                            req.runnerName ?: "",
                        )
                        isOnTheWay -> stringResource(R.string.status_hero_on_the_way_desc)
                        isDelivered -> stringResource(R.string.status_hero_delivered_desc)
                        isCompleted -> stringResource(R.string.status_hero_completed_desc)
                        isCancelled -> stringResource(R.string.status_hero_cancelled_desc)
                        else -> stringResource(R.string.request_detail_current_status)
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 12.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
