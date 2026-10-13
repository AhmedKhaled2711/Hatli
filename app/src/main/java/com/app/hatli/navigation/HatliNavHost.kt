package com.app.hatli.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.app.hatli.core.ui.navigation.AuthGraph
import com.app.hatli.core.ui.navigation.ChatRoute
import com.app.hatli.core.ui.navigation.CreateProfileRoute
import com.app.hatli.core.ui.navigation.CreateRequestRoute
import com.app.hatli.core.ui.navigation.HomeGraph
import com.app.hatli.core.ui.navigation.LoginRoute
import com.app.hatli.core.ui.navigation.Navigator
import com.app.hatli.core.ui.navigation.OnboardingRoute
import com.app.hatli.core.ui.navigation.OtpRoute
import com.app.hatli.core.ui.navigation.RateRunnerRoute
import com.app.hatli.core.ui.navigation.RequestDetailRoute
import com.app.hatli.core.ui.navigation.RoleSelectRoute
import com.app.hatli.core.domain.usecase.user.ResolveStartDestinationUseCase
import com.app.hatli.core.ui.navigation.SplashRoute
import com.app.hatli.core.ui.navigation.StartRoute
import com.app.hatli.feature.auth.navigation.authGraph
import com.app.hatli.feature.chat.navigation.chatGraph
import com.app.hatli.feature.home.navigation.homeGraph
import com.app.hatli.feature.profile.navigation.profileGraph
import com.app.hatli.feature.profile.presentation.ProfileRoute
import com.app.hatli.feature.request.navigation.requestGraph

@Composable
fun HatliNavHost(
    initialDestination: ResolveStartDestinationUseCase.StartDestination,
    onMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navigator = remember(navController) { Navigator(navController) }

    fun goHomeClearingStack() {
        navigator.navigateAndClearBackStack(HomeGraph)
    }

    fun goAuthClearingStack() {
        navigator.navigateAndClearBackStack(AuthGraph)
    }

    val rootStartDestination: Any = when (initialDestination) {
        ResolveStartDestinationUseCase.StartDestination.Home -> HomeGraph
        else -> AuthGraph
    }

    val authStartDestination: Any = when (initialDestination) {
        ResolveStartDestinationUseCase.StartDestination.Onboarding -> OnboardingRoute
        ResolveStartDestinationUseCase.StartDestination.Start -> StartRoute
        ResolveStartDestinationUseCase.StartDestination.RoleSelect -> RoleSelectRoute
        ResolveStartDestinationUseCase.StartDestination.CreateProfile -> CreateProfileRoute
        ResolveStartDestinationUseCase.StartDestination.Login -> LoginRoute
        ResolveStartDestinationUseCase.StartDestination.Home -> StartRoute
    }

    NavHost(
        navController = navController,
        startDestination = rootStartDestination,
        modifier = modifier,
    ) {
        authGraph(
            startDestination = authStartDestination,
            onNavigateHome = {
                goHomeClearingStack()
            },

            onNavigateOnboarding = {
                navController.navigate(OnboardingRoute) {
                    launchSingleTop = true
                }
            },

            onNavigateStart = {
                navController.navigate(StartRoute) {
                    launchSingleTop = true
                }
            },

            onNavigateLogin = {
                navController.navigate(LoginRoute) {
                    launchSingleTop = true
                }
            },

            onNavigateRole = {
                navController.navigate(RoleSelectRoute) {
                    launchSingleTop = true
                }
            },

            onNavigateCreateProfile = {
                navController.navigate(CreateProfileRoute) {
                    launchSingleTop = true
                }
            },

            onNavigateOtp = { verificationId, phone ->
                navController.navigate(
                    OtpRoute(
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
            onNavigateCreateRequest = { category ->
                navController.navigate(CreateRequestRoute(initialCategory = category)) {
                    launchSingleTop = true
                }
            },
            onOpenChat = { id ->
                navController.navigate(ChatRoute(id)) { launchSingleTop = true }
            },
            onOpenDetail = { id ->
                navController.navigate(RequestDetailRoute(id)) { launchSingleTop = true }
            },
            onOpenRate = { id ->
                navController.navigate(RateRunnerRoute(id)) { launchSingleTop = true }
            },
        )
        requestGraph(
            onBack = { navController.popBackStack() },
            onSubmitted = {
                navController.popBackStack()
                onMessage("Request posted — waiting for a Runner")
            },
            onOpenChat = { id ->
                navController.navigate(ChatRoute(id)) { launchSingleTop = true }
            },
            onOpenRate = { id ->
                navController.navigate(RateRunnerRoute(id)) { launchSingleTop = true }
            },
            onMessage = onMessage,
        )
        chatGraph(
            onBack = { navController.popBackStack() },
            onMessage = onMessage,
        )
        profileGraph(
            onBack = { navigator.popBackStack() },
            onLoggedOut = { goAuthClearingStack() },
        )
    }
}
