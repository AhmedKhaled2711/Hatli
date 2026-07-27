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

    fun goHomeClearingStack() {
        navigator.navigateAndClearBackStack(Routes.HOME_GRAPH)
    }

    fun goAuthClearingStack() {
        // Splash re-resolves → Login (session cleared). Clears Home from back stack.
        navigator.navigateAndClearBackStack(Routes.AUTH_GRAPH)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.AUTH_GRAPH,
        modifier = modifier,
    ) {
        authGraph(
            onNavigateHome = { goHomeClearingStack() },
            onNavigateLogin = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onNavigateOnboarding = {
                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onNavigateRole = {
                navController.navigate(Routes.ROLE_SELECT) { launchSingleTop = true }
            },
            onNavigateCreateProfile = {
                navController.navigate(Routes.CREATE_PROFILE) { launchSingleTop = true }
            },
            onNavigateOtp = { verificationId, phone ->
                navController.navigate(Routes.otp(verificationId, phone)) {
                    launchSingleTop = true
                }
            },
        )
        homeGraph(
            onNavigateToAuth = { goAuthClearingStack() },
            onNavigateToProfile = { navigator.navigate(Routes.PROFILE_GRAPH) },
            onMessage = onMessage,
        )
        menuGraph(onBack = { navigator.popBackStack() })
        cartGraph(onBack = { navigator.popBackStack() })
        checkoutGraph(onBack = { navigator.popBackStack() })
        ordersGraph(onBack = { navigator.popBackStack() })
        profileGraph(
            onBack = { navigator.popBackStack() },
            onLoggedOut = { goAuthClearingStack() },
        )
    }
}
