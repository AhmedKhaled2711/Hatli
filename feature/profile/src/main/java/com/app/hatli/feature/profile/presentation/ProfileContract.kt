package com.app.hatli.feature.profile.presentation

import com.app.hatli.core.model.AppThemeMode
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile? = null,
    val errorMessage: String? = null,
    val themeMode: AppThemeMode = AppThemeMode.FollowSystem,
    val appLanguage: String = "ar",
) : UiState

sealed interface ProfileUiEvent : UiEvent {
    data object Refresh : ProfileUiEvent
    data object Logout : ProfileUiEvent
    data class SetThemeMode(val themeMode: AppThemeMode) : ProfileUiEvent
    data class SetAppLanguage(val languageCode: String) : ProfileUiEvent
}

sealed interface ProfileUiEffect : UiEffect {
    data object LoggedOut : ProfileUiEffect
    data class AppLanguageChanged(val languageCode: String) : ProfileUiEffect
}
