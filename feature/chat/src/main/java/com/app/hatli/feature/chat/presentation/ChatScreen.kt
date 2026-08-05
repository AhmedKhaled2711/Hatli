package com.app.hatli.feature.chat.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.usecase.request.MarkChatReadUseCase
import com.app.hatli.core.domain.usecase.request.ObserveMessagesUseCase
import com.app.hatli.core.domain.usecase.request.SendMessageUseCase
import com.app.hatli.core.model.ChatMessage
import com.app.hatli.core.ui.base.BaseViewModel
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val draft: String = "",
    val isSending: Boolean = false,
    val myUid: String = "",
) : UiState

sealed interface ChatUiEvent : UiEvent {
    data class DraftChanged(val value: String) : ChatUiEvent
    data object Send : ChatUiEvent
    data object Back : ChatUiEvent
}

sealed interface ChatUiEffect : UiEffect {
    data object NavigateBack : ChatUiEffect
    data class ShowMessage(val message: String) : ChatUiEffect
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    observeMessages: ObserveMessagesUseCase,
    private val sendMessage: SendMessageUseCase,
    private val markChatRead: MarkChatReadUseCase,
) : BaseViewModel<ChatUiState, ChatUiEvent, ChatUiEffect>(
    ChatUiState(myUid = authRepository.currentUser()?.uid.orEmpty()),
) {
    private val requestId: String = checkNotNull(savedStateHandle["requestId"])

    init {
        observeMessages(requestId)
            .onEach { list -> setState { copy(messages = list) } }
            .launchIn(viewModelScope)
        viewModelScope.launch { markChatRead(requestId) }
    }

    override fun handleEvent(event: ChatUiEvent) {
        when (event) {
            is ChatUiEvent.DraftChanged -> setState { copy(draft = event.value) }
            ChatUiEvent.Send -> {
                val text = uiState.value.draft
                if (text.isBlank()) return
                viewModelScope.launch {
                    setState { copy(isSending = true) }
                    when (
                        val result = sendMessage(requestId, text, UUID.randomUUID().toString())
                    ) {
                        is Result.Success -> setState { copy(isSending = false, draft = "") }
                        is Result.Error -> {
                            setState { copy(isSending = false) }
                            sendEffect(ChatUiEffect.ShowMessage(result.userMessage()))
                        }
                        Result.Loading -> Unit
                    }
                }
            }
            ChatUiEvent.Back -> sendEffect(ChatUiEffect.NavigateBack)
        }
    }
}

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
    ChatScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun ChatScreen(
    state: ChatUiState,
    onEvent: (ChatUiEvent) -> Unit,
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
            .systemBarsPadding()
            .padding(HatliSpacing.md),
    ) {
        Text(
            text = "Chat",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = HatliSpacing.sm),
        )
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = HatliSpacing.sm),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        ) {
            if (state.messages.isEmpty()) {
                item {
                    Text(
                        text = "اسأل عن تفاصيل الطلب",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            items(state.messages, key = { it.id }) { msg ->
                val mine = msg.senderId == state.myUid
                HatliCard {
                    Text(
                        text = if (mine) "You" else "Them",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(msg.text, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        HatliTextField(
            value = state.draft,
            onValueChange = { onEvent(ChatUiEvent.DraftChanged(it)) },
            label = "Message",
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        ) {
            HatliSecondaryButton(
                text = "Back",
                onClick = { onEvent(ChatUiEvent.Back) },
                modifier = Modifier.weight(1f),
            )
            HatliPrimaryButton(
                text = if (state.isSending) "…" else "Send",
                onClick = { onEvent(ChatUiEvent.Send) },
                enabled = !state.isSending && state.draft.isNotBlank(),
                modifier = Modifier.weight(1f),
            )
        }
    }
}
