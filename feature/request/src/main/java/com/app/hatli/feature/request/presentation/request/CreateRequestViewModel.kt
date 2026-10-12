package com.app.hatli.feature.request.presentation.request

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.domain.usecase.request.CreateRequestUseCase
import com.app.hatli.core.domain.usecase.user.GetUserProfileUseCase
import com.app.hatli.core.model.BillsDetails
import com.app.hatli.core.model.CreateRequestInput
import com.app.hatli.core.model.CustomDetails
import com.app.hatli.core.model.PickupDetails
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.ShoppingDetails
import com.app.hatli.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRequestViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createRequest: CreateRequestUseCase,
    private val getUserProfile: GetUserProfileUseCase,
) : BaseViewModel<CreateRequestUiState, CreateRequestUiEvent, CreateRequestUiEffect>(
    CreateRequestUiState(),
) {

    init {
        loadUserProfileAddress()
        val initialCategory: String? = savedStateHandle["initialCategory"]
        val mapped = mapInitialCategory(initialCategory)
        if (mapped != null) {
            val (category, subtype) = mapped
            setState {
                copy(
                    category = category,
                    step = CreateWizardStep.Details,
                    wasDirectCategory = true,
                    categorySubtype = subtype,
                    fieldError = null,
                )
            }
        }
    }

    private fun loadUserProfileAddress() {
        viewModelScope.launch {
            when (val result = getUserProfile()) {
                is Result.Success -> {
                    val address = result.data?.address
                    val formatted = address?.takeIf { !it.isEmpty }?.formattedDeliveryAddress()
                    if (!formatted.isNullOrBlank()) {
                        setState { copy(savedDeliveryAddress = formatted) }
                    }
                }
                else -> Unit
            }
        }
    }

    private fun mapInitialCategory(raw: String?): Pair<RequestCategory, String?>? = when (raw?.lowercase()) {
        "grocery" -> RequestCategory.Shopping to "grocery"
        "pharmacy" -> RequestCategory.Shopping to "pharmacy"
        "courier", "pickup" -> RequestCategory.Pickup to "courier"
        "bills" -> RequestCategory.Bills to "bills"
        "custom_errand", "custom" -> RequestCategory.Custom to "custom"
        "shopping" -> RequestCategory.Shopping to null
        else -> null
    }

    override fun handleEvent(event: CreateRequestUiEvent) {
        when (event) {
            is CreateRequestUiEvent.CategorySelected -> setState {
                copy(
                    category = event.category,
                    step = CreateWizardStep.Details,
                    wasDirectCategory = false,
                    categorySubtype = null,
                    fieldError = null,
                )
            }
            CreateRequestUiEvent.ChangeCategory -> setState {
                copy(
                    step = CreateWizardStep.Category,
                    wasDirectCategory = false,
                    fieldError = null,
                )
            }
            CreateRequestUiEvent.Back -> onBack()
            CreateRequestUiEvent.Next -> onNext()
            CreateRequestUiEvent.Submit -> submit()
            is CreateRequestUiEvent.ShopNameChanged -> setState { copy(shopName = event.value) }
            is CreateRequestUiEvent.ItemsChanged -> setState { copy(itemsText = event.value, fieldError = null) }
            is CreateRequestUiEvent.NotesChanged -> setState { copy(notes = event.value) }
            is CreateRequestUiEvent.PickupAddressChanged ->
                setState { copy(pickupAddress = event.value, fieldError = null) }
            is CreateRequestUiEvent.DropoffAddressChanged ->
                setState { copy(dropoffAddress = event.value, fieldError = null) }
            is CreateRequestUiEvent.UseSavedAddress -> useSavedAddress(event.fallback)
            is CreateRequestUiEvent.PickupItemChanged ->
                setState { copy(pickupItemDescription = event.value, fieldError = null) }
            is CreateRequestUiEvent.BillTypeChanged ->
                setState { copy(billType = event.value, fieldError = null) }
            is CreateRequestUiEvent.AccountRefChanged ->
                setState { copy(accountRef = event.value, fieldError = null) }
            is CreateRequestUiEvent.BillAmountChanged ->
                setState { copy(billAmountText = event.value, fieldError = null) }
            is CreateRequestUiEvent.CustomTitleChanged ->
                setState { copy(customTitle = event.value, fieldError = null) }
            is CreateRequestUiEvent.CustomBodyChanged ->
                setState { copy(customBody = event.value, fieldError = null) }
            is CreateRequestUiEvent.BudgetChanged -> setState { copy(budgetText = event.value) }
            is CreateRequestUiEvent.DeliveryFeeChanged ->
                setState { copy(deliveryFeeText = event.value, fieldError = null) }
        }
    }

    private fun useSavedAddress(fallback: String) {
        val cached = uiState.value.savedDeliveryAddress
        if (!cached.isNullOrBlank()) {
            setState { copy(dropoffAddress = cached, fieldError = null) }
            return
        }
        viewModelScope.launch {
            when (val result = getUserProfile()) {
                is Result.Success -> {
                    val address = result.data?.address
                    val formatted = address?.takeIf { !it.isEmpty }?.formattedDeliveryAddress()
                    val targetAddress = if (!formatted.isNullOrBlank()) formatted else fallback
                    setState {
                        copy(
                            savedDeliveryAddress = formatted ?: savedDeliveryAddress,
                            dropoffAddress = if (targetAddress.isNotBlank()) targetAddress else dropoffAddress,
                            fieldError = null,
                        )
                    }
                }
                else -> {
                    if (fallback.isNotBlank()) {
                        setState { copy(dropoffAddress = fallback, fieldError = null) }
                    }
                }
            }
        }
    }

    private fun onBack() {
        val state = uiState.value
        when (state.step) {
            CreateWizardStep.Category -> sendEffect(CreateRequestUiEffect.NavigateBack)
            CreateWizardStep.Details -> {
                if (state.wasDirectCategory) {
                    sendEffect(CreateRequestUiEffect.NavigateBack)
                } else {
                    setState {
                        copy(step = CreateWizardStep.Category, category = null, fieldError = null)
                    }
                }
            }
            CreateWizardStep.FeeReview -> setState {
                copy(step = CreateWizardStep.Details, fieldError = null)
            }
        }
    }

    private fun onNext() {
        val state = uiState.value
        when (state.step) {
            CreateWizardStep.Category -> Unit
            CreateWizardStep.Details -> {
                val error = validateDetails(state)
                if (error != null) {
                    setState { copy(fieldError = error) }
                    return
                }
                setState { copy(step = CreateWizardStep.FeeReview, fieldError = null) }
            }
            CreateWizardStep.FeeReview -> submit()
        }
    }

    private fun validateDetails(state: CreateRequestUiState): String? = when (state.category) {
        RequestCategory.Shopping ->
            if (state.itemsText.isBlank()) "Enter what you need (shopping list)." else null
        RequestCategory.Pickup -> when {
            state.pickupAddress.isBlank() -> "Enter pickup address."
            state.dropoffAddress.isBlank() -> "Enter drop-off address."
            state.pickupItemDescription.isBlank() -> "Describe the item."
            else -> null
        }
        RequestCategory.Bills -> when {
            state.billType.isBlank() -> "Enter bill type."
            state.accountRef.isBlank() -> "Enter account / reference number."
            state.billAmountText.toDoubleOrNull() == null -> "Enter a valid amount."
            else -> null
        }
        RequestCategory.Custom -> when {
            state.customTitle.isBlank() -> "Enter a short title."
            state.customBody.isBlank() -> "Describe your request."
            else -> null
        }
        null -> "Select a category."
    }

    private fun submit() {
        val state = uiState.value
        if (state.isSubmitting) return
        val fee = state.deliveryFeeText.toDoubleOrNull()
        if (fee == null || fee < 0) {
            setState { copy(fieldError = "Enter a valid delivery fee.") }
            return
        }
        val input = state.toInput(fee) ?: return
        setState { copy(isSubmitting = true, fieldError = null) }
        viewModelScope.launch {
            when (val result = createRequest(input)) {
                is Result.Success -> {
                    setState { copy(isSubmitting = false) }
                    sendEffect(CreateRequestUiEffect.Submitted)
                }
                is Result.Error -> {
                    setState { copy(isSubmitting = false) }
                    sendEffect(CreateRequestUiEffect.ShowMessage(result.userMessage()))
                }
                Result.Loading -> Unit
            }
        }
    }

    private fun CreateRequestUiState.toInput(fee: Double): CreateRequestInput? {
        val cat = category ?: return null
        return when (cat) {
            RequestCategory.Shopping -> CreateRequestInput(
                category = cat,
                title = shopName.ifBlank { itemsText.lineSequence().firstOrNull().orEmpty() }
                    .ifBlank { "Shopping request" },
                description = buildString {
                    append(itemsText.trim())
                    if (notes.isNotBlank()) append("\n").append(notes.trim())
                },
                budget = budgetText.toDoubleOrNull(),
                deliveryFee = fee,
                pickupLabel = shopName.ifBlank { null },
                shopping = ShoppingDetails(
                    shopName = shopName.ifBlank { null },
                    itemsText = itemsText.trim(),
                    notes = notes.ifBlank { null },
                ),
            )
            RequestCategory.Pickup -> CreateRequestInput(
                category = cat,
                title = pickupItemDescription.trim().ifBlank { "Pickup" },
                description = pickupItemDescription.trim(),
                deliveryFee = fee,
                pickupLabel = pickupAddress.trim(),
                dropoffLabel = dropoffAddress.trim(),
                pickup = PickupDetails(itemDescription = pickupItemDescription.trim()),
            )
            RequestCategory.Bills -> {
                val amount = billAmountText.toDoubleOrNull() ?: return null
                CreateRequestInput(
                    category = cat,
                    title = "$billType — $accountRef",
                    description = "Pay $billType ($accountRef) — $amount EGP",
                    budget = amount,
                    deliveryFee = fee,
                    bills = BillsDetails(
                        billType = billType.trim(),
                        accountRef = accountRef.trim(),
                        amount = amount,
                    ),
                )
            }
            RequestCategory.Custom -> CreateRequestInput(
                category = cat,
                title = customTitle.trim(),
                description = customBody.trim(),
                budget = budgetText.toDoubleOrNull(),
                deliveryFee = fee,
                custom = CustomDetails(),
            )
        }
    }
}
