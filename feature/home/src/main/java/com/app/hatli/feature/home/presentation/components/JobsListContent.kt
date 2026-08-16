package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.feature.home.presentation.JobListFilter
import com.app.hatli.feature.home.presentation.runner.PendingJobCard
import androidx.compose.ui.res.stringResource
import com.app.hatli.feature.home.R

@Composable
fun JobsListContent(
    filter: JobListFilter,
    items: List<RequestSummary>,
    onFilter: (JobListFilter) -> Unit,
    onAccept: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    isAr: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.md),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        ) {
            items(
                items = JobListFilter.entries.toList(),
                key = { it.name },
            ) { filterItem ->
                HatliCard(
                    onClick = {
                        onFilter(filterItem)
                    },
                    selected = filterItem == filter,
                ) {
                    Text(
                        text = when (filterItem) {
                            JobListFilter.Active -> stringResource(R.string.home_jobs_filter_active)
                            JobListFilter.History -> stringResource(R.string.home_jobs_filter_history)
                        },
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        if (items.isEmpty()) {
            Text(
                text = stringResource(R.string.home_jobs_empty),
                style = MaterialTheme.typography.bodyLarge,
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                items(
                    items = items,
                    key = { it.id },
                ) { job ->
                    if (job.status == RequestStatus.Pending) {
                        PendingJobCard(
                            summary = job,
                            isAr = isAr,
                            onAccept = {
                                onAccept(job.id)
                            },
                            onDetails = {
                                onOpenDetail(job.id)
                            },
                        )
                    } else {
                        RequestRowCard(
                            summary = job,
                            isAr = isAr,
                            onClick = {
                                onOpenDetail(job.id)
                            },
                            trailingAction = {
                                if (job.canOpenChat) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(
                                            HatliSpacing.xs,
                                        ),
                                    ) {
                                        HatliSecondaryButton(
                                            text = stringResource(R.string.home_jobs_chat),
                                            onClick = {
                                                onOpenChat(job.id)
                                            },
                                        )
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}
