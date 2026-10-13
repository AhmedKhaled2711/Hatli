package com.app.hatli.feature.auth.presentation.profile

import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.feature.HatliFeatureFlags
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.usecase.auth.GetCurrentAuthUserUseCase
import com.app.hatli.core.domain.usecase.user.CreateUserProfileUseCase
import com.app.hatli.core.domain.usecase.user.GetPendingRoleUseCase
import com.app.hatli.core.model.CreateProfileInput
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.UserAddress
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
            setState { copy(role = role, phone = phone, step = ProfileSetupStep.Personal) }
        }
    }

    override fun handleEvent(event: CreateProfileUiEvent) {
        when (event) {
            is CreateProfileUiEvent.FirstName -> setState { copy(firstName = event.value, errorMessage = null) }
            is CreateProfileUiEvent.LastName -> setState { copy(lastName = event.value, errorMessage = null) }
            is CreateProfileUiEvent.Email -> setState { copy(email = event.value, errorMessage = null) }
            is CreateProfileUiEvent.GenderSelected -> setState { copy(gender = event.value, errorMessage = null) }
            is CreateProfileUiEvent.City -> setState { copy(city = event.value, errorMessage = null) }
            is CreateProfileUiEvent.Area -> setState { copy(area = event.value, errorMessage = null) }
            is CreateProfileUiEvent.Street -> setState { copy(street = event.value, errorMessage = null) }
            is CreateProfileUiEvent.Building -> setState { copy(building = event.value, errorMessage = null) }
            is CreateProfileUiEvent.Floor -> setState { copy(floor = event.value, errorMessage = null) }
            is CreateProfileUiEvent.Apartment -> setState { copy(apartment = event.value, errorMessage = null) }
            is CreateProfileUiEvent.AddressNotes -> setState { copy(addressNotes = event.value, errorMessage = null) }
            is CreateProfileUiEvent.NationalId -> setState { copy(nationalId = event.value, errorMessage = null) }
            is CreateProfileUiEvent.IdImage -> setState { copy(idImageUri = event.uri, errorMessage = null) }
            is CreateProfileUiEvent.Selfie -> setState { copy(selfieUri = event.uri, errorMessage = null) }
            is CreateProfileUiEvent.Transport -> setState { copy(transportType = event.value, errorMessage = null) }
            is CreateProfileUiEvent.VehicleInfo -> setState { copy(vehicleInfo = event.value, errorMessage = null) }
            CreateProfileUiEvent.Next -> goNext()
            CreateProfileUiEvent.Back -> goBack()
            CreateProfileUiEvent.Submit -> submit()
        }
    }

    private fun goNext() {
        val state = uiState.value
        val error = validateStep(state)
        if (error != null) {
            setState { copy(errorMessage = error) }
            return
        }
        val steps = state.steps
        val nextIndex = state.stepIndex + 1
        if (nextIndex > steps.lastIndex) {
            submit()
            return
        }
        setState { copy(step = steps[nextIndex], errorMessage = null) }
    }

    private fun goBack() {
        val state = uiState.value
        if (state.isFirstStep || state.isLoading) return
        val prev = state.steps.getOrNull(state.stepIndex - 1) ?: return
        setState { copy(step = prev, errorMessage = null) }
    }

    private val isAr: Boolean get() = java.util.Locale.getDefault().language == "ar"

    private fun validateStep(state: CreateProfileUiState): String? = when (state.step) {
        ProfileSetupStep.Personal -> when {
            state.firstName.isBlank() || state.lastName.isBlank() ->
                if (isAr) "الاسم الأول واسم العائلة مطلوبان." else "First and last name are required."
            !state.email.matches(EMAIL_REGEX) ->
                if (isAr) "يرجى إدخال بريد إلكتروني صحيح." else "Enter a valid email address."
            state.phone.isBlank() ->
                if (isAr) "رقم الهاتف مطلوب." else "Phone number is required."
            state.gender == null ->
                if (isAr) "يرجى تحديد النوع (ذكر أو أنثى)." else "Please select your gender."
            else -> null
        }
        ProfileSetupStep.Address -> when {
            state.city.isBlank() || state.area.isBlank() ||
                state.street.isBlank() || state.building.isBlank() ->
                if (isAr) "المدينة والمنطقة والشارع ورقم المبنى بيانات مطلوبة." else "City, area, street, and building are required."
            else -> null
        }
        ProfileSetupStep.Transport -> when {
            state.transportType == null ->
                if (isAr) "يرجى اختيار وسيلة التوصيل." else "Select how you deliver."
            state.transportType != TransportType.Walking && state.vehicleInfo.isBlank() ->
                if (isAr) "يرجى إضافة وصف مختصر للمركبة." else "Add a short vehicle description."
            else -> null
        }
        ProfileSetupStep.Verification -> when {
            state.nationalId.isBlank() ->
                if (isAr) "رقم الهوية الوطنية مطلوب." else "National ID is required."
            HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED &&
                (state.idImageUri.isNullOrBlank() || state.selfieUri.isNullOrBlank()) ->
                if (isAr) "صورة الهوية والصورة الشخصية مطلوبة." else "ID image and selfie are required."
            else -> null
        }
    }

    private fun submit() {
        val s = uiState.value
        val stepError = validateStep(s)
        if (stepError != null) {
            setState { copy(errorMessage = stepError) }
            return
        }
        // Ensure prior steps still valid before network write.
        for (step in s.steps) {
            val err = validateStep(s.copy(step = step))
            if (err != null) {
                setState { copy(step = step, errorMessage = err) }
                return
            }
        }
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            val gender = s.gender
            if (gender == null) {
                setState {
                    copy(
                        isLoading = false,
                        errorMessage = if (isAr) "يرجى تحديد النوع (ذكر أو أنثى)." else "Please select your gender.",
                    )
                }
                return@launch
            }
            val uploadsEnabled = HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED
            val input = CreateProfileInput(
                role = s.role,
                firstName = s.firstName,
                lastName = s.lastName,
                email = s.email,
                phone = s.phone,
                gender = gender,
                address = UserAddress(
                    city = s.city,
                    area = s.area,
                    street = s.street,
                    building = s.building,
                    floor = s.floor.ifBlank { null },
                    apartment = s.apartment.ifBlank { null },
                    notes = s.addressNotes.ifBlank { null },
                    latitude = null,
                    longitude = null,
                ),
                nationalId = s.nationalId.ifBlank { null },
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

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    }
}
