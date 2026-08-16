package com.app.hatli.feature.home.presentation.requester

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
import androidx.compose.ui.res.stringResource
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.CurrentRequestCard
import com.app.hatli.feature.home.presentation.components.HeroCreateCard
import com.app.hatli.feature.home.presentation.components.HomeHeader
import com.app.hatli.feature.home.presentation.components.HomeQuickActions
import com.app.hatli.feature.home.presentation.components.HomeSectionHeader
import com.app.hatli.feature.home.presentation.components.HomeStatItem
import com.app.hatli.feature.home.presentation.components.HomeStatsCard
import com.app.hatli.feature.home.presentation.components.QuickActionChip
import com.app.hatli.feature.home.presentation.components.RequestRowCard

@Composable
fun RequesterHomeContent(
    snapshot: RequesterHomeSnapshot?,
    isAr: Boolean,
    onCreate: () -> Unit,
    onQuickAction: (String) -> Unit,
    onChat: (String) -> Unit,
    onDetails: (String) -> Unit,
    onConfirm: (String) -> Unit,
    onRate: (String) -> Unit,
    onOpenRequests: () -> Unit,
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
        contentPadding = PaddingValues(
            horizontal = HatliSpacing.lg,
            vertical = HatliSpacing.md,
        ),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
    ) {

        item {
            HeroCreateCard(
                title = stringResource(
                    R.string.home_requester_hero_title,
                ),
                cta = stringResource(
                    R.string.home_requester_hero_cta,
                ),
                onCreate = onCreate,
            )
        }

        item {
            HomeStatsCard(
                firstStat = HomeStatItem(
                    title = stringResource(
                        R.string.home_requester_completed_requests,
                    ),
                    value = snapshot.recent.count {
                        it.status == RequestStatus.Delivered
                    },
                ),
                secondStat = HomeStatItem(
                    title = stringResource(
                        R.string.home_requester_total_requests,
                    ),
                    value = snapshot.recent.size,
                ),
                isAr = isAr,
            )
        }

        snapshot.active?.let { active ->
            item {
                HomeSectionHeader(
                    title = stringResource(
                        R.string.home_requester_current_request,
                    ),
                )
            }

            item {
                CurrentRequestCard(
                    summary = active,
                    isAr = isAr,
                    onChat = {
                        onChat(active.id)
                    },
                    onDetails = {
                        onDetails(active.id)
                    },
                    onConfirm = if (active.status == RequestStatus.Delivered) {
                        {
                            onConfirm(active.id)
                        }
                    } else {
                        null
                    },
                    onRate = if (active.needsRating) {
                        {
                            onRate(active.id)
                        }
                    } else {
                        null
                    },
                )
            }
        }

        if (snapshot.recent.isNotEmpty()) {
            item {
                HomeSectionHeader(
                    title = stringResource(R.string.home_recent_requests),
                    actionLabel = stringResource(
                        R.string.home_requester_view_all,
                    ),
                    onAction = onOpenRequests,
                )
            }

            items(
                items = snapshot.recent.take(5),
                key = { it.id },
            ) { request ->
                RequestRowCard(
                    summary = request,
                    isAr = isAr,
                    onClick = {
                        onDetails(request.id)
                    },
                )
            }
        }

        item {
            HomeSectionHeader(
                title = stringResource(
                    R.string.home_requester_quick_actions,
                ),
            )
        }

        item {
            HomeQuickActions(
                onCreateRequest = onCreate,
                onOpenRequests = onOpenRequests,
            )
        }

        if (snapshot.quickActions.isNotEmpty()) {
            item {
                HomeSectionHeader(
                    title = stringResource(
                        R.string.home_requester_services,
                    ),
                )
            }

            items(
                items = snapshot.quickActions,
                key = { it.id },
            ) { action ->
                QuickActionChip(
                    action = action,
                    isAr = isAr,
                    onClick = {
                        onQuickAction(action.id)
                    },
                )
            }
        }

        val tip = if (isAr) {
            snapshot.tipAr
        } else {
            snapshot.tipEn
        }

        if (tip.isNotBlank()) {
            item {
                HatliCard {
                    Text(
                        text = stringResource(
                            R.string.home_tip,
                        ),
                        style = MaterialTheme.typography.titleSmall,
                    )

                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}