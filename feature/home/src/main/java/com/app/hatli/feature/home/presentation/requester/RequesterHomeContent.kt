package com.app.hatli.feature.home.presentation.requester

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.card.CurrentRequestCard
import com.app.hatli.feature.home.presentation.components.HomeSectionHeader
import com.app.hatli.feature.home.presentation.components.card.RequestRowCard

private data class QuickServiceCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val containerColor: Color,
    val iconColor: Color,
)

@Composable
private fun rememberQuickServiceCategories(): List<QuickServiceCategory> {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val primary = MaterialTheme.colorScheme.primary
    val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    val tertiary = MaterialTheme.colorScheme.tertiary

    val groceryTitle = stringResource(R.string.home_category_grocery_title)
    val grocerySub = stringResource(R.string.home_category_grocery_subtitle)
    val pharmacyTitle = stringResource(R.string.home_category_pharmacy_title)
    val pharmacySub = stringResource(R.string.home_category_pharmacy_subtitle)
    val courierTitle = stringResource(R.string.home_category_courier_title)
    val courierSub = stringResource(R.string.home_category_courier_subtitle)
    val billsTitle = stringResource(R.string.home_category_bills_title)
    val billsSub = stringResource(R.string.home_category_bills_subtitle)
    val customTitle = stringResource(R.string.home_category_custom_title)
    val customSub = stringResource(R.string.home_category_custom_subtitle)

    return androidx.compose.runtime.remember(
        primaryContainer, primary, secondaryContainer, secondary, tertiaryContainer, tertiary,
        groceryTitle, grocerySub, pharmacyTitle, pharmacySub,
        courierTitle, courierSub, billsTitle, billsSub, customTitle, customSub,
    ) {
        listOf(
            QuickServiceCategory(
                id = "grocery",
                title = groceryTitle,
                subtitle = grocerySub,
                icon = Icons.Default.ShoppingBag,
                containerColor = CoralContainer,
                iconColor = BrandCoral,
            ),
            QuickServiceCategory(
                id = "pharmacy",
                title = pharmacyTitle,
                subtitle = pharmacySub,
                icon = Icons.Default.LocalPharmacy,
                containerColor = BrandTeal.copy(alpha = 0.12f),
                iconColor = BrandTeal,
            ),
            QuickServiceCategory(
                id = "courier",
                title = courierTitle,
                subtitle = courierSub,
                icon = Icons.Default.LocalShipping,
                containerColor = primaryContainer,
                iconColor = primary,
            ),
            QuickServiceCategory(
                id = "bills",
                title = billsTitle,
                subtitle = billsSub,
                icon = Icons.Default.Payment,
                containerColor = secondaryContainer,
                iconColor = secondary,
            ),
            QuickServiceCategory(
                id = "custom_errand",
                title = customTitle,
                subtitle = customSub,
                icon = Icons.Default.MoreHoriz,
                containerColor = tertiaryContainer,
                iconColor = tertiary,
            ),
        )
    }
}

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
                Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                    Text(
                        text = stringResource(R.string.home_direct_delivery_services),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                        contentPadding = PaddingValues(horizontal = 2.dp),
                    ) {
                        items(
                            items = quickServiceCategories,
                            key = { it.id },
                        ) { cat ->
                            CategoryCard(category = cat, onClick = { onQuickAction(cat.id) })
                        }
                    }
                }
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

@Composable
private fun LocationAndNotificationHeader(
    locationName: String,
    unreadCount: Int,
    onNotificationsClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Location Selector Pill (Theme-Aware Surface)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .clickable { /* Trigger neighborhood picker modal */ }
                .padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = BrandCoral,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = locationName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                    ),
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        // Notification Bell Badge Button (Theme-Aware Surface)
        BadgedBox(
            badge = {
                if (unreadCount > 0) {
                    Badge(containerColor = BrandCoral) {
                        Text(
                            text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                            color = Color.White,
                        )
                    }
                }
            },
        ) {
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = stringResource(R.string.home_notifications),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

@Composable
private fun HeroCreateBanner(onCreate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(BrandCoral)
            .shadow(6.dp, RoundedCornerShape(24.dp), spotColor = BrandCoral.copy(alpha = 0.3f))
            .clickable(onClick = onCreate)
            .padding(HatliSpacing.lg),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Text(
                        text = stringResource(R.string.home_hero_badge_title),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }

                Text(
                    text = stringResource(R.string.home_hero_banner_title),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                    ),
                )

                Text(
                    text = stringResource(R.string.home_hero_banner_description),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                    ),
                )
            }

            Spacer(modifier = Modifier.width(HatliSpacing.sm))

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = BrandCoral,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: QuickServiceCategory,
    onClick: () -> Unit,
) {
    val extended = HatliTheme.extendedColors

    Surface(
        modifier = Modifier
            .width(148.dp)
            .height(138.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.sm,
        shadowElevation = HatliElevation.sm,
        border = BorderStroke(1.dp, extended.border),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(HatliSpacing.md),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(category.iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        tint = category.iconColor,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = category.title,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                    ),
                )

                Text(
                    text = category.subtitle,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun RequesterTipCard(tipText: String) {
    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CoralContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = BrandCoral,
                    modifier = Modifier.size(20.dp),
                )
            }

            Spacer(modifier = Modifier.width(HatliSpacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.home_tip_title),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tipText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun RequesterHomeSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = HatliSpacing.lg, vertical = HatliSpacing.md),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        )
        Box(
            modifier = Modifier
                .width(140.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        )
    }
}
