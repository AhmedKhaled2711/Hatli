package com.app.hatli.feature.auth.presentation.profile

import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

enum class ProfileSetupStep {
    Personal,
    Address,
    Transport,
    Verification,
}

data class CreateProfileUiState(
    val role: UserRole = UserRole.Requester,
    val step: ProfileSetupStep = ProfileSetupStep.Personal,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val gender: Gender? = null,
    val city: String = "",
    val area: String = "",
    val street: String = "",
    val building: String = "",
    val floor: String = "",
    val apartment: String = "",
    val addressNotes: String = "",
    val nationalId: String = "",
    val idImageUri: String? = null,
    val selfieUri: String? = null,
    val transportType: TransportType? = null,
    val vehicleInfo: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val isRunner: Boolean get() = role == UserRole.Runner

    val steps: List<ProfileSetupStep>
        get() = if (isRunner) {
            listOf(
                ProfileSetupStep.Personal,
                ProfileSetupStep.Address,
                ProfileSetupStep.Transport,
                ProfileSetupStep.Verification,
            )
        } else {
            listOf(
                ProfileSetupStep.Personal,
                ProfileSetupStep.Address,
            )
        }

    val stepIndex: Int get() = steps.indexOf(step).coerceAtLeast(0)
    val totalSteps: Int get() = steps.size
    val stepNumber: Int get() = stepIndex + 1
    val progress: Float get() = stepNumber.toFloat() / totalSteps.toFloat()
    val isFirstStep: Boolean get() = stepIndex <= 0
    val isLastStep: Boolean get() = stepIndex >= steps.lastIndex

    val stepTitle: String
        get() = when (step) {
            ProfileSetupStep.Personal -> "Personal information"
            ProfileSetupStep.Address -> "Your address"
            ProfileSetupStep.Transport -> "How do you deliver?"
            ProfileSetupStep.Verification -> "Verification"
        }

    val stepDescription: String
        get() = when (step) {
            ProfileSetupStep.Personal -> "Tell us a bit about yourself so we can personalize your experience."
            ProfileSetupStep.Address -> "Add your address for smoother pickups and deliveries. Maps coming later."
            ProfileSetupStep.Transport -> "Choose the transport you use on the road. You can update this later."
            ProfileSetupStep.Verification -> "Complete your identity details. Photo verification arrives in a future update."
        }
}

sealed interface CreateProfileUiEvent : UiEvent {
    data class FirstName(val value: String) : CreateProfileUiEvent
    data class LastName(val value: String) : CreateProfileUiEvent
    data class Email(val value: String) : CreateProfileUiEvent
    data class GenderSelected(val value: Gender) : CreateProfileUiEvent
    data class City(val value: String) : CreateProfileUiEvent
    data class Area(val value: String) : CreateProfileUiEvent
    data class Street(val value: String) : CreateProfileUiEvent
    data class Building(val value: String) : CreateProfileUiEvent
    data class Floor(val value: String) : CreateProfileUiEvent
    data class Apartment(val value: String) : CreateProfileUiEvent
    data class AddressNotes(val value: String) : CreateProfileUiEvent
    data class NationalId(val value: String) : CreateProfileUiEvent
    data class IdImage(val uri: String) : CreateProfileUiEvent
    data class Selfie(val uri: String) : CreateProfileUiEvent
    data class Transport(val value: TransportType) : CreateProfileUiEvent
    data class VehicleInfo(val value: String) : CreateProfileUiEvent
    data object Next : CreateProfileUiEvent
    data object Back : CreateProfileUiEvent
    data object Submit : CreateProfileUiEvent
}

sealed interface CreateProfileUiEffect : UiEffect {
    data object GoHome : CreateProfileUiEffect
}
