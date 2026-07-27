package com.app.hatli.feature.cart.presentation

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class CartUiState(
    val title: String = "Cart",
) : UiState

sealed interface CartUiEvent : UiEvent {
    data object Back : CartUiEvent
}

sealed interface CartUiEffect : UiEffect {
    data object NavigateBack : CartUiEffect
}
