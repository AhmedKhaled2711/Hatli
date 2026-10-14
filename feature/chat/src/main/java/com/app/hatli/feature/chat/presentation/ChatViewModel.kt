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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
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
    private var sendJob: Job? = null
    private val isAr: Boolean get() = java.util.Locale.getDefault().language == "ar"

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
                    isMine -> if (isAr) "أنت" else "You"
                    !resolvedName.isNullOrBlank() -> resolvedName
                    isRequester -> if (isAr) "الكابتن" else "Runner"
                    else -> if (isAr) "صاحب الطلب" else "Requester"
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
            .catch { e ->
                setState { copy(isRequestLoading = false) }
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
                if (uiState.value.isSending || sendJob?.isActive == true) return
                val text = uiState.value.draft.trim()
                if (text.isBlank()) return
                sendJob = viewModelScope.launch {
                    setState { copy(isSending = true) }
                    try {
                        when (
                            val result = sendMessage(requestId, text, UUID.randomUUID().toString())
                        ) {
                            is Result.Success -> setState { copy(draft = "") }
                            is Result.Error -> {
                                sendEffect(ChatUiEffect.ShowMessage(result.userMessage()))
                            }
                            Result.Loading -> Unit
                        }
                    } finally {
                        setState { copy(isSending = false) }
                    }
                }
            }
            ChatUiEvent.Back -> sendEffect(ChatUiEffect.NavigateBack)
        }
    }
}