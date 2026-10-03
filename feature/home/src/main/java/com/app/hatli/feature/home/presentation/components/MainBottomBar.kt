package com.app.hatli.feature.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.MainTab

private object MorphingCapsuleDefaults {
    val BarHeight = 70.dp
    val InactiveItemSize = 46.dp
    val ActiveBadgeSize = 40.dp
    val ActiveItemHeight = 48.dp

    val BarCanvasColor = Color(0xFF1E2022)
    val BarBorderColor = Color(0xFF2A2D30)
    val ActivePillBgColor = Color(0xFF2C3035)
    val InactiveBgColor = Color(0xFF25282C)
    val InactiveIconColor = Color(0xFF8E959E)
    val ActiveBadgeColor = BrandCoral // #FF6B35
    val ActiveIconColor = CharcoalDark // #1E2022
}

@Immutable
private data class TabSpec(
    val tab: MainTab,
    val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val hasBadge: Boolean = false,
)

/**
 * Drop-in wrapper for existing callers of [MainBottomBar].
 */
@Composable
fun MainBottomBar(
    isRequester: Boolean,
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
    hasUnreadMessages: Boolean = false,
    modifier: Modifier = Modifier,
) {
    HatliFloatingBottomBar(
        selectedTab = selected,
        onTabSelected = onSelect,
        isRequester = isRequester,
        hasUnreadMessages = hasUnreadMessages,
        modifier = modifier,
    )
}

/**
 * Sleek "Morphing Capsule / Dynamic Pill" Floating Bottom Navigation Bar.
 * Shrink-wraps active pill content to eliminate dead space after text,
 * applies precise brand color tokens, and distributes tabs cleanly across the bar.
 */
@Composable
fun HatliFloatingBottomBar(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    isRequester: Boolean = true,
    hasUnreadMessages: Boolean = false,
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MorphingCapsuleDefaults.BarBorderColor, CircleShape)
                .shadow(
                    elevation = 16.dp,
                    shape = CircleShape,
                    spotColor = Color.Black.copy(alpha = 0.4f),
                    ambientColor = Color.Black.copy(alpha = 0.25f),
                ),
            shape = CircleShape,
            color = MorphingCapsuleDefaults.BarCanvasColor,
            tonalElevation = 8.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MorphingCapsuleDefaults.BarHeight)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs.forEach { spec ->
                    MorphingCapsuleTabItem(
                        tab = spec,
                        isSelected = selectedTab == spec.tab,
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
    onClick: () -> Unit,
) {
    val label = stringResource(tab.labelRes)

    val pillBackground by animateColorAsState(
        targetValue = if (isSelected) MorphingCapsuleDefaults.ActivePillBgColor else Color.Transparent,
        animationSpec = tween(durationMillis = 180),
        label = "pill_bg",
    )

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(MorphingCapsuleDefaults.ActiveItemHeight)
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
                start = if (isSelected) 4.dp else 0.dp,
                end = if (isSelected) 14.dp else 0.dp,
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isSelected) {
            // Active State: Edge-aligned Vibrant Circular Badge + Controlled 8dp Gap + Wrap-Content White Label
            Box(
                modifier = Modifier
                    .size(MorphingCapsuleDefaults.ActiveBadgeSize)
                    .clip(CircleShape)
                    .background(MorphingCapsuleDefaults.ActiveBadgeColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = tab.iconRes),
                    contentDescription = label,
                    tint = MorphingCapsuleDefaults.ActiveIconColor,
                    modifier = Modifier.size(20.dp),
                )
            }

            AnimatedVisibility(
                visible = isSelected,
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
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                        ),
                        modifier = Modifier.wrapContentWidth(),
                    )
                }
            }
        } else {
            // Inactive State: Compact Circular Target (46.dp x 46.dp)
            Box(
                modifier = Modifier
                    .size(MorphingCapsuleDefaults.InactiveItemSize)
                    .clip(CircleShape)
                    .background(MorphingCapsuleDefaults.InactiveBgColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = tab.iconRes),
                    contentDescription = label,
                    tint = MorphingCapsuleDefaults.InactiveIconColor,
                    modifier = Modifier.size(20.dp),
                )

                // Optional Unread Notification Badge Dot
                if (tab.hasBadge) {
                    Badge(
                        modifier = Modifier
                            .size(10.dp)
                            .align(Alignment.TopEnd)
                            .padding(top = 4.dp, end = 4.dp),
                        containerColor = BrandCoral,
                    )
                }
            }
        }
    }
}

@Preview(name = "Hatli Floating Bottom Bar - Requester", showBackground = true)
@Composable
private fun HatliFloatingBottomBarPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFF8F9FA)),
            contentAlignment = Alignment.BottomCenter,
        ) {
            HatliFloatingBottomBar(
                selectedTab = MainTab.Home,
                onTabSelected = {},
                isRequester = true,
                hasUnreadMessages = true,
            )
        }
    }
}
