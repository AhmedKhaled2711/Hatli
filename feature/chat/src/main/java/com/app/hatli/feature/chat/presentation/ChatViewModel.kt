package com.app.hatli.feature.chat.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.repository.UserRepository
import com.app.hatli.core.domain.usecase.request.MarkChatReadUseCase
import com.app.hatli.core.domain.usecase.request.ObserveMessagesUseCase
import com.app.hatli.core.domain.usecase.request.SendMessageUseCase
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    userRepository: UserRepository,
    observeMessages: ObserveMessagesUseCase,
    private val sendMessage: SendMessageUseCase,
    private val markChatRead: MarkChatReadUseCase,
) : BaseViewModel<ChatUiState, ChatUiEvent, ChatUiEffect>(
    ChatUiState(
        myUid = authRepository.currentUser()?.uid.orEmpty(),
    ),
){
    private val requestId: String = checkNotNull(savedStateHandle["requestId"])

    init {
        val currentUid = authRepository.currentUser()?.uid.orEmpty()

        combine(
            observeMessages(requestId),
            userRepository.observeProfile(currentUid),
        ) { messages, profile ->
            messages.map { message ->
                ChatMessageUi(
                    id = message.id,
                    text = message.text,
                    isMine = message.senderId == currentUid,
                    senderLabel = when {
                        message.senderId == currentUid -> "You"

                        profile?.role == UserRole.Requester -> "Runner"

                        profile?.role == UserRole.Runner -> "Requester"

                        else -> "User"
                    },
                )
            }
        }
            .onEach { messages ->
                setState {
                    copy(messages = messages)
                }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            markChatRead(requestId)
        }
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