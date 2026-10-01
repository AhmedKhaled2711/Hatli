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
    modifier: Modifier = Modifier,
) {
    if (snapshot == null) {
        RunnerHomeSkeleton(modifier = modifier)
        return
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = OffWhiteCanvas,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            contentPadding = PaddingValues(
                start = HatliSpacing.lg,
                end = HatliSpacing.lg,
                top = HatliSpacing.md,
                bottom = 110.dp, // Content scrolls cleanly above floating bottom bar
            ),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
        ) {
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
                            title = "لا يوجود طلب قيد التنفيذ حالياً",
                            description = "تصفّح الطلبات القريبة أدناه وقدم عرض التوصيل الآن.",
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
                            title = "أنت غير متاح لـ استلام الطلبات",
                            description = "قم بتفعيل مفتاح التواجد بالعلّى للبدء في استقبال طلبات التوصيل.",
                        )
                    }
                }

                snapshot.nearby.isEmpty() -> {
                    item(key = "no_nearby_state") {
                        RunnerEmptyStateCard(
                            icon = Icons.Outlined.Inbox,
                            title = "لا توجد طلبات قريبة حالياً",
                            description = "سنخطر فور إدراج طلب توصيل جديد في منطقتك الجغرافية.",
                        )
                    }
                }

                else -> {
                    item(key = "nearby_header") {
                        HomeSectionHeader(
                            title = "الطلبات القريبة المتاحة",
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
                    color = CharcoalDark,
                ),
            )
            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CharcoalMuted,
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

