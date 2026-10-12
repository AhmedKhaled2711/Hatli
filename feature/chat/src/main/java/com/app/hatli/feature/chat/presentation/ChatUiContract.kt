package com.app.hatli.feature.chat.presentation

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.model.ChatMessage
import com.app.hatli.core.ui.base.UiState

import com.app.hatli.core.model.RequestStatus

sealed interface ChatUiEffect : UiEffect {
    data object NavigateBack : ChatUiEffect
    data class ShowMessage(val message: String) : ChatUiEffect
}


sealed interface ChatUiEvent : UiEvent {
    data class DraftChanged(val value: String) : ChatUiEvent
    data object Send : ChatUiEvent
    data object Back : ChatUiEvent
}


data class ChatUiState(
    val messages: List<ChatMessageUi> = emptyList(),
    val draft: String = "",
    val isSending: Boolean = false,
    val myUid: String = "",
    val participantName: String? = null,
    val isRequester: Boolean = false,
    val requestStatus: RequestStatus? = null,
    val isRequestLoading: Boolean = true,
) : UiState


data class ChatMessageUi(
    val id: String,
    val text: String,
    val isMine: Boolean,
    val senderLabel: String,
)
