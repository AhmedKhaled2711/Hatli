package com.app.hatli.feature.orders.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Destination
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.orders.presentation.OrdersRoute

object OrdersDestination : Destination {
    override val route: String = Routes.ORDERS
}

fun NavGraphBuilder.ordersGraph(onBack: () -> Unit) {
    navigation(
        route = Routes.ORDERS_GRAPH,
        startDestination = OrdersDestination.route,
    ) {
        composable(OrdersDestination.route) {
            OrdersRoute(onBack = onBack)
        }
    }
}
