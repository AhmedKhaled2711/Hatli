package com.app.hatli.feature.home.presentation.runner.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
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
import com.app.hatli.core.designsystem.component.HatliSwitch
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.feature.home.R

private const val MAX_NOTIFICATION_COUNT = 99
private const val MAX_NOTIFICATION_LABEL = "99+"

private object RunnerHeaderDimensions {
    val NotificationButtonSize = 48.dp
    val NotificationIconSize = 22.dp
    val NotificationElevation = 1.dp
}

@Composable
fun RunnerHeaderRow(
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
fun RunnerNotificationButton(
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
