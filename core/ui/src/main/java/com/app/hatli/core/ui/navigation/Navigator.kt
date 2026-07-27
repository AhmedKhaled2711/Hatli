package com.app.hatli.core.ui.navigation

import androidx.navigation.NavHostController

/**
 * Thin navigation facade — keeps NavController out of ViewModels when possible.
 * Prefer emitting UiEffect.Navigate and handling it in the Screen/NavHost.
 */
class Navigator(
    private val navController: NavHostController,
) {
    fun navigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateAndClearBackStack(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun popBackStack(): Boolean = navController.popBackStack()

    fun navigateUp(): Boolean = navController.navigateUp()
}
