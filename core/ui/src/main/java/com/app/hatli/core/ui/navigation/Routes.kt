package com.app.hatli.core.ui.navigation

import kotlinx.serialization.Serializable

// Deep link metadata constants
object DeepLinkConstants {
    const val HOST = "hatli.app"
    const val SCHEME = "https"
}

// Navigation Graphs
@Serializable
data object AuthGraph

@Serializable
data object HomeGraph

@Serializable
data object RequestGraph

@Serializable
data object ChatGraph

@Serializable
data object ProfileGraph

// Auth Destinations
@Serializable
data object SplashRoute

@Serializable
data object OnboardingRoute

@Serializable
data object StartRoute

@Serializable
data object LoginRoute

@Serializable
data class OtpRoute(val verificationId: String, val phone: String)

@Serializable
data object RoleSelectRoute

@Serializable
data object CreateProfileRoute

// Home & Shell Destinations
@Serializable
data object MainShellRoute

// Request Destinations
@Serializable
data object CreateRequestRoute

@Serializable
data class RequestDetailRoute(val requestId: String)

@Serializable
data class RateRunnerRoute(val requestId: String)

// Chat Destinations
@Serializable
data class ChatRoute(val requestId: String)

// Profile Destination
@Serializable
data object ProfileScreenRoute
