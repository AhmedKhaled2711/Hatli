package com.app.hatli.feature.home.presentation.requester

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.CurrentRequestCard
import com.app.hatli.feature.home.presentation.components.HeroCreateCard
import com.app.hatli.feature.home.presentation.components.HomeQuickActions
import com.app.hatli.feature.home.presentation.components.HomeSectionHeader
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
    modifier: Modifier = Modifier,
) {
    if (snapshot == null) {
        RequesterHomeSkeleton(modifier = modifier)
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .animateContentSize(),
        contentPadding = PaddingValues(
            horizontal = HatliSpacing.lg,
            vertical = HatliSpacing.md,
        ),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
    ) {
        // Hero Section - Primary Action Callout
        item(key = "hero_create_card") {
            HeroCreateCard(
                title = stringResource(R.string.home_requester_hero_title),
                cta = stringResource(R.string.home_requester_hero_cta),
                onCreate = onCreate,
            )
        }

        // Active Request Section (Top Operational Priority)
        snapshot.active?.let { active ->
            item(key = "active_request_header") {
                HomeSectionHeader(
                    title = stringResource(R.string.home_requester_current_request)
                )
            }

            item(key = "active_request_card") {
                AnimatedContent(
                    targetState = active,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "ActiveRequestTransition"
                ) { currentActive ->
                    CurrentRequestCard(
                        summary = currentActive,
                        isAr = isAr,
                        onChat = { onChat(currentActive.id) },
                        onDetails = { onDetails(currentActive.id) },
                        onConfirm = if (currentActive.status == RequestStatus.Delivered) {
                            { onConfirm(currentActive.id) }
                        } else {
                            null
                        },
                        onRate = if (currentActive.needsRating) {
                            { onRate(currentActive.id) }
                        } else {
                            null
                        },
                    )
                }
            }
        }

        // Recent Requests History Section
        if (snapshot.recent.isNotEmpty()) {
            item(key = "recent_requests_header") {
                HomeSectionHeader(
                    title = stringResource(R.string.home_recent_requests),
                    actionLabel = stringResource(R.string.home_requester_view_all),
                    onAction = onOpenRequests,
                )
            }

            items(
                items = snapshot.recent.take(5),
                key = { "recent_${it.id}" },
            ) { request ->
                RequestRowCard(
                    summary = request,
                    isAr = isAr,
                    onClick = { onDetails(request.id) },
                )
            }
        }

        // Quick Actions Shortcut Area
        item(key = "quick_actions_header") {
            HomeSectionHeader(
                title = stringResource(R.string.home_requester_quick_actions)
            )
        }

        item(key = "quick_actions_component") {
            HomeQuickActions(
                onCreateRequest = onCreate,
                onOpenRequests = onOpenRequests,
            )
        }

        // Services & Capabilities Discovery Section
        if (snapshot.quickActions.isNotEmpty()) {
            item(key = "services_header") {
                HomeSectionHeader(
                    title = stringResource(R.string.home_requester_services)
                )
            }

            items(
                items = snapshot.quickActions,
                key = { "service_${it.id}" },
            ) { action ->
                QuickActionChip(
                    action = action,
                    isAr = isAr,
                    onClick = { onQuickAction(action.id) },
                )
            }
        }

        // Informational Tips Card
        val tip = if (isAr) snapshot.tipAr else snapshot.tipEn

        if (tip.isNotBlank()) {
            item(key = "tip_card") {
                RequesterTipCard(tipText = tip)
            }
        }
    }
}

/**
 * Polished, lightweight Tip Card to highlight contextual user guidance.
 */
@Composable
private fun RequesterTipCard(
    tipText: String,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(HatliSpacing.sm),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(HatliSpacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.home_tip),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = tipText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Smooth Skeleton Loader reflecting actual Requester Home visual layout.
 */
@Composable
private fun RequesterHomeSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = HatliSpacing.lg, vertical = HatliSpacing.md),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg)
    ) {
        // Hero Card Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        )

        // Section Title Placeholder
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        )

        // Active Card Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        )
    }
}