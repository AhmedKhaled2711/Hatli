package com.app.hatli.feature.home.presentation.runner

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.PowerOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.HomeSectionHeader
import com.app.hatli.feature.home.presentation.components.card.CurrentJobCard
import com.app.hatli.feature.home.presentation.components.card.RunnerStatsRow
import com.app.hatli.feature.home.presentation.runner.components.RunnerEmptyStateCard
import com.app.hatli.feature.home.presentation.runner.components.RunnerHeaderRow
import com.app.hatli.feature.home.presentation.runner.components.RunnerOnlineInfoBanner
import com.app.hatli.feature.home.presentation.runnerNextAction
import com.app.hatli.feature.home.presentation.runnerNextStatus

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
