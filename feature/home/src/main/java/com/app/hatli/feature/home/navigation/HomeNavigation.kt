package com.app.hatli.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Destination
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.home.presentation.HomeRoute

object HomeDestination : Destination {
    override val route: String = Routes.HOME
}

fun NavGraphBuilder.homeGraph(
    onNavigateToAuth: () -> Unit,
    onMessage: (String) -> Unit,
) {
    navigation(
        route = Routes.HOME_GRAPH,
        startDestination = HomeDestination.route,
    ) {
        composable(HomeDestination.route) {
            HomeRoute(
                onNavigateToAuth = onNavigateToAuth,
                onMessage = onMessage,
            )
        }
    }
}
