package com.app.hatli.feature.checkout.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Destination
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.checkout.presentation.CheckoutRoute

object CheckoutDestination : Destination {
    override val route: String = Routes.CHECKOUT
}

fun NavGraphBuilder.checkoutGraph(onBack: () -> Unit) {
    navigation(
        route = Routes.CHECKOUT_GRAPH,
        startDestination = CheckoutDestination.route,
    ) {
        composable(CheckoutDestination.route) {
            CheckoutRoute(onBack = onBack)
        }
    }
}
