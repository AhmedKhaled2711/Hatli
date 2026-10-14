package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.feature.home.presentation.JobListFilter
import com.app.hatli.feature.home.presentation.components.card.RequestRowCard
import com.app.hatli.feature.home.presentation.runner.PendingJobCard
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.formatting.isAppArabic

@Composable
fun JobsListContent(
    filter: JobListFilter,
    items: List<RequestSummary>,
    onFilter: (JobListFilter) -> Unit,
    onAccept: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenChat: (String) -> Unit,
) {
    val isAr = isAppArabic()
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(HatliSpacing.xl),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
                ) {
                    Icon(
                        imageVector = when (filter) {
                            JobListFilter.Active -> Icons.Outlined.TwoWheeler
                            JobListFilter.History -> Icons.Outlined.History
                        },
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    )
                    Text(
                        text = when (filter) {
                            JobListFilter.Active -> if (isAr) "لا توجد مهام نشطة حالياً" else "No active jobs right now"
                            JobListFilter.History -> if (isAr) "لا يوجد سجل مهام سابق" else "No job history yet"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 128.dp),
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
