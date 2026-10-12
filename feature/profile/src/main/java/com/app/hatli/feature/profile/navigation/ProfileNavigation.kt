package com.app.hatli.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.ProfileGraph
import com.app.hatli.core.ui.navigation.ProfileScreenRoute
import com.app.hatli.feature.profile.presentation.ProfileRoute

fun NavGraphBuilder.profileGraph(
    onBack: () -> Unit,
    onLoggedOut: () -> Unit,
) {
    navigation<ProfileGraph>(
        startDestination = ProfileScreenRoute,
    ) {
        composable<ProfileScreenRoute> {
            ProfileRoute(onLoggedOut = onLoggedOut, onBack = onBack)
        }
    }
}
