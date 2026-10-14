package com.app.hatli.feature.home.presentation.requester

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.HomeSectionHeader
import com.app.hatli.feature.home.presentation.components.card.CurrentRequestCard
import com.app.hatli.feature.home.presentation.components.card.RequestRowCard
import com.app.hatli.feature.home.presentation.requester.components.HeroCreateBanner
import com.app.hatli.feature.home.presentation.requester.components.LocationAndNotificationHeader
import com.app.hatli.feature.home.presentation.requester.components.QuickServiceCategoriesSection
import com.app.hatli.feature.home.presentation.requester.components.RequesterTipCard
import com.app.hatli.feature.home.presentation.requester.components.rememberQuickServiceCategories

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
        RequesterHomeShimmer(modifier = modifier)
        return
    }

    val quickServiceCategories = rememberQuickServiceCategories()
    val displayLocation = if (snapshot.neighborhood.isNotBlank()) {
        "📍 ${snapshot.neighborhood}"
    } else {
        stringResource(R.string.home_location_select_prompt)
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
                bottom = 128.dp, // Content scrolls cleanly above bottom navigation bar
            ),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
        ) {
            // Localized Neighborhood Dropdown & Notifications Header
            item(key = "requester_location_header") {
                LocationAndNotificationHeader(
                    locationName = displayLocation,
                    unreadCount = snapshot.unreadNotifications,
                    onNotificationsClick = { /* Handle notification click */ },
                )
            }

            // Hero Action Banner (High-Impact Custom Errand Callout)
            item(key = "hero_create_card") {
                HeroCreateBanner(onCreate = onCreate)
            }

            // Quick Service Categories Carousel
            item(key = "quick_service_categories") {
                QuickServiceCategoriesSection(
                    categories = quickServiceCategories,
                    onQuickAction = onQuickAction,
                )
            }

            // Active Request Preview Card (Top Operational Priority)
            snapshot.active?.let { active ->
                item(key = "active_request_header") {
                    HomeSectionHeader(
                        title = stringResource(R.string.home_requester_current_request),
                    )
                }

                item(key = "active_request_card") {
                    AnimatedContent(
                        targetState = active,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "ActiveRequestTransition",
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

            // Recent Requests Section
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

            // Informational Tip Card
            val tip = if (isAr) snapshot.tipAr else snapshot.tipEn
            if (tip.isNotBlank()) {
                item(key = "tip_card") {
                    RequesterTipCard(tipText = tip)
                }
            }
        }
    }
}
