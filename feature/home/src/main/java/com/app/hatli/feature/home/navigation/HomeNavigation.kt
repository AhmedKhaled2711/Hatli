package com.app.hatli.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.HomeGraph
import com.app.hatli.core.ui.navigation.MainShellRoute as MainShellRouteKey
import com.app.hatli.feature.home.presentation.MainShellRoute

fun NavGraphBuilder.homeGraph(
    profileTab: @Composable () -> Unit,
    onMessage: (String) -> Unit,
    onNavigateCreateRequest: (String?) -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenRate: (String) -> Unit,
) {
    navigation<HomeGraph>(
        startDestination = MainShellRouteKey,
    ) {
        composable<MainShellRouteKey> {
            MainShellRoute(
                profileTab = profileTab,
                onCreateRequest = onNavigateCreateRequest,
                onOpenChat = onOpenChat,
                onOpenDetail = onOpenDetail,
                onOpenRate = onOpenRate,
                onMessage = onMessage,
            )
        }
    }
}
