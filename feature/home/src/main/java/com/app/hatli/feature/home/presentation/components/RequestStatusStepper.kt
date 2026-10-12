package com.app.hatli.feature.home.presentation.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.lifecycleSteps
import com.app.hatli.core.model.stepperIndex
import com.app.hatli.feature.home.R

@Composable
fun RequestStatus.stepperLabel(): String = when (this) {
    RequestStatus.Pending -> stringResource(R.string.home_stepper_step_pending)
    RequestStatus.Accepted -> stringResource(R.string.home_stepper_step_accepted)
    RequestStatus.InProgress -> stringResource(R.string.home_stepper_step_in_progress)
    RequestStatus.Purchased -> stringResource(R.string.home_stepper_step_purchased)
    RequestStatus.OnTheWay -> stringResource(R.string.home_stepper_step_on_the_way)
    RequestStatus.Delivered -> stringResource(R.string.home_stepper_step_delivered)
    RequestStatus.Completed -> stringResource(R.string.home_stepper_step_completed)
    RequestStatus.Cancelled -> stringResource(R.string.home_status_cancelled)
}

/**
 * Compact lifecycle visualization for Current Request / Job cards.
 * Pending / Cancelled show a single status chip instead of the full track.
 */
@Composable
fun RequestStatusStepper(
    status: RequestStatus,
    category: RequestCategory?,
    modifier: Modifier = Modifier,
) {
    when (status) {
        RequestStatus.Pending -> {
            StatusChip(
                label = stringResource(R.string.home_stepper_looking_for_runner),
                emphasized = true,
                modifier = modifier.fillMaxWidth(),
            )
        }
        RequestStatus.Cancelled -> {
            StatusChip(
                label = stringResource(R.string.home_status_cancelled),
                emphasized = false,
                modifier = modifier.fillMaxWidth(),
            )
        }
        else -> {
            val steps = lifecycleSteps(category)
            val current = status.stepperIndex(category).coerceIn(0, steps.lastIndex)
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    steps.forEachIndexed { index, step ->
                        val reached = index <= current
                        val isCurrent = index == current
                        Box(
                            modifier = Modifier
                                .size(if (isCurrent) 12.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> MaterialTheme.colorScheme.primary
                                        reached -> MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                                        else -> MaterialTheme.colorScheme.outlineVariant
                                    },
                                ),
                        )
                        if (index < steps.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(2.dp)
                                    .background(
                                        if (index < current) {
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                                        } else {
                                            MaterialTheme.colorScheme.outlineVariant
                                        },
                                    ),
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Show first, current, and last labels to avoid clutter on small phones.
                    val labels = listOf(
                        steps.first(),
                        steps[current],
                        steps.last(),
                    ).distinct()
                    labels.forEach { step ->
                        Text(
                            text = step.stepperLabel(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (step == steps[current]) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (step == steps[current]) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = HatliSpacing.xxxs),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(
    label: String,
    emphasized: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(
                if (emphasized) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
            )
            .padding(horizontal = HatliSpacing.sm, vertical = HatliSpacing.xs),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = if (emphasized) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}
