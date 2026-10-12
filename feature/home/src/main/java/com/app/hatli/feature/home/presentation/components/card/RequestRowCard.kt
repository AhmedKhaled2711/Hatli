package com.app.hatli.feature.home.presentation.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.feature.home.presentation.components.badge.CategoryBadge
import com.app.hatli.feature.home.presentation.components.badge.StatusBadge
import com.app.hatli.feature.home.presentation.components.formatting.isAppArabic
import com.app.hatli.feature.home.presentation.components.formatting.formatDistance
import com.app.hatli.feature.home.presentation.components.formatting.formatEgp
import com.app.hatli.feature.home.presentation.components.formatting.formatRelativeTime

@Composable
fun RequestRowCard(
    summary: RequestSummary,
    isAr: Boolean = isAppArabic(),
    onClick: (() -> Unit)? = null,
    trailingAction: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val extended = HatliTheme.extendedColors
    val feeFormatted = formatEgp(summary.offerAmount, isAr)
    val distanceFormatted = summary.distanceMeters?.let { formatDistance(it, isAr) }
    val timeFormatted = summary.createdAtEpochMs?.let { formatRelativeTime(it, isAr) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.sm,
        shadowElevation = HatliElevation.sm,
        border = BorderStroke(1.dp, extended.border),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Top Row: Category badge + Status badge + Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    CategoryBadge(category = summary.category, isAr = isAr)
                    if (!timeFormatted.isNullOrBlank()) {
                        Text(
                            text = timeFormatted,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                            ),
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    if (summary.unreadCount > 0) {
                        Badge { Text("${summary.unreadCount}") }
                    }
                    StatusBadge(
                        status = summary.status,
                        isAr = isAr,
                        runnerName = summary.runnerName,
                    )
                }
            }

            // Title & Route
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = summary.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                val routeText = when {
                    !summary.pickupLabel.isNullOrBlank() && !summary.dropoffLabel.isNullOrBlank() ->
                        "📍 ${summary.pickupLabel} ➔ 🏁 ${summary.dropoffLabel}"
                    !summary.pickupLabel.isNullOrBlank() ->
                        "📍 ${summary.pickupLabel}"
                    !summary.dropoffLabel.isNullOrBlank() ->
                        "🏁 ${summary.dropoffLabel}"
                    !summary.description.isNullOrBlank() ->
                        summary.description
                    else -> null
                }
                if (routeText != null) {
                    Text(
                        text = routeText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Bottom Meta Row: Distance / Person + Fee
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (distanceFormatted != null) {
                        Text(
                            text = distanceFormatted,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                            ),
                        )
                    }
                    val partnerName = summary.runnerName ?: summary.requesterName
                    if (partnerName != null) {
                        Text(
                            text = partnerName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
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

            trailingAction?.invoke()
        }
    }
}
