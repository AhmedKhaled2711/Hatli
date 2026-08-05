package com.app.hatli.feature.chat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.Routes
import com.app.hatli.feature.chat.presentation.ChatRoute

fun NavGraphBuilder.chatGraph(
    onBack: () -> Unit,
    onMessage: (String) -> Unit,
) {
    navigation(
        route = Routes.CHAT_GRAPH,
        startDestination = Routes.CHAT,
    ) {
        composable(
            route = Routes.CHAT,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType }),
        ) {
            ChatRoute(onBack = onBack, onMessage = onMessage)
        }
    }
}
