package com.app.hatli.feature.request.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Destination
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.request.presentation.CreateRequestRoute
import com.app.hatli.feature.request.presentation.detail.RequestDetailRoute
import com.app.hatli.feature.request.presentation.rate.RateRunnerRoute

object CreateRequestDestination : Destination {
    override val route: String = Routes.CREATE_REQUEST
}

fun NavGraphBuilder.requestGraph(
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenRate: (String) -> Unit = {},
    onMessage: (String) -> Unit,
) {
    navigation(
        route = Routes.REQUEST_GRAPH,
        startDestination = CreateRequestDestination.route,
    ) {
        composable(CreateRequestDestination.route) {
            CreateRequestRoute(
                onBack = onBack,
                onSubmitted = onSubmitted,
                onMessage = onMessage,
            )
        }
        composable(
            route = Routes.REQUEST_DETAIL,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType }),
        ) {
            RequestDetailRoute(
                onBack = onBack,
                onOpenChat = onOpenChat,
                onOpenRate = onOpenRate,
                onMessage = onMessage,
            )
        }
        composable(
            route = Routes.RATE_RUNNER,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType }),
        ) {
            RateRunnerRoute(
                onDone = onBack,
                onMessage = onMessage,
            )
        }
    }
}
