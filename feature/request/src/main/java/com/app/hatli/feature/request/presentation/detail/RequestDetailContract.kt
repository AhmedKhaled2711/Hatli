package com.app.hatli.feature.request.presentation.detail

import com.app.hatli.core.model.Request
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class RequestDetailUiState(
    val isLoading: Boolean = true,
    val request: Request? = null,
    val isRequester: Boolean = false,
    val isRunner: Boolean = false,
    val runnerNextCta: String? = null,
    val cancelReason: String = "",
    val showCancelSheet: Boolean = false,
    val isCancelling: Boolean = false,
    val isConfirming: Boolean = false,
    val isAdvancing: Boolean = false,
    val errorMessage: String? = null,
) : UiState

sealed interface RequestDetailUiEvent : UiEvent {
    data object Refresh : RequestDetailUiEvent
    data object OpenChat : RequestDetailUiEvent
    data object OpenRate : RequestDetailUiEvent
    data object AdvanceStatus : RequestDetailUiEvent
    data object ConfirmDelivery : RequestDetailUiEvent
    data object ShowCancel : RequestDetailUiEvent
    data object HideCancel : RequestDetailUiEvent
    data class CancelReasonChanged(val value: String) : RequestDetailUiEvent
    data object ConfirmCancel : RequestDetailUiEvent
    data object Back : RequestDetailUiEvent
}

sealed interface RequestDetailUiEffect : UiEffect {
    data object NavigateBack : RequestDetailUiEffect
    data class NavigateChat(val requestId: String) : RequestDetailUiEffect
    data class NavigateRate(val requestId: String) : RequestDetailUiEffect
    data class ShowMessage(val message: String) : RequestDetailUiEffect
}
