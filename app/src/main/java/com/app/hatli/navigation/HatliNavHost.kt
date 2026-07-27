package com.app.hatli.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.app.hatli.core.ui.navigation.Navigator
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.auth.navigation.authGraph
import com.app.hatli.feature.cart.navigation.cartGraph
import com.app.hatli.feature.checkout.navigation.checkoutGraph
import com.app.hatli.feature.home.navigation.homeGraph
import com.app.hatli.feature.menu.navigation.menuGraph
import com.app.hatli.feature.orders.navigation.ordersGraph
import com.app.hatli.feature.profile.navigation.profileGraph

@Composable
fun HatliNavHost(
    onMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navigator = remember(navController) { Navigator(navController) }

    NavHost(
        navController = navController,
        startDestination = Routes.HOME_GRAPH,
        modifier = modifier,
    ) {
        homeGraph(
            onNavigateToAuth = { navigator.navigate(Routes.AUTH_GRAPH) },
            onMessage = onMessage,
        )
        authGraph(
            onBack = { navigator.popBackStack() },
            onLoginSuccess = {
                navigator.navigateAndClearBackStack(Routes.HOME_GRAPH)
            },
        )
        menuGraph(onBack = { navigator.popBackStack() })
        cartGraph(onBack = { navigator.popBackStack() })
        checkoutGraph(onBack = { navigator.popBackStack() })
        ordersGraph(onBack = { navigator.popBackStack() })
        profileGraph(onBack = { navigator.popBackStack() })
    }
}
