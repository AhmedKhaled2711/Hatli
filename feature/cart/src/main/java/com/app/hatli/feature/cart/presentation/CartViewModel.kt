package com.app.hatli.feature.cart.presentation

import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() :
    BaseViewModel<CartUiState, CartUiEvent, CartUiEffect>(CartUiState()) {

    override fun handleEvent(event: CartUiEvent) {
        when (event) {
            CartUiEvent.Back -> sendEffect(CartUiEffect.NavigateBack)
        }
    }
}
