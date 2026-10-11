package com.app.hatli.feature.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.MainTab

@Immutable
private data class TabSpec(
    val tab: MainTab,
    val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
    val hasBadge: Boolean = false,
)

/**
 * Drop-in wrapper for callers of [MainBottomBar].
 */
@Composable
fun MainBottomBar(
    isRequester: Boolean,
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
    hasUnreadMessages: Boolean = false,
    isCompact: Boolean = false,
    modifier: Modifier = Modifier,
) {
    HatliFloatingBottomBar(
        selectedTab = selected,
        onTabSelected = onSelect,
        isRequester = isRequester,
        hasUnreadMessages = hasUnreadMessages,
        isCompact = isCompact,
        modifier = modifier,
    )
}

/**
 * Production "Morphing Capsule / Dynamic Pill" Floating Bottom Navigation Bar.
 * - Dynamic Light / Dark mode color fidelity adhering strictly to [MaterialTheme.colorScheme].
 * - Responsive scroll-driven compact state: shrinks gracefully downwards with spring animation on downward scroll,
 *   restores on upward scroll, preserving screen estate without jarring disappearance.
 */
@Composable
fun HatliFloatingBottomBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    isRequester: Boolean = true,
    hasUnreadMessages: Boolean = false,
    isCompact: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        TabSpec(
            tab = MainTab.Home,
            labelRes = R.string.home_tab_home,
            iconRes = R.drawable.ic_home,
        ),
        TabSpec(
            tab = MainTab.RequestsOrJobs,
            labelRes = if (isRequester) R.string.home_tab_requests else R.string.home_tab_jobs,
            iconRes = if (isRequester) R.drawable.ic_requests else R.drawable.ic_jobs,
        ),
        TabSpec(
            tab = MainTab.Messages,
            labelRes = R.string.home_tab_messages,
            iconRes = R.drawable.ic_messages,
            hasBadge = hasUnreadMessages,
        ),
        TabSpec(
            tab = MainTab.Profile,
            labelRes = R.string.home_tab_profile,
            iconRes = R.drawable.ic_profile,
        ),
    )

    val isDark = isSystemInDarkTheme()

    // Dynamic Sizing with Spring Motion Specs
    val currentBarHeight by animateDpAsState(
        targetValue = if (isCompact) 56.dp else 70.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "bar_height",
    )

    val currentVerticalPadding by animateDpAsState(
        targetValue = if (isCompact) 8.dp else 16.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "bar_v_padding",
    )

    val currentTranslationY by animateDpAsState(
        targetValue = if (isCompact) 12.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "bar_translation_y",
    )

    val currentActiveItemHeight by animateDpAsState(
        targetValue = if (isCompact) 40.dp else 48.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "active_item_h",
    )

    val currentActiveBadgeSize by animateDpAsState(
        targetValue = if (isCompact) 36.dp else 40.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "active_badge_size",
    )

    val currentInactiveItemSize by animateDpAsState(
        targetValue = if (isCompact) 38.dp else 46.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "inactive_size",
    )

    val currentIconSize by animateDpAsState(
        targetValue = if (isCompact) 18.dp else 20.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "icon_size",
    )

    // Palette Resolution for Light and Dark Modes
    val barCanvasColor = MaterialTheme.colorScheme.surface
    val barBorderColor = if (isDark) {
        Color(0xFF2A2D30)
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
    }
    val shadowAmbientColor = if (isDark) {
        Color.Black.copy(alpha = 0.35f)
    } else {
        Color.Black.copy(alpha = 0.06f)
    }
    val shadowSpotColor = if (isDark) {
        Color.Black.copy(alpha = 0.5f)
    } else {
        Color.Black.copy(alpha = 0.12f)
    }

    val inactiveItemBg = MaterialTheme.colorScheme.surfaceVariant
    val inactiveIconColor = MaterialTheme.colorScheme.onSurfaceVariant

    val activePillBg = if (isDark) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    val activeBadgeColor = MaterialTheme.colorScheme.primary
    val activeIconColor = MaterialTheme.colorScheme.onPrimary
    val activeTextColor = if (isDark) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
    val badgeDotColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = currentVerticalPadding)
            .graphicsLayer {
                translationY = currentTranslationY.toPx()
            },
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, barBorderColor, CircleShape)
                .shadow(
                    elevation = if (isCompact) 8.dp else 16.dp,
                    shape = CircleShape,
                    spotColor = shadowSpotColor,
                    ambientColor = shadowAmbientColor,
                ),
            shape = CircleShape,
            color = barCanvasColor,
            tonalElevation = if (isDark) 8.dp else 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(currentBarHeight)
                    .padding(horizontal = 8.dp, vertical = if (isCompact) 6.dp else 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs.forEach { spec ->
                    MorphingCapsuleTabItem(
                        tab = spec,
                        isSelected = selectedTab == spec.tab,
                        isCompact = isCompact,
                        activeItemHeight = currentActiveItemHeight,
                        activeBadgeSize = currentActiveBadgeSize,
                        inactiveItemSize = currentInactiveItemSize,
                        iconSize = currentIconSize,
                        activePillBg = activePillBg,
                        activeBadgeColor = activeBadgeColor,
                        activeIconColor = activeIconColor,
                        activeTextColor = activeTextColor,
                        inactiveBgColor = inactiveItemBg,
                        inactiveIconColor = inactiveIconColor,
                        badgeDotColor = badgeDotColor,
                        onClick = { onTabSelected(spec.tab) },
                    )
                }
            }
        }
    }
}

@Composable
private fun MorphingCapsuleTabItem(
    tab: TabSpec,
    isSelected: Boolean,
    isCompact: Boolean,
    activeItemHeight: Dp,
    activeBadgeSize: Dp,
    inactiveItemSize: Dp,
    iconSize: Dp,
    activePillBg: Color,
    activeBadgeColor: Color,
    activeIconColor: Color,
    activeTextColor: Color,
    inactiveBgColor: Color,
    inactiveIconColor: Color,
    badgeDotColor: Color,
    onClick: () -> Unit,
) {
    val label = stringResource(tab.labelRes)

    val pillBackground by animateColorAsState(
        targetValue = if (isSelected) activePillBg else Color.Transparent,
        animationSpec = tween(durationMillis = 180),
        label = "pill_bg",
    )

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(activeItemHeight)
            .clip(CircleShape)
            .background(pillBackground)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow,
                ),
            )
            .padding(
                start = if (isSelected) (if (isCompact) 2.dp else 4.dp) else 0.dp,
                end = if (isSelected) (if (isCompact) 2.dp else 14.dp) else 0.dp,
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isSelected) {
            // Active State: Edge-aligned Vibrant Circular Badge
            Box(
                modifier = Modifier
                    .size(activeBadgeSize)
                    .clip(CircleShape)
                    .background(activeBadgeColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = tab.iconRes),
                    contentDescription = label,
                    tint = activeIconColor,
                    modifier = Modifier.size(iconSize),
                )
            }

            AnimatedVisibility(
                visible = isSelected && !isCompact,
                enter = fadeIn(animationSpec = tween(180, delayMillis = 40)) +
                        expandHorizontally(expandFrom = Alignment.Start),
                exit = fadeOut(animationSpec = tween(100)) +
                        shrinkHorizontally(shrinkTowards = Alignment.Start),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = activeTextColor,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                        ),
                        modifier = Modifier.wrapContentWidth(),
                    )
                }
            }
        } else {
            // Inactive State: Compact Circular Target
            Box(
                modifier = Modifier
                    .size(inactiveItemSize)
                    .clip(CircleShape)
                    .background(inactiveBgColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = tab.iconRes),
                    contentDescription = label,
                    tint = inactiveIconColor,
                    modifier = Modifier.size(iconSize),
                )

                // Optional Unread Notification Badge Dot
                if (tab.hasBadge) {
                    Badge(
                        modifier = Modifier
                            .size(if (isCompact) 8.dp else 10.dp)
                            .align(Alignment.TopEnd)
                            .padding(
                                top = if (isCompact) 2.dp else 4.dp,
                                end = if (isCompact) 2.dp else 4.dp,
                            ),
                        containerColor = badgeDotColor,
                    )
                }
            }
        }
    }
}

@Preview(name = "Hatli Floating Bottom Bar - Requester Light", showBackground = true)
@Composable
private fun HatliFloatingBottomBarPreviewLight() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFFF8F9FA)),
            contentAlignment = Alignment.BottomCenter,
        ) {
            HatliFloatingBottomBar(
                selectedTab = MainTab.Home,
                onTabSelected = {},
                isRequester = true,
                hasUnreadMessages = true,
                isCompact = false,
            )
        }
    }
}
