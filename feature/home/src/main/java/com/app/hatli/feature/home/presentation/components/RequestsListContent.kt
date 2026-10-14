package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.ShoppingBag
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
                            RequestListFilter.Active -> Icons.Outlined.ShoppingBag
                            RequestListFilter.Completed -> Icons.Outlined.CheckCircleOutline
                            RequestListFilter.Cancelled -> Icons.Outlined.Cancel
                        },
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    )
                    Text(
                        text = when (filter) {
                            RequestListFilter.Active -> if (isAr) "لا توجد طلبات جارية حالياً" else "No active requests right now"
                            RequestListFilter.Completed -> if (isAr) "لا توجد طلبات مكتملة بعد" else "No completed requests yet"
                            RequestListFilter.Cancelled -> if (isAr) "لا توجد طلبات ملغاة" else "No cancelled requests"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                    if (filter == RequestListFilter.Active) {
                        Spacer(modifier = Modifier.height(HatliSpacing.xs))
                        HatliPrimaryButton(
                            text = stringResource(R.string.home_requests_create),
                            onClick = onCreate,
                        )
                    }
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