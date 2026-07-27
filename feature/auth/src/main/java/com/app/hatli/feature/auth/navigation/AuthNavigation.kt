package com.app.hatli.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Destination
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.auth.presentation.AuthRoute

object AuthDestination : Destination {
    override val route: String = Routes.AUTH
}

fun NavGraphBuilder.authGraph(
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    navigation(
        route = Routes.AUTH_GRAPH,
        startDestination = AuthDestination.route,
    ) {
        composable(
            route = AuthDestination.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern =
                        "${Routes.DEEP_LINK_SCHEME}://${Routes.DEEP_LINK_HOST}/auth"
                },
            ),
        ) {
            AuthRoute(onBack = onBack, onLoginSuccess = onLoginSuccess)
        }
    }
}
