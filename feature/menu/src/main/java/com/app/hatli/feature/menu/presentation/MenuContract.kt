package com.app.hatli.feature.menu.presentation

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class MenuUiState(
    val title: String = "Menu",
) : UiState

sealed interface MenuUiEvent : UiEvent {
    data object Back : MenuUiEvent
}

sealed interface MenuUiEffect : UiEffect {
    data object NavigateBack : MenuUiEffect
}
