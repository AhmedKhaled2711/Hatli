package com.app.hatli.feature.chat.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.repository.UserRepository
import com.app.hatli.core.domain.usecase.request.MarkChatReadUseCase
import com.app.hatli.core.domain.usecase.request.ObserveMessagesUseCase
import com.app.hatli.core.domain.usecase.request.ObserveRequestUseCase
import com.app.hatli.core.domain.usecase.request.SendMessageUseCase
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val userRepository: UserRepository,
    observeMessages: ObserveMessagesUseCase,
    observeRequest: ObserveRequestUseCase,
    private val sendMessage: SendMessageUseCase,
    private val markChatRead: MarkChatReadUseCase,
) : BaseViewModel<ChatUiState, ChatUiEvent, ChatUiEffect>(
    ChatUiState(
        myUid = authRepository.currentUser()?.uid.orEmpty(),
    ),
) {
    private val requestId: String = checkNotNull(savedStateHandle["requestId"])
    private val cachedProfileNames = ConcurrentHashMap<String, String>()

    init {
        val currentUid = authRepository.currentUser()?.uid.orEmpty()

        combine(
            observeMessages(requestId),
            observeRequest(requestId),
            userRepository.observeProfile(currentUid),
        ) { messages, request, myProfile ->
            val isRequester = when {
                request != null && request.requesterId == currentUid -> true
                request != null && request.runnerId == currentUid -> false
                myProfile?.role == UserRole.Requester -> true
                myProfile?.role == UserRole.Runner -> false
                else -> true
            }

            val directCounterpartName = if (isRequester) {
                request?.runnerName
            } else {
                request?.requesterName
            }?.trim()?.takeIf { it.isNotBlank() }

            val counterpartUid = if (isRequester) {
                request?.runnerId
            } else {
                request?.requesterId
            }?.trim()?.takeIf { it.isNotBlank() }

            if (directCounterpartName == null && counterpartUid != null) {
                resolveCounterpartProfile(counterpartUid)
            }

            val resolvedName = directCounterpartName
                ?: counterpartUid?.let { cachedProfileNames[it] }

            val messageUis = messages.map { message ->
                val isMine = message.senderId == currentUid
                val senderLabel = when {
                    isMine -> "You"
                    !resolvedName.isNullOrBlank() -> resolvedName
                    isRequester -> "Runner"
                    else -> "Requester"
                }
                ChatMessageUi(
                    id = message.id,
                    text = message.text,
                    isMine = isMine,
                    senderLabel = senderLabel,
                )
            }

            Triple(messageUis, resolvedName, Pair(isRequester, request))
        }
            .onEach { (messageUis, resolvedName, roleAndReq) ->
                val (isRequester, request) = roleAndReq
                setState {
                    copy(
                        messages = messageUis,
                        participantName = resolvedName,
                        isRequester = isRequester,
                        requestStatus = request?.status,
                        isRequestLoading = request == null,
                    )
                }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            markChatRead(requestId)
        }
    }

    private fun resolveCounterpartProfile(uid: String) {
        if (cachedProfileNames.containsKey(uid)) return
        viewModelScope.launch {
            when (val result = userRepository.getProfile(uid)) {
                is Result.Success -> {
                    val name = result.data?.fullName?.trim()
                    if (!name.isNullOrBlank()) {
                        cachedProfileNames[uid] = name
                        setState {
                            if (participantName.isNullOrBlank()) {
                                copy(participantName = name)
                            } else {
                                this
                            }
                        }
                    }
                }
                else -> Unit
            }
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