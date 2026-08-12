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
import com.app.hatli.feature.chat.navigation.chatGraph
import com.app.hatli.feature.checkout.navigation.checkoutGraph
import com.app.hatli.feature.home.navigation.homeGraph
import com.app.hatli.feature.menu.navigation.menuGraph
import com.app.hatli.feature.orders.navigation.ordersGraph
import com.app.hatli.feature.profile.navigation.profileGraph
import com.app.hatli.feature.profile.presentation.ProfileRoute
import com.app.hatli.feature.request.navigation.requestGraph

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
        navigator.navigateAndClearBackStack(Routes.AUTH_GRAPH)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.AUTH_GRAPH,
        modifier = modifier,
    ) {
        authGraph(
            onNavigateHome = {
                goHomeClearingStack()
            },

            onNavigateOnboarding = {
                navController.navigate(Routes.ONBOARDING) {
                    popUpTo(Routes.SPLASH) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },

            onNavigateStart = {
                navController.navigate(Routes.START) {
                    popUpTo(Routes.SPLASH) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },

            onNavigateLogin = {
                navController.navigate(Routes.LOGIN) {
                    launchSingleTop = true
                }
            },

            onNavigateRole = {
                navController.navigate(Routes.ROLE_SELECT) {
                    launchSingleTop = true
                }
            },

            onNavigateCreateProfile = {
                navController.navigate(Routes.CREATE_PROFILE) {
                    launchSingleTop = true
                }
            },

            onNavigateOtp = { verificationId, phone ->
                navController.navigate(
                    Routes.otp(
                        verificationId = verificationId,
                        phone = phone,
                    ),
                ) {
                    launchSingleTop = true
                }
            },
        )
        homeGraph(
            profileTab = {
                ProfileRoute(
                    onLoggedOut = { goAuthClearingStack() },
                    onBack = {},
                    showBack = false,
                )
            },
            onMessage = onMessage,
            onNavigateCreateRequest = {
                navController.navigate(Routes.CREATE_REQUEST) {
                    launchSingleTop = true
                }
            },
            onOpenChat = { id ->
                navController.navigate(Routes.chat(id)) { launchSingleTop = true }
            },
            onOpenDetail = { id ->
                navController.navigate(Routes.requestDetail(id)) { launchSingleTop = true }
            },
            onOpenRate = { id ->
                navController.navigate(Routes.rateRunner(id)) { launchSingleTop = true }
            },
        )
        requestGraph(
            onBack = { navController.popBackStack() },
            onSubmitted = {
                navController.popBackStack()
                onMessage("Request posted — waiting for a Runner")
            },
            onOpenChat = { id ->
                navController.navigate(Routes.chat(id)) { launchSingleTop = true }
            },
            onOpenRate = { id ->
                navController.navigate(Routes.rateRunner(id)) { launchSingleTop = true }
            },
            onMessage = onMessage,
        )
        chatGraph(
            onBack = { navController.popBackStack() },
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
