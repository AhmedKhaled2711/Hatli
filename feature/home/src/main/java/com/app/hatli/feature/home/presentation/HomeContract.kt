package com.app.hatli.feature.home.presentation

import com.app.hatli.core.model.Post
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class HomeUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val errorMessage: String? = null,
) : UiState

sealed interface HomeUiEvent : UiEvent {
    data object Refresh : HomeUiEvent
    data object OpenAuth : HomeUiEvent
    data class PostClicked(val id: Int) : HomeUiEvent
}

sealed interface HomeUiEffect : UiEffect {
    data object NavigateToAuth : HomeUiEffect
    data class ShowMessage(val message: String) : HomeUiEffect
}
