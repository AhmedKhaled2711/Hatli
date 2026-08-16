package com.app.hatli.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.auth.presentation.login.LoginRoute
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingRoute
import com.app.hatli.feature.auth.presentation.otp.OtpRoute
import com.app.hatli.feature.auth.presentation.profile.CreateProfileRoute
import com.app.hatli.feature.auth.presentation.role.RoleRoute
import com.app.hatli.feature.auth.presentation.splash.SplashRoute
import com.app.hatli.feature.auth.presentation.start.StartRoute

fun NavGraphBuilder.authGraph(
    onNavigateHome: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateOnboarding: () -> Unit,
    onNavigateStart: () -> Unit,
    onNavigateRole: () -> Unit,
    onNavigateCreateProfile: () -> Unit,
    onNavigateOtp: (verificationId: String, phone: String) -> Unit,
) {
    navigation(
        route = Routes.AUTH_GRAPH,
        startDestination = Routes.SPLASH,
    ) {
        composable(Routes.SPLASH) {
            SplashRoute(
                onOnboarding = onNavigateOnboarding,
                onStart = onNavigateStart,
                onLogin = onNavigateLogin,
                onCreateProfile = onNavigateCreateProfile,
                onHome = onNavigateHome,
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingRoute(
                onStart = onNavigateStart,
            )
        }

        composable(Routes.START) {
            StartRoute(
                onBack = onNavigateOnboarding,
                onPhoneLogin = onNavigateLogin,
            )
        }

        composable(Routes.LOGIN) {
            LoginRoute(
                onBack = onNavigateStart,
                onOtp = onNavigateOtp,
            )
        }

        composable(
            route = Routes.OTP,
            arguments = listOf(
                navArgument("verificationId") {
                    type = NavType.StringType
                },
                navArgument("phone") {
                    type = NavType.StringType
                },
            ),
        ) {
            OtpRoute(
                onBack = onNavigateLogin,
                onRoleSelect = onNavigateRole,
                onCreateProfile = onNavigateCreateProfile,
                onHome = onNavigateHome,
            )
        }

        composable(Routes.ROLE_SELECT) {
            RoleRoute(
                onCreateProfile = onNavigateCreateProfile,
            )
        }

        composable(Routes.CREATE_PROFILE) {
            CreateProfileRoute(
                onHome = onNavigateHome,
            )
        }
    }
}