package com.app.hatli.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Destination
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.home.presentation.CreateRequestStubScreen
import com.app.hatli.feature.home.presentation.MainShellRoute

object MainShellDestination : Destination {
    override val route: String = Routes.MAIN_SHELL
}

object CreateRequestStubDestination : Destination {
    override val route: String = Routes.CREATE_REQUEST_STUB
}

fun NavGraphBuilder.homeGraph(
    profileTab: @Composable () -> Unit,
    onMessage: (String) -> Unit,
    onNavigateCreateStub: () -> Unit,
    onBackFromCreateStub: () -> Unit,
) {
    navigation(
        route = Routes.HOME_GRAPH,
        startDestination = MainShellDestination.route,
    ) {
        composable(MainShellDestination.route) {
            MainShellRoute(
                profileTab = profileTab,
                onCreateRequestStub = onNavigateCreateStub,
                onMessage = onMessage,
            )
        }
        composable(CreateRequestStubDestination.route) {
            CreateRequestStubScreen(onBack = onBackFromCreateStub)
        }
    }
}
