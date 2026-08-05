package com.app.hatli.feature.request.presentation

import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestDefaults
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState

enum class CreateWizardStep {
    Category,
    Details,
    FeeReview,
}

data class CreateRequestUiState(
    val step: CreateWizardStep = CreateWizardStep.Category,
    val category: RequestCategory? = null,
    // Shopping
    val shopName: String = "",
    val itemsText: String = "",
    val notes: String = "",
    // Pickup
    val pickupAddress: String = "",
    val dropoffAddress: String = "",
    val pickupItemDescription: String = "",
    // Bills
    val billType: String = "",
    val accountRef: String = "",
    val billAmountText: String = "",
    // Custom
    val customTitle: String = "",
    val customBody: String = "",
    val budgetText: String = "",
    val deliveryFeeText: String = RequestDefaults.SUGGESTED_DELIVERY_FEE_EGP.toInt().toString(),
    val fieldError: String? = null,
    val isSubmitting: Boolean = false,
) : UiState

sealed interface CreateRequestUiEvent : UiEvent {
    data class CategorySelected(val category: RequestCategory) : CreateRequestUiEvent
    data object Back : CreateRequestUiEvent
    data object Next : CreateRequestUiEvent
    data object Submit : CreateRequestUiEvent
    data class ShopNameChanged(val value: String) : CreateRequestUiEvent
    data class ItemsChanged(val value: String) : CreateRequestUiEvent
    data class NotesChanged(val value: String) : CreateRequestUiEvent
    data class PickupAddressChanged(val value: String) : CreateRequestUiEvent
    data class DropoffAddressChanged(val value: String) : CreateRequestUiEvent
    data class PickupItemChanged(val value: String) : CreateRequestUiEvent
    data class BillTypeChanged(val value: String) : CreateRequestUiEvent
    data class AccountRefChanged(val value: String) : CreateRequestUiEvent
    data class BillAmountChanged(val value: String) : CreateRequestUiEvent
    data class CustomTitleChanged(val value: String) : CreateRequestUiEvent
    data class CustomBodyChanged(val value: String) : CreateRequestUiEvent
    data class BudgetChanged(val value: String) : CreateRequestUiEvent
    data class DeliveryFeeChanged(val value: String) : CreateRequestUiEvent
}

sealed interface CreateRequestUiEffect : UiEffect {
    data object NavigateBack : CreateRequestUiEffect
    data object Submitted : CreateRequestUiEffect
    data class ShowMessage(val message: String) : CreateRequestUiEffect
}
