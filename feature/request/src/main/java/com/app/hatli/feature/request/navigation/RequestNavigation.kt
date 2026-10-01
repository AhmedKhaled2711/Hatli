package com.app.hatli.feature.request.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.CreateRequestRoute as CreateRequestRouteKey
import com.app.hatli.core.ui.navigation.RateRunnerRoute as RateRunnerRouteKey
import com.app.hatli.core.ui.navigation.RequestDetailRoute as RequestDetailRouteKey
import com.app.hatli.core.ui.navigation.RequestGraph
import com.app.hatli.feature.request.presentation.detail.RequestDetailRoute
import com.app.hatli.feature.request.presentation.rate.RateRunnerRoute
import com.app.hatli.feature.request.presentation.request.CreateRequestRoute

fun NavGraphBuilder.requestGraph(
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenRate: (String) -> Unit = {},
    onMessage: (String) -> Unit,
) {
    navigation<RequestGraph>(
        startDestination = CreateRequestRouteKey,
    ) {
        composable<CreateRequestRouteKey> {
            CreateRequestRoute(
                onBack = onBack,
                onSubmitted = onSubmitted,
                onMessage = onMessage,
            )
        }
        composable<RequestDetailRouteKey> {
            RequestDetailRoute(
                onBack = onBack,
                onOpenChat = onOpenChat,
                onOpenRate = onOpenRate,
                onMessage = onMessage,
            )
        }
        composable<RateRunnerRouteKey> {
            RateRunnerRoute(
                onDone = onBack,
                onMessage = onMessage,
            )
        }
    }
}
