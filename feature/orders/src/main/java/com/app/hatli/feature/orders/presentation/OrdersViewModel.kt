package com.app.hatli.feature.orders.presentation

import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor() :
    BaseViewModel<OrdersUiState, OrdersUiEvent, OrdersUiEffect>(OrdersUiState()) {

    override fun handleEvent(event: OrdersUiEvent) {
        when (event) {
            OrdersUiEvent.Back -> sendEffect(OrdersUiEffect.NavigateBack)
        }
    }
}
