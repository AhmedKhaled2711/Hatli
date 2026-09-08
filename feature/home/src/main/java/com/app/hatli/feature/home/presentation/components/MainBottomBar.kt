package com.app.hatli.feature.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.MainTab

private object MainBottomBarDefaults {

    val HorizontalPadding = 16.dp
    val VerticalPadding = 10.dp

    val Height = 76.dp
    val ItemHeight = 60.dp

    val IconContainerSize = 52.dp
    val IconSize = 26.dp

    val OuterCornerRadius = 40.dp
    val ItemCornerRadius = 32.dp

    val IconLabelSpacing = 10.dp
    val ItemSpacing = 4.dp

    val MaxWidth = 560.dp

    const val SelectedWeight = 2.3f
    const val UnselectedWeight = 1f

    const val AnimationDurationMillis = 250
}

@Immutable
private data class TabSpec(
    val tab: MainTab,
    val labelRes: Int,
    @DrawableRes val iconRes: Int,
)

@Composable
fun MainBottomBar(
    isRequester: Boolean,
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
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
            labelRes = if (isRequester) {
                R.string.home_tab_requests
            } else {
                R.string.home_tab_jobs
            },
            iconRes = if (isRequester) {
                R.drawable.ic_requests
            } else {
                R.drawable.ic_jobs
            },
        ),
        TabSpec(
            tab = MainTab.Messages,
            labelRes = R.string.home_tab_messages,
            iconRes = R.drawable.ic_messages,
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
            .padding(
                horizontal = MainBottomBarDefaults.HorizontalPadding,
                vertical = MainBottomBarDefaults.VerticalPadding,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .width(MainBottomBarDefaults.MaxWidth),
            shape = RoundedCornerShape(
                MainBottomBarDefaults.OuterCornerRadius,
            ),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            shadowElevation = 8.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MainBottomBarDefaults.Height)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    MainBottomBarDefaults.ItemSpacing,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs.forEach { tab ->
                    MainBottomBarItem(
                        tab = tab,
                        selected = selected == tab.tab,
                        onClick = {
                            onSelect(tab.tab)
                        },
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

    val targetWeight = if (selected) {
        MainBottomBarDefaults.SelectedWeight
    } else {
        MainBottomBarDefaults.UnselectedWeight
    }

    val animatedWeight by animateFloatAsState(
        targetValue = targetWeight,
        animationSpec = tween(
            durationMillis = MainBottomBarDefaults.AnimationDurationMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "bottom_bar_item_weight",
    )

    Row(
        modifier = Modifier
            .weight(animatedWeight)
            .height(MainBottomBarDefaults.ItemHeight)
            .clip(
                RoundedCornerShape(
                    MainBottomBarDefaults.ItemCornerRadius,
                ),
            )
            .background(
                color = if (selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
            )
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab,
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
            modifier = Modifier
                .size(MainBottomBarDefaults.IconContainerSize)
                .clip(CircleShape)
                .background(
                    color = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = tab.iconRes),
                contentDescription = label,
                modifier = Modifier.size(
                    MainBottomBarDefaults.IconSize,
                ),
                tint = if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }

        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 180,
                    delayMillis = 40,
                ),
            ) + expandHorizontally(
                animationSpec = tween(
                    durationMillis = 220,
                    easing = FastOutSlowInEasing,
                ),
                expandFrom = Alignment.Start,
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 100,
                ),
            ) + shrinkHorizontally(
                animationSpec = tween(
                    durationMillis = 160,
                    easing = FastOutSlowInEasing,
                ),
                shrinkTowards = Alignment.Start,
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier.width(
                        MainBottomBarDefaults.IconLabelSpacing,
                    ),
                )

                Text(
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}
