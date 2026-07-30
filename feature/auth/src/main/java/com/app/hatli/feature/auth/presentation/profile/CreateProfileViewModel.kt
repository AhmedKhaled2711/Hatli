package com.app.hatli.feature.auth.presentation.profile

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.feature.HatliFeatureFlags
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.usecase.auth.GetCurrentAuthUserUseCase
import com.app.hatli.core.domain.usecase.user.CreateUserProfileUseCase
import com.app.hatli.core.domain.usecase.user.GetPendingRoleUseCase
import com.app.hatli.core.model.CreateProfileInput
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val getPendingRole: GetPendingRoleUseCase,
    private val getCurrentAuthUser: GetCurrentAuthUserUseCase,
    private val createUserProfile: CreateUserProfileUseCase,
) : BaseViewModel<CreateProfileUiState, CreateProfileUiEvent, CreateProfileUiEffect>(
    CreateProfileUiState(),
) {

    init {
        viewModelScope.launch {
            val role = getPendingRole() ?: UserRole.Requester
            val phone = getCurrentAuthUser()?.phoneNumber.orEmpty()
            setState { copy(role = role, phone = phone) }
        }
    }

    override fun handleEvent(event: CreateProfileUiEvent) {
        when (event) {
            is CreateProfileUiEvent.FirstName -> setState { copy(firstName = event.value) }
            is CreateProfileUiEvent.LastName -> setState { copy(lastName = event.value) }
            is CreateProfileUiEvent.Email -> setState { copy(email = event.value) }
            is CreateProfileUiEvent.GenderSelected -> setState { copy(gender = event.value) }
            is CreateProfileUiEvent.NationalId -> setState { copy(nationalId = event.value) }
            is CreateProfileUiEvent.IdImage -> setState { copy(idImageUri = event.uri) }
            is CreateProfileUiEvent.Selfie -> setState { copy(selfieUri = event.uri) }
            is CreateProfileUiEvent.Transport -> setState { copy(transportType = event.value) }
            is CreateProfileUiEvent.VehicleInfo -> setState { copy(vehicleInfo = event.value) }
            CreateProfileUiEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val s = uiState.value
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            val uploadsEnabled = HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED
            val input = CreateProfileInput(
                role = s.role,
                firstName = s.firstName,
                lastName = s.lastName,
                email = s.email,
                phone = s.phone,
                gender = s.gender,
                nationalId = s.nationalId.ifBlank { null },
                // Local URIs only matter when Storage uploads are enabled (Blaze).
                idImageLocalUri = if (uploadsEnabled) s.idImageUri else null,
                selfieLocalUri = if (uploadsEnabled) s.selfieUri else null,
                transportType = s.transportType,
                vehicleInfo = s.vehicleInfo.ifBlank { null },
            )
            when (val result = createUserProfile(input)) {
                is Result.Success -> {
                    setState { copy(isLoading = false) }
                    sendEffect(CreateProfileUiEffect.GoHome)
                }
                is Result.Error -> setState {
                    copy(isLoading = false, errorMessage = result.userMessage())
                }
                Result.Loading -> Unit
            }
        }
    }
}
