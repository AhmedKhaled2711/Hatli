package com.app.hatli.feature.profile.presentation

import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val errorMessage: String? = null,
) : UiState

sealed interface ProfileUiEvent : UiEvent {
    data object Refresh : ProfileUiEvent
    data object Logout : ProfileUiEvent
}

sealed interface ProfileUiEffect : UiEffect {
    data object LoggedOut : ProfileUiEffect
}
