package com.app.hatli.feature.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun ChatRoute(
    onBack: () -> Unit,
    onMessage: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            ChatUiEffect.NavigateBack -> onBack()
            is ChatUiEffect.ShowMessage -> onMessage(effect.message)
        }
    }

    ChatScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
    )
}