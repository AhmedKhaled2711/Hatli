package com.app.hatli.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.usecase.GetPostsUseCase
import com.app.hatli.core.domain.usecase.ObservePostsUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPosts: GetPostsUseCase,
    observePosts: ObservePostsUseCase,
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(HomeUiState(isLoading = true)) {

    init {
        observePosts()
            .onEach { posts -> setState { copy(posts = posts, isLoading = false) } }
            .launchIn(viewModelScope)
        onEvent(HomeUiEvent.Refresh)
    }

    override fun handleEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Refresh -> refresh()
            HomeUiEvent.OpenAuth -> sendEffect(HomeUiEffect.NavigateToAuth)
            is HomeUiEvent.PostClicked ->
                sendEffect(HomeUiEffect.ShowMessage("Post ${event.id}"))
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            when (val result = getPosts()) {
                is Result.Success -> setState { copy(isLoading = false, posts = result.data) }
                is Result.Error -> setState {
                    copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Something went wrong",
                    )
                }
                Result.Loading -> setState { copy(isLoading = true) }
            }
        }
    }
}
