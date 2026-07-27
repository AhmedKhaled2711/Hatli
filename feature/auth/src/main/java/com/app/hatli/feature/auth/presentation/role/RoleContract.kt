package com.app.hatli.feature.auth.presentation.role

import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class RoleUiState(
    val selected: UserRole? = null,
) : UiState

sealed interface RoleUiEvent : UiEvent {
    data class Select(val role: UserRole) : RoleUiEvent
    data object Continue : RoleUiEvent
}

sealed interface RoleUiEffect : UiEffect {
    data object GoCreateProfile : RoleUiEffect
}
