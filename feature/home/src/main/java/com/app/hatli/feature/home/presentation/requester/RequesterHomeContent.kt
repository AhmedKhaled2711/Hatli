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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreHoriz
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
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.CurrentRequestCard
import com.app.hatli.feature.home.presentation.components.HomeSectionHeader
import com.app.hatli.feature.home.presentation.components.RequestRowCard

private data class QuickServiceCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val containerColor: Color,
    val iconColor: Color,
)

private val QuickServiceCategories = listOf(
    QuickServiceCategory(
        id = "grocery",
        title = "سوبر ماركت",
        subtitle = "بقالة ومستلزمات منزلية",
        icon = Icons.Default.ShoppingBag,
        containerColor = Color(0xFFFFF4F0),
        iconColor = BrandCoral,
    ),
    QuickServiceCategory(
        id = "pharmacy",
        title = "صيدلية ورعاية",
        subtitle = "أدوية ومستلزمات طبية",
        icon = Icons.Default.LocalPharmacy,
        containerColor = Color(0xFFEAFDF9),
        iconColor = BrandTeal,
    ),
    QuickServiceCategory(
        id = "courier",
        title = "طرد ومستندات",
        subtitle = "نقل أوراق وأغراض",
        icon = Icons.Default.LocalShipping,
        containerColor = Color(0xFFEBF3FE),
        iconColor = Color(0xFF2F6FED),
    ),
    QuickServiceCategory(
        id = "custom_errand",
        title = "مشوار مخصص",
        subtitle = "اطلب أي شيء تريد",
        icon = Icons.Default.MoreHoriz,
        containerColor = Color(0xFFF5EEFE),
        iconColor = Color(0xFF9333EA),
    ),
)

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
                bottom = 110.dp, // Content scrolls cleanly above bottom bar & FAB
            ),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
        ) {
            // Localized Neighborhood Dropdown & Notifications Header
            item(key = "requester_location_header") {
                LocationAndNotificationHeader(
                    locationName = "📍 المحلة الكبرى · حي الأشجار",
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
                        text = "خدمات التوصيل المباشرة",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark,
                        ),
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                        contentPadding = PaddingValues(horizontal = 2.dp),
                    ) {
                        items(
                            items = QuickServiceCategories,
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
        // Location Selector Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
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
                        color = CharcoalDark,
                        fontSize = 13.sp,
                    ),
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = CharcoalMuted,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        // Notification Bell Badge Button
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
                    .background(Color.White),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "التنبيهات",
                    tint = CharcoalDark,
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
                        text = "خدمة التوصيل السريع",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }

                Text(
                    text = "اطلب أي حاجة من أي مكان في مدينتك",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 20.sp,
                    ),
                )

                Text(
                    text = "حدد المشتريات والموقع، وسيتولى الكابتن التوصيل حتى باب بيتك.",
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
    Box(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(category.containerColor)
            .clickable(onClick = onClick)
            .padding(HatliSpacing.md),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
            horizontalAlignment = Alignment.Start,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(category.iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = category.iconColor,
                    modifier = Modifier.size(22.dp),
                )
            }

            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = CharcoalDark,
                    fontSize = 14.sp,
                ),
            )

            Text(
                text = category.subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CharcoalMuted,
                    fontSize = 11.sp,
                ),
            )
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
                    text = "نصيحة هامة",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CharcoalDark,
                    ),
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tipText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CharcoalMuted,
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