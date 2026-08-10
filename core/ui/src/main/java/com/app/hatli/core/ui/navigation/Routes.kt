package com.app.hatli.core.ui.navigation

/**
 * Type-safe route contract. Feature modules expose Destinations; app wires graphs.
 */
interface Destination {
    val route: String
}

object Routes {
    const val SPLASH = "splash"
    const val START = "start"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val OTP = "otp/{verificationId}/{phone}"
    const val ROLE_SELECT = "role_select"
    const val CREATE_PROFILE = "create_profile"

    const val HOME_GRAPH = "home_graph"
    const val AUTH_GRAPH = "auth_graph"
    const val MENU_GRAPH = "menu_graph"
    const val CART_GRAPH = "cart_graph"
    const val CHECKOUT_GRAPH = "checkout_graph"
    const val ORDERS_GRAPH = "orders_graph"
    const val PROFILE_GRAPH = "profile_graph"

    /** Post-auth main shell (bottom nav). */
    const val MAIN_SHELL = "main_shell"

    /** Assisted Create Request wizard (Phase 3). */
    const val REQUEST_GRAPH = "request_graph"
    const val CREATE_REQUEST = "create_request"
    const val REQUEST_DETAIL = "request_detail/{requestId}"
    const val RATE_RUNNER = "rate_runner/{requestId}"

    /** Chat thread (Phase 4). */
    const val CHAT_GRAPH = "chat_graph"
    const val CHAT = "chat/{requestId}"

    fun requestDetail(requestId: String): String = "request_detail/$requestId"
    fun rateRunner(requestId: String): String = "rate_runner/$requestId"
    fun chat(requestId: String): String = "chat/$requestId"

    const val HOME = "home"
    const val AUTH = "auth"
    const val MENU = "menu"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val ORDERS = "orders"
    const val PROFILE = "profile"

    const val DEEP_LINK_HOST = "hatli.app"
    const val DEEP_LINK_SCHEME = "https"

    fun otp(verificationId: String, phone: String): String {
        val encodedPhone = java.net.URLEncoder.encode(phone, Charsets.UTF_8.name())
        val encodedId = java.net.URLEncoder.encode(verificationId, Charsets.UTF_8.name())
        return "otp/$encodedId/$encodedPhone"
    }
}
