package com.app.hatli.feature.profile.presentation

import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() :
    BaseViewModel<ProfileUiState, ProfileUiEvent, ProfileUiEffect>(ProfileUiState()) {

    override fun handleEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.Back -> sendEffect(ProfileUiEffect.NavigateBack)
        }
    }
}
