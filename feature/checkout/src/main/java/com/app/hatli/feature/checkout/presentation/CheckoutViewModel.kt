package com.app.hatli.feature.checkout.presentation

import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor() :
    BaseViewModel<CheckoutUiState, CheckoutUiEvent, CheckoutUiEffect>(CheckoutUiState()) {

    override fun handleEvent(event: CheckoutUiEvent) {
        when (event) {
            CheckoutUiEvent.Back -> sendEffect(CheckoutUiEffect.NavigateBack)
        }
    }
}
