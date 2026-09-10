package com.app.hatli.feature.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.app.hatli.core.designsystem.component.auth.HatliAuthTopBar
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.feature.chat.components.ChatComposer
import com.app.hatli.feature.chat.components.ChatEmptyState
import com.app.hatli.feature.chat.components.ChatMessageBubble

@Composable
fun ChatScreen(
    state: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
    onBack: () -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom =  HatliSpacing.lg)
            .background(MaterialTheme.colorScheme.background),
    ) {

        HatliAuthTopBar(
            title = "Chat",
            onBack = onBack,
            modifier = Modifier.statusBarsPadding(),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            if (state.messages.isEmpty()) {
                ChatEmptyState(
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = HatliSpacing.md,
                        vertical = HatliSpacing.lg,
                    ),
                    verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                ) {
                    items(
                        items = state.messages,
                        key = { it.id },
                    ) { message ->
                        ChatMessageBubble(
                            text = message.text,
                            isMine = message.isMine,
                            senderLabel = message.senderLabel,
                        )
                    }
                }
            }
        }

        ChatComposer(
            value = state.draft,
            isSending = state.isSending,
            onValueChange = {
                onEvent(ChatUiEvent.DraftChanged(it))
            },
            onSend = {
                onEvent(ChatUiEvent.Send)
            },
        )
    }
}
