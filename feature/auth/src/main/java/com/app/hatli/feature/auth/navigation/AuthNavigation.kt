package com.app.hatli.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.AuthGraph
import com.app.hatli.core.ui.navigation.CreateProfileRoute as CreateProfileRouteKey
import com.app.hatli.core.ui.navigation.LoginRoute as LoginRouteKey
import com.app.hatli.core.ui.navigation.OnboardingRoute as OnboardingRouteKey
import com.app.hatli.core.ui.navigation.OtpRoute as OtpRouteKey
import com.app.hatli.core.ui.navigation.RoleSelectRoute as RoleSelectRouteKey
import com.app.hatli.core.ui.navigation.SplashRoute as SplashRouteKey
import com.app.hatli.core.ui.navigation.StartRoute as StartRouteKey
import com.app.hatli.feature.auth.presentation.login.LoginRoute
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingRoute
import com.app.hatli.feature.auth.presentation.otp.OtpRoute
import com.app.hatli.feature.auth.presentation.profile.CreateProfileRoute
import com.app.hatli.feature.auth.presentation.role.RoleRoute
import com.app.hatli.feature.auth.presentation.splash.SplashRoute
import com.app.hatli.feature.auth.presentation.start.StartRoute

fun NavGraphBuilder.authGraph(
    startDestination: Any = StartRouteKey,
    onNavigateHome: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateOnboarding: () -> Unit,
    onNavigateStart: () -> Unit,
    onNavigateRole: () -> Unit,
    onNavigateCreateProfile: () -> Unit,
    onNavigateOtp: (verificationId: String, phone: String) -> Unit,
) {
    navigation<AuthGraph>(
        startDestination = startDestination,
    ) {
        composable<SplashRouteKey> {
            SplashRoute(
                onOnboarding = onNavigateOnboarding,
                onStart = onNavigateStart,
                onLogin = onNavigateLogin,
                onRoleSelect = onNavigateRole,
                onCreateProfile = onNavigateCreateProfile,
                onHome = onNavigateHome,
            )
        }

        composable<OnboardingRouteKey> {
            OnboardingRoute(
                onStart = onNavigateStart,
            )
        }

        composable<StartRouteKey> {
            StartRoute(
                onBack = onNavigateOnboarding,
                onPhoneLogin = onNavigateLogin,
            )
        }

        composable<LoginRouteKey> {
            LoginRoute(
                onBack = onNavigateStart,
                onOtp = onNavigateOtp,
            )
        }

        composable<OtpRouteKey> {
            OtpRoute(
                onBack = onNavigateLogin,
                onRoleSelect = onNavigateRole,
                onCreateProfile = onNavigateCreateProfile,
                onHome = onNavigateHome,
            )
        }

        composable<RoleSelectRouteKey> {
            RoleRoute(
                onCreateProfile = onNavigateCreateProfile,
            )
        }

        composable<CreateProfileRouteKey> {
            CreateProfileRoute(
                onHome = onNavigateHome,
            )
        }
    }
}