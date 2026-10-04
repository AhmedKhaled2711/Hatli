package com.app.hatli.feature.home.presentation.runner

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.PowerOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.CurrentJobCard
import com.app.hatli.feature.home.presentation.components.HomeSectionHeader
import com.app.hatli.feature.home.presentation.components.RunnerPresenceCard
import com.app.hatli.feature.home.presentation.components.RunnerStatsRow
import com.app.hatli.feature.home.presentation.runnerNextAction
import com.app.hatli.feature.home.presentation.runnerNextStatus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.BrandCoral

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
    modifier: Modifier = Modifier,
) {
    if (snapshot == null) {
        RunnerHomeSkeleton(modifier = modifier)
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
            // Localized Runner Dashboard Persona Pill & Notifications Header
            item(key = "runner_header") {
                RunnerHeaderRow(
                    unreadCount = snapshot.unreadNotifications,
                    onNotificationsClick = onNotifications,
                )
            }

            // Online / Offline Duty Presence Banner Card
            item(key = "presence_card") {
                RunnerPresenceCard(
                    isOnline = snapshot.presence.isOnline,
                    onToggle = onToggleOnline,
                )
            }

            // Daily Operational Stats Row
            item(key = "stats_row") {
                RunnerStatsRow(
                    earnings = snapshot.stats.dailyEarnings,
                    jobs = snapshot.stats.todayJobsCount,
                    rating = snapshot.stats.rating,
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
private fun RunnerHomeSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
    ) {
        HatliCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(32.dp)) {}
        }
    }
}

@Composable
private fun RunnerHeaderRow(
    unreadCount: Int,
    onNotificationsClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Captain Dashboard Persona Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsBike,
                    contentDescription = null,
                    tint = BrandCoral,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = stringResource(R.string.home_runner_dashboard_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                    ),
                )
            }
        }

        // Notification Bell Badge Button
        BadgedBox(
            badge = {
                if (unreadCount > 0) {
                    Badge(containerColor = BrandCoral) {
                        Text(
                            text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                            color = Color.White,
                        )
                    }
                }
            },
        ) {
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(R.string.home_notifications),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

