package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.AmberPending
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.RunnerNextAction
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import java.text.NumberFormat
import java.util.Locale

@Composable
fun isAppArabic(): Boolean =
    LocalLayoutDirection.current == LayoutDirection.Rtl || Locale.getDefault().language == "ar"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String,
    unread: Int,
    onNotifications: () -> Unit,
    showNotifications: Boolean = false,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        actions = {
            if (showNotifications) {
                IconButton(onClick = onNotifications) {
                    BadgedBox(
                        badge = {
                            if (unread > 0) {
                                Badge { Text("$unread") }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                        )
                    }
                }
            }
        },
        expandedHeight = 56.dp,
        windowInsets = TopAppBarDefaults.windowInsets,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Composable
fun HeroCreateCard(
    title: String,
    cta: String,
    onCreate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            HatliPrimaryButton(text = cta, onClick = onCreate)
        }
    }
}

// --- Reusable Card Formatters & Badges ---

@Composable
fun formatRelativeTime(epochMs: Long, isAr: Boolean = false): String {
    if (epochMs <= 0L) return ""
    val diffMs = System.currentTimeMillis() - epochMs
    if (diffMs < 0L) return stringResource(R.string.home_card_time_just_now)
    val minutes = diffMs / 60_000L
    val hours = diffMs / 3_600_000L
    val days = diffMs / 86_400_000L
    return when {
        minutes < 1 -> stringResource(R.string.home_card_time_just_now)
        minutes < 60 -> stringResource(R.string.home_card_time_mins_ago, minutes.toInt())
        hours < 24 -> stringResource(R.string.home_card_time_hours_ago, hours.toInt())
        days < 30 -> stringResource(R.string.home_card_time_days_ago, days.toInt())
        else -> stringResource(R.string.home_card_time_more_than_month)
    }
}

@Composable
fun formatEgp(amount: Double?, isAr: Boolean = false): String {
    if (amount == null) return ""
    val formatted = NumberFormat.getIntegerInstance(Locale.US).format(amount.toInt())
    return "$formatted ${stringResource(R.string.home_card_currency_egp)}"
}

@Composable
fun formatDistance(meters: Int?, isAr: Boolean = false): String {
    if (meters == null) return ""
    return if (meters >= 1000) {
        val km = String.format(Locale.US, "%.1f", meters / 1000.0)
        stringResource(R.string.home_card_distance_km, km)
    } else {
        stringResource(R.string.home_card_distance_m, meters)
    }
}

@Composable
fun CategoryBadge(
    category: RequestCategory?,
    isAr: Boolean = isAppArabic(),
    modifier: Modifier = Modifier,
) {
    val (labelRes, icon, color) = when (category) {
        RequestCategory.Shopping -> Triple(
            R.string.home_category_badge_shopping,
            Icons.Default.ShoppingBag,
            BrandCoral,
        )
        RequestCategory.Pickup -> Triple(
            R.string.home_category_badge_pickup,
            Icons.Default.LocalShipping,
            BrandTeal,
        )
        RequestCategory.Bills -> Triple(
            R.string.home_category_badge_bills,
            Icons.Default.Receipt,
            Color(0xFF7B1FA2),
        )
        RequestCategory.Custom -> Triple(
            R.string.home_category_badge_custom,
            Icons.Default.MoreHoriz,
            AmberPending,
        )
        null -> Triple(
            R.string.home_category_badge_general,
            Icons.Default.ShoppingBag,
            MaterialTheme.colorScheme.primary,
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.25f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(13.dp),
            )
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                ),
                color = color,
            )
        }
    }
}

@Composable
fun StatusBadge(
    status: RequestStatus,
    isAr: Boolean = isAppArabic(),
    runnerName: String? = null,
    modifier: Modifier = Modifier,
) {
    val (bgColor, contentColor, labelRes) = when (status) {
        RequestStatus.Pending -> Triple(
            AmberPending.copy(alpha = 0.14f),
            AmberPending,
            R.string.home_status_pending,
        )
        RequestStatus.Accepted -> Triple(
            BrandCoral.copy(alpha = 0.12f),
            BrandCoral,
            R.string.home_status_accepted,
        )
        RequestStatus.InProgress -> Triple(
            BrandCoral.copy(alpha = 0.14f),
            BrandCoral,
            R.string.home_status_in_progress,
        )
        RequestStatus.Purchased -> Triple(
            Color(0xFF7B1FA2).copy(alpha = 0.12f),
            Color(0xFF7B1FA2),
            R.string.home_status_purchased,
        )
        RequestStatus.OnTheWay -> Triple(
            BrandTeal.copy(alpha = 0.14f),
            BrandTeal,
            R.string.home_status_on_the_way,
        )
        RequestStatus.Delivered -> Triple(
            Color(0xFF2E7D32).copy(alpha = 0.14f),
            Color(0xFF2E7D32),
            R.string.home_status_delivered,
        )
        RequestStatus.Completed -> Triple(
            Color(0xFF2E7D32).copy(alpha = 0.12f),
            Color(0xFF2E7D32),
            R.string.home_status_completed,
        )
        RequestStatus.Cancelled -> Triple(
            MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
            MaterialTheme.colorScheme.error,
            R.string.home_status_cancelled,
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        border = BorderStroke(0.5.dp, contentColor.copy(alpha = 0.3f)),
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
            ),
            color = contentColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

// --- Requester Active Request Card ---

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
                isAr = isAr,
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
                isAr = isAr,
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

@Composable
fun ConversationRowCard(
    summary: RequestSummary,
    isAr: Boolean = isAppArabic(),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = HatliTheme.extendedColors
    val timeFormatted = summary.createdAtEpochMs?.let { formatRelativeTime(it, isAr) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.sm,
        shadowElevation = HatliElevation.sm,
        border = BorderStroke(1.dp, extended.border),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HatliSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(BrandTeal.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = BrandTeal,
                    modifier = Modifier.size(24.dp),
                )
            }

            // Main Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = summary.runnerName ?: summary.requesterName ?: summary.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
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

                Text(
                    text = summary.lastMessage ?: stringResource(R.string.home_card_tap_to_chat),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = summary.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    StatusBadge(
                        status = summary.status,
                        isAr = isAr,
                        runnerName = summary.runnerName,
                    )
                }
            }

            if (summary.unreadCount > 0) {
                Badge { Text("${summary.unreadCount}") }
            }
        }
    }
}

@Composable
fun RunnerPresenceCard(
    isOnline: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)) {
            Text(
                text = if (isOnline) stringResource(R.string.home_runner_status_online) else stringResource(R.string.home_runner_status_offline),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = if (isOnline) {
                    stringResource(R.string.home_runner_online_desc)
                } else {
                    stringResource(R.string.home_runner_offline_desc)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (isOnline) {
                HatliSecondaryButton(text = stringResource(R.string.home_runner_action_go_offline), onClick = onToggle)
            } else {
                HatliPrimaryButton(text = stringResource(R.string.home_runner_action_go_online), onClick = onToggle)
            }
        }
    }
}

@Composable
fun RunnerStatsRow(
    earnings: Double,
    jobs: Int,
    rating: Double,
    isAr: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        val amountFormatted = NumberFormat.getIntegerInstance(Locale.US).format(earnings.toInt())
        val currencyText = stringResource(R.string.home_card_currency_egp)
        val ratingFormatted = if (rating > 0.0) {
            String.format(Locale.US, "%.1f", rating)
        } else {
            "—"
        }

        // Today Earnings Card (given 1.25f weight so financial value has sufficient breathing room)
        StatCardContainer(
            modifier = Modifier.weight(1.25f),
            label = stringResource(R.string.home_runner_stat_today),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally),
            ) {
                Text(
                    text = amountFormatted,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )
                Text(
                    text = currencyText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }

        // Jobs Card
        StatCardContainer(
            modifier = Modifier.weight(0.85f),
            label = stringResource(R.string.home_runner_stat_jobs),
        ) {
            Text(
                text = jobs.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
        }

        // Rating Card
        StatCardContainer(
            modifier = Modifier.weight(0.9f),
            label = stringResource(R.string.home_runner_stat_rating),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally),
            ) {
                Text(
                    text = ratingFormatted,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = AmberPending,
                    modifier = Modifier.size(13.dp),
                )
            }
        }
    }
}

@Composable
private fun StatCardContainer(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val extended = HatliTheme.extendedColors
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.sm,
        shadowElevation = HatliElevation.sm,
        border = BorderStroke(1.dp, extended.border),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(3.dp))
            content()
        }
    }
}
