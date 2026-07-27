package com.app.hatli.feature.profile.presentation

import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class ProfileUiState(
    val title: String = "Profile",
) : UiState

sealed interface ProfileUiEvent : UiEvent {
    data object Back : ProfileUiEvent
}

sealed interface ProfileUiEffect : UiEffect {
    data object NavigateBack : ProfileUiEffect
}
