package com.app.hatli.feature.chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.app.hatli.core.designsystem.component.auth.HatliAuthTopBar
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.feature.chat.R
import com.app.hatli.feature.chat.components.ChatComposer
import com.app.hatli.feature.chat.components.ChatEmptyState
import com.app.hatli.feature.chat.components.ChatMessageBubble

private val SuccessGreen = Color(0xFF0F9F55)

@OptIn(ExperimentalLayoutApi::class)
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

    val isImeVisible = WindowInsets.isImeVisible
    LaunchedEffect(isImeVisible) {
        if (isImeVisible && state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    val counterpartName = state.participantName?.takeIf { it.isNotBlank() }
        ?: if (state.isRequester) {
            stringResource(R.string.chat_counterpart_runner_fallback)
        } else {
            stringResource(R.string.chat_counterpart_client_fallback)
        }

    val statusLabel = when (state.requestStatus) {
        RequestStatus.Pending -> stringResource(R.string.chat_status_pending)
        RequestStatus.Accepted -> stringResource(R.string.chat_status_accepted)
        RequestStatus.InProgress -> stringResource(R.string.chat_status_in_progress)
        RequestStatus.Purchased -> stringResource(R.string.chat_status_purchased)
        RequestStatus.OnTheWay -> stringResource(R.string.chat_status_on_the_way)
        RequestStatus.Delivered -> stringResource(R.string.chat_status_delivered)
        RequestStatus.Completed -> stringResource(R.string.chat_status_completed)
        RequestStatus.Cancelled -> stringResource(R.string.chat_status_cancelled)
        null -> if (state.isRequestLoading) {
            stringResource(R.string.chat_status_loading)
        } else {
            ""
        }
    }

    val statusColor = when (state.requestStatus) {
        RequestStatus.Pending -> MaterialTheme.colorScheme.tertiary
        RequestStatus.Accepted, RequestStatus.InProgress, RequestStatus.Purchased -> MaterialTheme.colorScheme.primary
        RequestStatus.OnTheWay -> MaterialTheme.colorScheme.secondary
        RequestStatus.Delivered, RequestStatus.Completed -> SuccessGreen
        RequestStatus.Cancelled -> MaterialTheme.colorScheme.error
        null -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.navigationBars)),
        ) {
            HatliAuthTopBar(
                title = counterpartName,
                subtitle = statusLabel.takeIf { it.isNotBlank() },
                subtitleColor = statusColor,
                statusIndicatorColor = statusColor.takeIf { state.requestStatus != null },
                onBack = onBack,
                backContentDescription = stringResource(R.string.chat_back),
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
                            vertical = HatliSpacing.md,
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
}
