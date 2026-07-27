package com.app.hatli.feature.auth.presentation.profile

import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

data class CreateProfileUiState(
    val role: UserRole = UserRole.Requester,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val gender: Gender = Gender.PreferNotToSay,
    val nationalId: String = "",
    val idImageUri: String? = null,
    val selfieUri: String? = null,
    val transportType: TransportType? = null,
    val vehicleInfo: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val isRunner: Boolean get() = role == UserRole.Runner
}

sealed interface CreateProfileUiEvent : UiEvent {
    data class FirstName(val value: String) : CreateProfileUiEvent
    data class LastName(val value: String) : CreateProfileUiEvent
    data class Email(val value: String) : CreateProfileUiEvent
    data class GenderSelected(val value: Gender) : CreateProfileUiEvent
    data class NationalId(val value: String) : CreateProfileUiEvent
    data class IdImage(val uri: String) : CreateProfileUiEvent
    data class Selfie(val uri: String) : CreateProfileUiEvent
    data class Transport(val value: TransportType) : CreateProfileUiEvent
    data class VehicleInfo(val value: String) : CreateProfileUiEvent
    data object Submit : CreateProfileUiEvent
}

sealed interface CreateProfileUiEffect : UiEffect {
    data object GoHome : CreateProfileUiEffect
}
