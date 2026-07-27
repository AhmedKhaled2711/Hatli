package com.app.hatli.core.ui.navigation

/**
 * Type-safe route contract. Feature modules expose Destinations; app wires graphs.
 */
interface Destination {
    val route: String
}

object Routes {
    const val HOME_GRAPH = "home_graph"
    const val AUTH_GRAPH = "auth_graph"
    const val MENU_GRAPH = "menu_graph"
    const val CART_GRAPH = "cart_graph"
    const val CHECKOUT_GRAPH = "checkout_graph"
    const val ORDERS_GRAPH = "orders_graph"
    const val PROFILE_GRAPH = "profile_graph"

    const val HOME = "home"
    const val AUTH = "auth"
    const val MENU = "menu"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val ORDERS = "orders"
    const val PROFILE = "profile"

    const val DEEP_LINK_HOST = "hatli.app"
    const val DEEP_LINK_SCHEME = "https"
}
