package com.app.hatli.feature.orders.presentation

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class OrdersUiState(
    val title: String = "Orders",
) : UiState

sealed interface OrdersUiEvent : UiEvent {
    data object Back : OrdersUiEvent
}

sealed interface OrdersUiEffect : UiEffect {
    data object NavigateBack : OrdersUiEffect
}
