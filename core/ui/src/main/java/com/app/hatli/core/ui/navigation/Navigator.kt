package com.app.hatli.core.ui.navigation

import androidx.navigation.NavHostController

/**
 * Thin navigation facade supporting Type-Safe Navigation.
 */
class Navigator(
    private val navController: NavHostController,
) {
    fun <T : Any> navigate(route: T) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun <T : Any> navigateAndClearBackStack(route: T) {
        navController.navigate(route) {
            popUpTo(navController.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun popBackStack(): Boolean = navController.popBackStack()

    fun navigateUp(): Boolean = navController.navigateUp()
}
