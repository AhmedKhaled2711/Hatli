package com.app.hatli.feature.auth.presentation.role

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.domain.usecase.user.SetPendingRoleUseCase
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoleViewModel @Inject constructor(
    private val setPendingRole: SetPendingRoleUseCase,
) : BaseViewModel<RoleUiState, RoleUiEvent, RoleUiEffect>(RoleUiState()) {

    private var continueJob: kotlinx.coroutines.Job? = null

    override fun handleEvent(event: RoleUiEvent) {
        when (event) {
            is RoleUiEvent.Select -> setState { copy(selected = event.role) }
            RoleUiEvent.Continue -> {
                if (continueJob?.isActive == true) return
                val role = uiState.value.selected ?: return
                continueJob = viewModelScope.launch {
                    setPendingRole(role)
                    sendEffect(RoleUiEffect.GoCreateProfile)
                }
            }
        }
    }
}
