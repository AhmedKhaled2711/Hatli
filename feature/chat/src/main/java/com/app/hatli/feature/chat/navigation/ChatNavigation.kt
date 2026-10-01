package com.app.hatli.feature.chat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.hatli.core.ui.navigation.ChatGraph
import com.app.hatli.core.ui.navigation.ChatRoute as ChatRouteKey
import com.app.hatli.feature.chat.presentation.ChatRoute

fun NavGraphBuilder.chatGraph(
    onBack: () -> Unit,
    onMessage: (String) -> Unit,
) {
    navigation<ChatGraph>(
        startDestination = ChatRouteKey(""),
    ) {
        composable<ChatRouteKey> {
            ChatRoute(onBack = onBack, onMessage = onMessage)
        }
    }
}
