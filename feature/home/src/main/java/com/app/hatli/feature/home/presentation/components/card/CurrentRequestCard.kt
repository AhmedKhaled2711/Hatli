package com.app.hatli.feature.home.presentation.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.badge.CategoryBadge
import com.app.hatli.feature.home.presentation.components.badge.StatusBadge
import com.app.hatli.feature.home.presentation.components.formatting.isAppArabic
import com.app.hatli.feature.home.presentation.components.formatting.formatEgp
import com.app.hatli.feature.home.presentation.components.formatting.formatRoute
import com.app.hatli.feature.home.presentation.components.RequestStatusStepper

@Composable
fun CurrentRequestCard(
    summary: RequestSummary,
    isAr: Boolean = isAppArabic(),
    onChat: () -> Unit,
    onDetails: () -> Unit,
    onConfirm: (() -> Unit)? = null,
    onRate: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val extended = HatliTheme.extendedColors
    val feeFormatted = formatEgp(summary.offerAmount, isAr)
    val defaultCaptain = stringResource(R.string.home_card_default_captain_name)
    val statusLabel = when (summary.status) {
        RequestStatus.Pending -> stringResource(R.string.home_card_status_looking_for_captain)
        RequestStatus.Accepted -> stringResource(
            R.string.home_card_status_captain_accepted,
            summary.runnerName ?: defaultCaptain,
        )
        RequestStatus.InProgress -> stringResource(R.string.home_card_status_captain_working)
        RequestStatus.Purchased -> stringResource(R.string.home_card_status_items_purchased)
        RequestStatus.OnTheWay -> stringResource(R.string.home_card_status_on_the_way)
        RequestStatus.Delivered -> stringResource(R.string.home_card_status_delivered_confirm)
        RequestStatus.Completed -> if (summary.needsRating) {
            stringResource(R.string.home_card_status_completed_rate)
        } else {
            stringResource(R.string.home_status_completed)
        }
        RequestStatus.Cancelled -> stringResource(R.string.home_status_cancelled)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onDetails),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.sm,
        shadowElevation = HatliElevation.sm,
        border = BorderStroke(1.dp, extended.border),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Header: Category Pill + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CategoryBadge(category = summary.category, isAr = isAr)

                StatusBadge(
                    status = summary.status,
                    isAr = isAr,
                    runnerName = summary.runnerName,
                )
            }

            // Status Headline & Title
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = statusLabel,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                    ),
                )

                Text(
                    text = summary.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Route or location if available
                val routeText = formatRoute(
                    pickupLabel = summary.pickupLabel,
                    dropoffLabel = summary.dropoffLabel,
                    isAr = isAr,
                )
                if (routeText != null) {
                    Text(
                        text = routeText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Status Stepper
            RequestStatusStepper(
                status = summary.status,
                category = summary.category,
            )

            val runnerName = summary.runnerName
            if (runnerName != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, extended.border),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(BrandTeal.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = BrandTeal,
                                    modifier = Modifier.size(18.dp),
                                )
                            }

                            Column {
                                Text(
                                    text = runnerName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp,
                                    ),
                                )
                                Text(
                                    text = stringResource(R.string.home_card_delivery_captain_role),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp,
                                    ),
                                )
                            }
                        }

                        if (feeFormatted.isNotBlank()) {
                            Text(
                                text = feeFormatted,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandCoral,
                                    fontSize = 13.sp,
                                ),
                            )
                        }
                    }
                }
            }

            // Messages notice if unread
            if (summary.unreadCount > 0) {
                Text(
                    text = stringResource(R.string.home_runner_new_messages, summary.unreadCount),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = BrandCoral,
                    ),
                )
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                if (summary.canOpenChat) {
                    HatliSecondaryButton(
                        text = stringResource(R.string.home_card_chat),
                        onClick = onChat,
                        modifier = Modifier.weight(1f),
                    )
                }
                HatliSecondaryButton(
                    text = stringResource(R.string.home_card_details),
                    onClick = onDetails,
                    modifier = Modifier.weight(1f),
                )
            }

            if (onConfirm != null) {
                HatliPrimaryButton(
                    text = stringResource(R.string.home_card_confirm_delivery),
                    onClick = onConfirm,
                )
            }

            if (onRate != null) {
                HatliPrimaryButton(
                    text = stringResource(R.string.home_card_rate_runner),
                    onClick = onRate,
                )
            }
        }
    }
}
