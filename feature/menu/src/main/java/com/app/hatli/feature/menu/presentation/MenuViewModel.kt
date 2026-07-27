package com.app.hatli.feature.menu.presentation

import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() :
    BaseViewModel<MenuUiState, MenuUiEvent, MenuUiEffect>(MenuUiState()) {

    override fun handleEvent(event: MenuUiEvent) {
        when (event) {
            MenuUiEvent.Back -> sendEffect(MenuUiEffect.NavigateBack)
        }
    }
}
