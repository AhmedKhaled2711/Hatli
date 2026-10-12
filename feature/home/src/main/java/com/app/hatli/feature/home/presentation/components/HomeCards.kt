package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.core.model.requesterLabelAr
import com.app.hatli.core.model.shortLabelEn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.ui.text.style.TextAlign
import com.app.hatli.core.designsystem.theme.AmberPending
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliTheme
import androidx.compose.ui.res.stringResource
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.RunnerNextAction
import java.text.NumberFormat
import java.util.Locale

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

@Composable
fun CurrentRequestCard(
    summary: RequestSummary,
    isAr: Boolean,
    onChat: () -> Unit,
    onDetails: () -> Unit,
    onConfirm: (() -> Unit)? = null,
    onRate: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val statusLabel = if (isAr) {
        summary.status.requesterLabelAr(summary.runnerName)
    } else {
        when (summary.status) {
            RequestStatus.Pending -> "Looking for a Runner"
            RequestStatus.Accepted -> "${summary.runnerName ?: "A Runner"} accepted your request"
            RequestStatus.InProgress -> "Runner is working on it"
            RequestStatus.Purchased -> "Items purchased"
            RequestStatus.OnTheWay -> "On the way to you"
            RequestStatus.Delivered -> "Delivered — confirm receipt"
            RequestStatus.Completed -> if (summary.needsRating) "Completed — rate your Runner" else "Completed"
            RequestStatus.Cancelled -> "Cancelled"
        }
    }
    HatliCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onDetails,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            Text(
                text = if (isAr) "طلبك الحالي" else "Current Request",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = statusLabel,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(summary.title, style = MaterialTheme.typography.bodyLarge)
            RequestStatusStepper(
                status = summary.status,
                category = summary.category,
                isAr = isAr,
            )
            if (summary.unreadCount > 0) {
                Text(
                    text = if (isAr) {
                        "${summary.unreadCount} رسائل جديدة"
                    } else {
                        "${summary.unreadCount} new messages"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                if (summary.canOpenChat) {
                    HatliSecondaryButton(
                        text = if (isAr) "محادثة" else "Chat",
                        onClick = onChat,
                        modifier = Modifier.weight(1f),
                    )
                }
                HatliSecondaryButton(
                    text = if (isAr) "التفاصيل" else "Details",
                    onClick = onDetails,
                    modifier = Modifier.weight(1f),
                )
            }
            if (onConfirm != null) {
                HatliPrimaryButton(
                    text = if (isAr) "تأكيد الاستلام" else "Confirm received",
                    onClick = onConfirm,
                )
            }
            if (onRate != null) {
                HatliPrimaryButton(
                    text = if (isAr) "قيّم الـ Runner" else "Rate Runner",
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
    isAr: Boolean,
    modifier: Modifier = Modifier,
) {
    val nextCta = when (nextAction) {
        RunnerNextAction.StartWork ->
            stringResource(R.string.home_runner_start_work)

        RunnerNextAction.MarkPurchased ->
            stringResource(R.string.home_runner_mark_purchased)

        RunnerNextAction.OnTheWay ->
            stringResource(R.string.home_runner_on_the_way)

        RunnerNextAction.MarkDelivered ->
            stringResource(R.string.home_runner_mark_delivered)

        null -> null
    }

    HatliCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onDetails,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                HatliSpacing.sm,
            ),
        ) {
            Text(
                text = stringResource(
                    R.string.home_runner_current_job,
                ),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = summary.requesterName?.let { requesterName ->
                    stringResource(
                        R.string.home_runner_for_requester,
                        requesterName,
                    )
                } ?: summary.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = summary.title,
                style = MaterialTheme.typography.bodyLarge,
            )

            RequestStatusStepper(
                status = summary.status,
                category = summary.category,
                isAr = isAr,
            )

            if (summary.unreadCount > 0) {
                Text(
                    text = stringResource(
                        R.string.home_runner_new_messages,
                        summary.unreadCount,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    HatliSpacing.sm,
                ),
            ) {
                HatliSecondaryButton(
                    text = stringResource(
                        R.string.home_runner_chat,
                    ),
                    onClick = onChat,
                    modifier = Modifier.weight(1f),
                )

                HatliSecondaryButton(
                    text = stringResource(
                        R.string.home_runner_details,
                    ),
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

            if (summary.status == RequestStatus.Delivered) {
                Text(
                    text = stringResource(
                        R.string.home_runner_waiting_confirmation,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun RequestRowCard(
    summary: RequestSummary,
    isAr: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailingAction: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    HatliCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xxs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = summary.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                if (summary.unreadCount > 0) {
                    Badge { Text("${summary.unreadCount}") }
                }
            }
            Text(
                text = if (isAr) {
                    when (summary.status) {
                        RequestStatus.Pending -> "جاري البحث"
                        else -> summary.status.requesterLabelAr(summary.runnerName)
                    }
                } else {
                    summary.status.shortLabelEn()
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                summary.distanceMeters?.let {
                    Text(
                        text = if (isAr) "$it م" else "$it m",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                summary.offerAmount?.let {
                    Text(
                        text = if (isAr) "${it.toInt()} جنيه" else "${it.toInt()} EGP",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
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
    isAr: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth(), onClick = onClick) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xxs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = summary.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                if (summary.unreadCount > 0) {
                    Badge { Text("${summary.unreadCount}") }
                }
            }
            Text(
                text = summary.lastMessage
                    ?: if (isAr) "ابدأ المحادثة" else "Tap to open chat",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = if (isAr) summary.status.requesterLabelAr(summary.runnerName)
                else summary.status.shortLabelEn(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
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
        val currencyText = if (isAr) "ج.م" else "EGP"
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
