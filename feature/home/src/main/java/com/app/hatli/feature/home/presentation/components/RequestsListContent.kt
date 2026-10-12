package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.RequestListFilter
import com.app.hatli.feature.home.presentation.components.card.RequestRowCard
import com.app.hatli.feature.home.presentation.components.formatting.isAppArabic

@Composable
fun RequestsListContent(
    filter: RequestListFilter,
    items: List<RequestSummary>,
    onFilter: (RequestListFilter) -> Unit,
    onCreate: () -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onRate: (String) -> Unit,
    isAr: Boolean = isAppArabic(),
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
                items = RequestListFilter.entries.toList(),
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
                            RequestListFilter.Active ->
                                stringResource(
                                    R.string.home_requests_filter_active,
                                )

                            RequestListFilter.Completed ->
                                stringResource(
                                    R.string.home_requests_filter_completed,
                                )

                            RequestListFilter.Cancelled ->
                                stringResource(
                                    R.string.home_requests_filter_cancelled,
                                )
                        },
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        if (items.isEmpty()) {
            Text(
                text = stringResource(
                    R.string.home_requests_empty,
                ),
                style = MaterialTheme.typography.bodyLarge,
            )

            HatliPrimaryButton(
                text = stringResource(
                    R.string.home_requests_create,
                ),
                onClick = onCreate,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 128.dp),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                items(
                    items = items,
                    key = { it.id },
                ) { request ->

                    RequestRowCard(
                        summary = request,
                        isAr = isAr,
                        onClick = {
                            onOpenDetail(request.id)
                        },
                        trailingAction = {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(
                                    HatliSpacing.xs,
                                ),
                            ) {
                                if (request.canOpenChat) {
                                    HatliSecondaryButton(
                                        text = stringResource(
                                            R.string.home_requests_chat,
                                        ),
                                        onClick = {
                                            onOpenChat(request.id)
                                        },
                                    )
                                }

                                if (request.needsRating) {
                                    HatliPrimaryButton(
                                        text = stringResource(
                                            R.string.home_requests_rate,
                                        ),
                                        onClick = {
                                            onRate(request.id)
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