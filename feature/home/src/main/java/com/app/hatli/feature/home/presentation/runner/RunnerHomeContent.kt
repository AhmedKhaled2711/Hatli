package com.app.hatli.feature.home.presentation.runner

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PowerOff
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.HatliSwitch
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.card.CurrentJobCard
import com.app.hatli.feature.home.presentation.components.HomeSectionHeader
import com.app.hatli.feature.home.presentation.components.card.RunnerStatsRow
import com.app.hatli.feature.home.presentation.runnerNextAction
import com.app.hatli.feature.home.presentation.runnerNextStatus

private const val MAX_NOTIFICATION_COUNT = 99
private const val MAX_NOTIFICATION_LABEL = "99+"

@Composable
fun RunnerHomeContent(
    snapshot: RunnerHomeSnapshot?,
    isAr: Boolean,
    onToggleOnline: () -> Unit,
    onViewAll: () -> Unit,
    onAccept: (String) -> Unit,
    onChat: (String) -> Unit,
    onDetails: (String) -> Unit,
    onAdvance: (String, RequestStatus) -> Unit,
    onNotifications: () -> Unit = {},
    showOnlineInfoBanner: Boolean = false,
    onDismissOnlineInfoBanner: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    if (snapshot == null) {
        RunnerHomeShimmer(modifier = modifier)
        return
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            contentPadding = PaddingValues(
                start = HatliSpacing.lg,
                end = HatliSpacing.lg,
                top = HatliSpacing.md,
                bottom = 128.dp, // Content scrolls cleanly above floating bottom bar
            ),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
        ) {
            // Localized Runner Dashboard Persona Pill, Availability Toggle & Notifications Header
            item(key = "runner_header") {
                RunnerHeaderRow(
                    isOnline = snapshot.presence.isOnline,
                    unreadCount = snapshot.unreadNotifications,
                    onToggleOnline = onToggleOnline,
                    onNotificationsClick = onNotifications,
                )
            }

            // Compact contextual dismissible online banner (displayed max 6 times lifetime)
            if (showOnlineInfoBanner && snapshot.presence.isOnline) {
                item(key = "runner_online_info_banner") {
                    RunnerOnlineInfoBanner(
                        onDismiss = onDismissOnlineInfoBanner,
                    )
                }
            }

            // Daily Operational Stats Row
            item(key = "stats_row") {
                RunnerStatsRow(
                    earnings = snapshot.stats.dailyEarnings,
                    jobs = snapshot.stats.todayJobsCount,
                    rating = snapshot.stats.rating,
                    isAr = isAr,
                )
            }

            // Active Job Operational Priority Card
            item(key = "active_job_section") {
                AnimatedContent(
                    targetState = snapshot.activeJob,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "ActiveJobTransition",
                ) { active ->
                    if (active != null) {
                        val nextAction = active.runnerNextAction()
                        CurrentJobCard(
                            summary = active,
                            nextAction = nextAction,
                            onNextStatus = active.runnerNextStatus()?.let { status ->
                                { onAdvance(active.id, status) }
                            },
                            onChat = { onChat(active.id) },
                            onDetails = { onDetails(active.id) },
                            isAr = isAr,
                        )
                    } else {
                        RunnerEmptyStateCard(
                            icon = Icons.Outlined.CheckCircle,
                            title = stringResource(R.string.home_runner_no_active_job_title),
                            description = stringResource(R.string.home_runner_no_active_job_sub),
                        )
                    }
                }
            }

            // Nearby Jobs Real-time Stream Section
            when {
                !snapshot.presence.isOnline -> {
                    item(key = "offline_state") {
                        RunnerEmptyStateCard(
                            icon = Icons.Outlined.PowerOff,
                            title = stringResource(R.string.home_runner_offline_title),
                            description = stringResource(R.string.home_runner_offline_sub),
                        )
                    }
                }

                snapshot.nearby.isEmpty() -> {
                    item(key = "no_nearby_state") {
                        RunnerEmptyStateCard(
                            icon = Icons.Outlined.Inbox,
                            title = stringResource(R.string.home_runner_no_nearby_title),
                            description = stringResource(R.string.home_runner_no_nearby_sub),
                        )
                    }
                }

                else -> {
                    item(key = "nearby_header") {
                        HomeSectionHeader(
                            title = stringResource(R.string.home_runner_nearby_title),
                            actionLabel = stringResource(R.string.home_runner_view_all_jobs),
                            onAction = onViewAll,
                        )
                    }

                    items(
                        items = snapshot.nearby.take(5),
                        key = { "job_${it.id}" },
                    ) { job ->
                        PendingJobCard(
                            summary = job,
                            isAr = isAr,
                            onAccept = { onAccept(job.id) },
                            onDetails = { onDetails(job.id) },
                        )
                    }

                    item(key = "view_all_button") {
                        HatliSecondaryButton(
                            text = stringResource(R.string.home_runner_view_all_jobs),
                            onClick = onViewAll,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RunnerEmptyStateCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String?,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            )
            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            }
        }
    }
}


@Composable
private fun RunnerHeaderRow(
    isOnline: Boolean,
    unreadCount: Int,
    onToggleOnline: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr,
    ) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            // Left side: Online / Offline toggle
            RunnerAvailabilityToggle(
                isOnline = isOnline,
                onToggle = onToggleOnline,
                modifier = Modifier.align(Alignment.CenterStart),
            )

            // Right side: Notifications
            RunnerNotificationButton(
                unreadCount = unreadCount,
                onClick = onNotificationsClick,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
    }
}

@Composable
private fun RunnerNotificationButton(
    unreadCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val notificationDescription = stringResource(
        id = R.string.home_notifications,
    )

    val displayedUnreadCount = unreadCount
        .coerceAtLeast(0)
        .coerceAtMost(MAX_NOTIFICATION_COUNT)

    BadgedBox(
        modifier = modifier,
        badge = {
            if (unreadCount > 0) {
                Badge(
                    containerColor = BrandCoral,
                ) {
                    Text(
                        text = if (displayedUnreadCount == MAX_NOTIFICATION_COUNT) {
                            MAX_NOTIFICATION_LABEL
                        } else {
                            displayedUnreadCount.toString()
                        },
                        color = Color.White,
                        maxLines = 1,
                    )
                }
            }
        },
    ) {
        Surface(
            modifier = Modifier.size(
                RunnerHeaderDimensions.NotificationButtonSize,
            ),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = RunnerHeaderDimensions.NotificationElevation,
            shadowElevation = RunnerHeaderDimensions.NotificationElevation,
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.fillMaxSize(),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = notificationDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(
                        RunnerHeaderDimensions.NotificationIconSize,
                    ),
                )
            }
        }
    }
}

private object RunnerHeaderDimensions {
    val NotificationButtonSize = 48.dp
    val NotificationIconSize = 22.dp
    val NotificationElevation = 1.dp
}


@Composable
fun RunnerAvailabilityToggle(
    isOnline: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val onlineLabel = stringResource(R.string.home_runner_status_online)
    val offlineLabel = stringResource(R.string.home_runner_status_offline)
    val actionLabel = if (isOnline) {
        stringResource(R.string.home_runner_action_go_offline)
    } else {
        stringResource(R.string.home_runner_action_go_online)
    }

    val isDark = HatliTheme.isDark
    val containerColor by animateColorAsState(
        targetValue = if (isOnline) {
            if (isDark) Color(0xFF261812) else HatliColors.CoralContainer
        } else {
            if (isDark) MaterialTheme.colorScheme.surfaceVariant else HatliColors.Gray100
        },
        label = "ToggleContainerColor",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isOnline) {
            HatliColors.BrandCoral.copy(alpha = 0.4f)
        } else {
            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        },
        label = "ToggleBorderColor",
    )

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                role = Role.Switch,
                onClickLabel = actionLabel,
                onClick = onToggle,
            )
            .semantics {
                stateDescription = if (isOnline) onlineLabel else offlineLabel
            },
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = if (isOnline) onlineLabel else offlineLabel,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                ),
                color = if (isOnline) {
                    if (isDark) Color(0xFFFF8B5E) else HatliColors.BrandCoral
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = 1,
            )

            HatliSwitch(
                checked = isOnline,
                onCheckedChange = null,
                width = 46.dp,
                height = 26.dp,
                thumbSize = 20.dp,
                trackPadding = 3.dp,
                checkedTrackColor = HatliColors.BrandCoral,
            )
        }
    }
}

@Composable
fun RunnerOnlineInfoBanner(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = HatliSpacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = HatliColors.Success,
                modifier = Modifier.size(20.dp),
            )

            Text(
                text = stringResource(R.string.home_runner_online_desc),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp,
                ),
                modifier = Modifier.weight(1f),
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(28.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.home_runner_dismiss_tip),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

