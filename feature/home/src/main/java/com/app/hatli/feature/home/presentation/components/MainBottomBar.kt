package com.app.hatli.feature.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.MainTab

private object MainBottomBarDefaults {
    val Height = 64.dp
    val ItemHeight = 50.dp
    val IconSize = 22.dp
    val OuterCornerRadius = 32.dp
    val ItemCornerRadius = 24.dp
    val IconLabelSpacing = 8.dp
    val ItemSpacing = 4.dp
    const val AnimationDurationMillis = 220
}

@Immutable
private data class TabSpec(
    val tab: MainTab,
    val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val hasBadge: Boolean = false,
)

@Composable
fun MainBottomBar(
    isRequester: Boolean,
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
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
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(MainBottomBarDefaults.OuterCornerRadius),
                    spotColor = CharcoalDark.copy(alpha = 0.12f),
                    ambientColor = CharcoalDark.copy(alpha = 0.06f),
                ),
            shape = RoundedCornerShape(MainBottomBarDefaults.OuterCornerRadius),
            color = Color.White,
            tonalElevation = 0.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MainBottomBarDefaults.Height)
                    .padding(horizontal = 6.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.spacedBy(MainBottomBarDefaults.ItemSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs.forEach { tab ->
                    MainBottomBarItem(
                        tab = tab,
                        selected = selected == tab.tab,
                        onClick = { onSelect(tab.tab) },
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.MainBottomBarItem(
    tab: TabSpec,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val label = stringResource(tab.labelRes)

    val targetWeight = if (selected) 2.2f else 1f

    val animatedWeight by animateFloatAsState(
        targetValue = targetWeight,
        animationSpec = tween(
            durationMillis = MainBottomBarDefaults.AnimationDurationMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "bottom_bar_item_weight",
    )

    val containerColor by animateColorAsState(
        targetValue = if (selected) CoralContainer else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "tabContainerBg",
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) BrandCoral else CharcoalMuted,
        animationSpec = tween(durationMillis = 200),
        label = "tabContentColor",
    )

    Row(
        modifier = Modifier
            .weight(animatedWeight)
            .height(MainBottomBarDefaults.ItemHeight)
            .clip(RoundedCornerShape(MainBottomBarDefaults.ItemCornerRadius))
            .background(containerColor)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            )
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = MainBottomBarDefaults.AnimationDurationMillis,
                    easing = FastOutSlowInEasing,
                ),
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = tab.iconRes),
                contentDescription = label,
                modifier = Modifier.size(MainBottomBarDefaults.IconSize),
                tint = contentColor,
            )

            // Unread Badge Indicator
            if (tab.hasBadge) {
                Badge(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp),
                    containerColor = BrandCoral,
                )
            }
        }

        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 160, delayMillis = 30),
            ) + expandHorizontally(
                animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
                expandFrom = Alignment.Start,
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 100),
            ) + shrinkHorizontally(
                animationSpec = tween(durationMillis = 140, easing = FastOutSlowInEasing),
                shrinkTowards = Alignment.Start,
            ),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(MainBottomBarDefaults.IconLabelSpacing))

                Text(
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = BrandCoral,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                    ),
                )
            }
        }
    }
}

