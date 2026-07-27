package com.app.hatli.feature.cart.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Destination
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.cart.presentation.CartRoute

object CartDestination : Destination {
    override val route: String = Routes.CART
}

fun NavGraphBuilder.cartGraph(onBack: () -> Unit) {
    navigation(
        route = Routes.CART_GRAPH,
        startDestination = CartDestination.route,
    ) {
        composable(CartDestination.route) {
            CartRoute(onBack = onBack)
        }
    }
}
