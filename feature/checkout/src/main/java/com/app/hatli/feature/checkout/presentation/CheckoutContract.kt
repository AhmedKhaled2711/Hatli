package com.app.hatli.feature.checkout.presentation

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class CheckoutUiState(
    val title: String = "Checkout",
) : UiState

sealed interface CheckoutUiEvent : UiEvent {
    data object Back : CheckoutUiEvent
}

sealed interface CheckoutUiEffect : UiEffect {
    data object NavigateBack : CheckoutUiEffect
}
