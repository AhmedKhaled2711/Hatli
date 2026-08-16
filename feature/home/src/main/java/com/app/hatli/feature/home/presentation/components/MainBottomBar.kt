package com.app.hatli.feature.home.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.MainTab

@Composable
fun MainBottomBar(
    isRequester: Boolean,
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
) {
    data class TabSpec(
        val tab: MainTab,
        val labelRes: Int,
        val icon: ImageVector,
    )

    val tabs = listOf(
        TabSpec(
            tab = MainTab.Home,
            labelRes = R.string.home_tab_home,
            icon = Icons.Outlined.Home,
        ),
        TabSpec(
            tab = MainTab.RequestsOrJobs,
            labelRes = if (isRequester) {
                R.string.home_tab_requests
            } else {
                R.string.home_tab_jobs
            },
            icon = if (isRequester) {
                Icons.AutoMirrored.Outlined.ListAlt
            } else {
                Icons.Outlined.WorkOutline
            },
        ),
        TabSpec(
            tab = MainTab.Messages,
            labelRes = R.string.home_tab_messages,
            icon = Icons.Outlined.ChatBubbleOutline,
        ),
        TabSpec(
            tab = MainTab.Profile,
            labelRes = R.string.home_tab_profile,
            icon = Icons.Outlined.Person,
        ),
    )

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        tabs.forEach { tab ->
            val isSelected = selected == tab.tab
            val label = stringResource(tab.labelRes)

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onSelect(tab.tab)
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = label,
                    )
                },
                label = {
                    Text(text = label)
                },
            )
        }
    }
}