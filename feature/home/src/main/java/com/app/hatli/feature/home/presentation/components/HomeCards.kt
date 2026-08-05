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
    nextCta: String?,
    onNextStatus: (() -> Unit)?,
    onChat: () -> Unit,
    onDetails: () -> Unit,
    isAr: Boolean,
    modifier: Modifier = Modifier,
) {
    HatliCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onDetails,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            Text(
                text = if (isAr) "وظيفتك الحالية" else "Active Job",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = summary.requesterName?.let {
                    if (isAr) "طلب $it" else "For $it"
                } ?: summary.title,
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
                HatliSecondaryButton(
                    text = if (isAr) "محادثة" else "Chat",
                    onClick = onChat,
                    modifier = Modifier.weight(1f),
                )
                HatliSecondaryButton(
                    text = if (isAr) "التفاصيل" else "Details",
                    onClick = onDetails,
                    modifier = Modifier.weight(1f),
                )
            }
            if (nextCta != null && onNextStatus != null) {
                HatliPrimaryButton(text = nextCta, onClick = onNextStatus)
            }
            if (summary.status == RequestStatus.Delivered) {
                Text(
                    text = if (isAr) {
                        "في انتظار تأكيد صاحب الطلب"
                    } else {
                        "Waiting for requester to confirm"
                    },
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
                text = if (isOnline) "Online" else "Offline",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = if (isOnline) {
                    "You're receiving nearby requests. Your Active Job stays visible either way."
                } else {
                    "Go online to see nearby requests. Accepted jobs still appear under Active Job."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (isOnline) {
                HatliSecondaryButton(text = "Go Offline", onClick = onToggle)
            } else {
                HatliPrimaryButton(text = "Go Online", onClick = onToggle)
            }
        }
    }
}

@Composable
fun RunnerStatsRow(
    earnings: Double,
    jobs: Int,
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        StatChip(
            modifier = Modifier.weight(1f),
            title = "${earnings.toInt()} EGP",
            subtitle = "Today",
        )
        StatChip(
            modifier = Modifier.weight(1f),
            title = "$jobs",
            subtitle = "Jobs",
        )
        StatChip(
            modifier = Modifier.weight(1f),
            title = "$rating ★",
            subtitle = "Rating",
        )
    }
}

@Composable
private fun StatChip(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = HatliSpacing.xs),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}
