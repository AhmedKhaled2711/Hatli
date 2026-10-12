package com.app.hatli.feature.home.presentation.runner

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
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.badge.CategoryBadge
import com.app.hatli.feature.home.presentation.components.formatting.isAppArabic
import com.app.hatli.feature.home.presentation.components.formatting.formatDistance
import com.app.hatli.feature.home.presentation.components.formatting.formatEgp
import com.app.hatli.feature.home.presentation.components.formatting.formatRelativeTime
import com.app.hatli.feature.home.presentation.components.formatting.formatRoute

@Composable
fun PendingJobCard(
    summary: RequestSummary,
    isAr: Boolean = isAppArabic(),
    onAccept: () -> Unit,
    onDetails: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = HatliTheme.extendedColors
    val earningsFormatted = formatEgp(summary.offerAmount, isAr)
    val distanceFormatted = summary.distanceMeters?.let { formatDistance(it, isAr) }
    val timeFormatted = formatRelativeTime(summary.createdAtEpochMs, isAr)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onDetails),
        shape = RoundedCornerShape(18.dp),
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
            // Header: Category Pill + NEW/Time Badge + Prominent Earnings Badge
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

                    // NEW badge or relative time
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = BrandTeal.copy(alpha = 0.12f),
                    ) {
                        Text(
                            text = timeFormatted,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = BrandTeal,
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        )
                    }
                }

                // Prominent Earnings Badge (+35 EGP)
                if (earningsFormatted.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BrandTeal.copy(alpha = 0.14f),
                        border = BorderStroke(1.dp, BrandTeal.copy(alpha = 0.35f)),
                    ) {
                        Text(
                            text = "+$earningsFormatted",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BrandTeal,
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }
            }

            // Job Title & Details
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = summary.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Route if available
                val routeText = formatRoute(
                    pickupLabel = summary.pickupLabel,
                    dropoffLabel = summary.dropoffLabel,
                    description = summary.description,
                    isAr = isAr,
                )
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

            // Requester name & Distance indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val requesterName = summary.requesterName
                if (requesterName != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp),
                            )
                        }
                        Text(
                            text = requesterName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                            ),
                        )
                    }
                }

                if (distanceFormatted != null) {
                    Text(
                        text = distanceFormatted,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                        ),
                    )
                }
            }

            // Side-by-side action buttons: Details (secondary) + Accept Job (primary)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HatliSecondaryButton(
                    text = stringResource(R.string.home_card_details),
                    onClick = onDetails,
                    modifier = Modifier.weight(1f),
                )

                HatliPrimaryButton(
                    text = stringResource(R.string.home_card_accept_job),
                    onClick = onAccept,
                    modifier = Modifier.weight(1.3f),
                )
            }
        }
    }
}