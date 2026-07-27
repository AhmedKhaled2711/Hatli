package com.app.hatli.feature.menu.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Destination
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.menu.presentation.MenuRoute

object MenuDestination : Destination {
    override val route: String = Routes.MENU
}

fun NavGraphBuilder.menuGraph(onBack: () -> Unit) {
    navigation(
        route = Routes.MENU_GRAPH,
        startDestination = MenuDestination.route,
    ) {
        composable(MenuDestination.route) {
            MenuRoute(onBack = onBack)
        }
    }
}
