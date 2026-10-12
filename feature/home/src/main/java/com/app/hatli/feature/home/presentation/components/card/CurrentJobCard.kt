package com.app.hatli.feature.home.presentation.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.app.hatli.feature.home.presentation.RunnerNextAction
import com.app.hatli.feature.home.presentation.components.badge.CategoryBadge
import com.app.hatli.feature.home.presentation.components.badge.StatusBadge
import com.app.hatli.feature.home.presentation.components.formatting.isAppArabic
import com.app.hatli.feature.home.presentation.components.formatting.formatDistance
import com.app.hatli.feature.home.presentation.components.formatting.formatEgp
import com.app.hatli.feature.home.presentation.components.RequestStatusStepper

@Composable
fun CurrentJobCard(
    summary: RequestSummary,
    nextAction: RunnerNextAction?,
    onNextStatus: (() -> Unit)?,
    onChat: () -> Unit,
    onDetails: () -> Unit,
    isAr: Boolean = isAppArabic(),
    modifier: Modifier = Modifier,
) {
    val extended = HatliTheme.extendedColors
    val nextCta = when (nextAction) {
        RunnerNextAction.StartWork -> stringResource(R.string.home_runner_start_work)
        RunnerNextAction.MarkPurchased -> stringResource(R.string.home_runner_mark_purchased)
        RunnerNextAction.OnTheWay -> stringResource(R.string.home_runner_on_the_way)
        RunnerNextAction.MarkDelivered -> stringResource(R.string.home_runner_mark_delivered)
        null -> null
    }
    val earningsFormatted = formatEgp(summary.offerAmount, isAr)

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
                    runnerName = null,
                )
            }

            // Requester headline & Job title
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val requesterName = summary.requesterName
                if (requesterName != null) {
                    Text(
                        text = stringResource(R.string.home_runner_for_requester, requesterName),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                        ),
                    )
                }

                Text(
                    text = summary.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Route if available
                val routeText = when {
                    !summary.pickupLabel.isNullOrBlank() && !summary.dropoffLabel.isNullOrBlank() ->
                        "📍 ${summary.pickupLabel} ➔ 🏁 ${summary.dropoffLabel}"
                    !summary.pickupLabel.isNullOrBlank() ->
                        "📍 ${summary.pickupLabel}"
                    !summary.dropoffLabel.isNullOrBlank() ->
                        "🏁 ${summary.dropoffLabel}"
                    else -> null
                }
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

            // Earnings + Distance Summary Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, extended.border),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = "${stringResource(R.string.home_card_delivery_fee)}:",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                            ),
                        )
                        if (earningsFormatted.isNotBlank()) {
                            Text(
                                text = earningsFormatted,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandTeal,
                                    fontSize = 13.sp,
                                ),
                            )
                        }
                    }

                    summary.distanceMeters?.let {
                        Text(
                            text = formatDistance(it, isAr),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        )
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

            if (summary.status == RequestStatus.Delivered) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = BrandTeal.copy(alpha = 0.1f),
                ) {
                    Text(
                        text = stringResource(R.string.home_runner_waiting_confirmation),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = BrandTeal,
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    )
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                HatliSecondaryButton(
                    text = stringResource(R.string.home_runner_chat),
                    onClick = onChat,
                    modifier = Modifier.weight(1f),
                )
                HatliSecondaryButton(
                    text = stringResource(R.string.home_runner_details),
                    onClick = onDetails,
                    modifier = Modifier.weight(1f),
                )
            }

            if (nextCta != null && onNextStatus != null) {
                HatliPrimaryButton(
                    text = nextCta,
                    onClick = onNextStatus,
                )
            }
        }
    }
}
