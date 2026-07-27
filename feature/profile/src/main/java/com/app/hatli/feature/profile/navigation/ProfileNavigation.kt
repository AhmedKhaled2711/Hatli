package com.app.hatli.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Destination
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.profile.presentation.ProfileRoute

object ProfileDestination : Destination {
    override val route: String = Routes.PROFILE
}

fun NavGraphBuilder.profileGraph(
    onBack: () -> Unit,
    onLoggedOut: () -> Unit,
) {
    navigation(
        route = Routes.PROFILE_GRAPH,
        startDestination = ProfileDestination.route,
    ) {
        composable(ProfileDestination.route) {
            ProfileRoute(onLoggedOut = onLoggedOut, onBack = onBack)
        }
    }
}
