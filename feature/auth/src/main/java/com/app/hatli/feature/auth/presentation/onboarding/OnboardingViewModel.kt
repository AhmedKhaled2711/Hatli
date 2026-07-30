package com.app.hatli.feature.auth.presentation.onboarding

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.domain.usecase.user.SetOnboardingCompletedUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setOnboardingCompleted: SetOnboardingCompletedUseCase,
) : BaseViewModel<OnboardingUiState, OnboardingUiEvent, OnboardingUiEffect>(OnboardingUiState()) {

    override fun handleEvent(event: OnboardingUiEvent) {
        when (event) {
            OnboardingUiEvent.Next -> {
                val state = uiState.value
                if (state.isLast) complete()
                else setState { copy(pageIndex = pageIndex + 1) }
            }
            is OnboardingUiEvent.PageSelected -> {
                val clamped = event.index.coerceIn(0, uiState.value.pages.lastIndex)
                if (clamped != uiState.value.pageIndex) {
                    setState { copy(pageIndex = clamped) }
                }
            }
            OnboardingUiEvent.Skip,
            OnboardingUiEvent.GetStarted,
            -> complete()
        }
    }

    private fun complete() {
        viewModelScope.launch {
            setOnboardingCompleted()
            sendEffect(OnboardingUiEffect.GoLogin)
        }
    }
}
