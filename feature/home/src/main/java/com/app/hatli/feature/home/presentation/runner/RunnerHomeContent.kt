package com.app.hatli.feature.home.presentation.components.runner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.CurrentJobCard
import com.app.hatli.feature.home.presentation.components.RunnerPresenceCard
import com.app.hatli.feature.home.presentation.components.RunnerStatsRow
import com.app.hatli.feature.home.presentation.runner.PendingJobCard
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
) {
    if (snapshot == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
    ) {
        item {
            RunnerPresenceCard(
                isOnline = snapshot.presence.isOnline,
                onToggle = onToggleOnline,
            )
        }

        item {
            RunnerStatsRow(
                earnings = snapshot.stats.dailyEarnings,
                jobs = snapshot.stats.todayJobsCount,
                rating = snapshot.stats.rating,
            )
        }

        snapshot.activeJob?.let { active ->
            val nextAction = active.runnerNextAction()

            item {
                CurrentJobCard(
                    summary = active,
                    nextAction = nextAction,
                    onNextStatus = active.runnerNextStatus()?.let { status ->
                        {
                            onAdvance(active.id, status)
                        }
                    },
                    onChat = {
                        onChat(active.id)
                    },
                    onDetails = {
                        onDetails(active.id)
                    },
                    isAr = isAr,
                )
            }
        } ?: item {
            HatliCard {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        HatliSpacing.xs,
                    ),
                ) {
                    Text(
                        text = stringResource(
                            R.string.home_runner_no_active_job,
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Text(
                        text = stringResource(
                            R.string.home_runner_no_active_job_description,
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        when {
            !snapshot.presence.isOnline -> {
                item {
                    Text(
                        text = stringResource(
                            R.string.home_runner_go_online,
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            snapshot.nearby.isEmpty() -> {
                item {
                    Text(
                        text = stringResource(
                            R.string.home_runner_no_nearby_requests,
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

            else -> {
                item {
                    Text(
                        text = stringResource(
                            R.string.home_runner_nearby_requests,
                        ),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                items(
                    items = snapshot.nearby.take(3),
                    key = { it.id },
                ) { job ->
                    PendingJobCard(
                        summary = job,
                        isAr = isAr,
                        onAccept = {
                            onAccept(job.id)
                        },
                        onDetails = {
                            onDetails(job.id)
                        },
                    )
                }

                item {
                    HatliSecondaryButton(
                        text = stringResource(
                            R.string.home_runner_view_all_jobs,
                        ),
                        onClick = onViewAll,
                    )
                }
            }
        }
    }
}