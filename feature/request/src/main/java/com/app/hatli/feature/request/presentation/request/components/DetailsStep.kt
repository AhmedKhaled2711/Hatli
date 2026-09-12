package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState

@Composable
fun DetailsStep(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    when (state.category) {
        RequestCategory.Shopping -> {
            HatliTextField(
                value = state.shopName,
                onValueChange = { onEvent(CreateRequestUiEvent.ShopNameChanged(it)) },
                label = stringResource(R.string.create_request_shop_name_label),
            )
            HatliTextField(
                value = state.itemsText,
                onValueChange = { onEvent(CreateRequestUiEvent.ItemsChanged(it)) },
                label = stringResource(R.string.create_request_items_label),
                supportingText = stringResource(R.string.create_request_items_supporting),
            )
            HatliTextField(
                value = state.notes,
                onValueChange = { onEvent(CreateRequestUiEvent.NotesChanged(it)) },
                label = stringResource(R.string.create_request_notes_label),
            )
            HatliTextField(
                value = state.budgetText,
                onValueChange = { onEvent(CreateRequestUiEvent.BudgetChanged(it)) },
                label = stringResource(R.string.create_request_budget_label),
            )
        }
        RequestCategory.Pickup -> {
            HatliTextField(
                value = state.pickupAddress,
                onValueChange = { onEvent(CreateRequestUiEvent.PickupAddressChanged(it)) },
                label = stringResource(R.string.create_request_pickup_address_label),
            )
            HatliTextField(
                value = state.dropoffAddress,
                onValueChange = { onEvent(CreateRequestUiEvent.DropoffAddressChanged(it)) },
                label = stringResource(R.string.create_request_dropoff_address_label),
            )
            HatliTextField(
                value = state.pickupItemDescription,
                onValueChange = { onEvent(CreateRequestUiEvent.PickupItemChanged(it)) },
                label = stringResource(R.string.create_request_item_desc_label),
            )
        }
        RequestCategory.Bills -> {
            HatliTextField(
                value = state.billType,
                onValueChange = { onEvent(CreateRequestUiEvent.BillTypeChanged(it)) },
                label = stringResource(R.string.create_request_bill_type_label),
                supportingText = stringResource(R.string.create_request_bill_type_supporting),
            )
            HatliTextField(
                value = state.accountRef,
                onValueChange = { onEvent(CreateRequestUiEvent.AccountRefChanged(it)) },
                label = stringResource(R.string.create_request_account_ref_label),
            )
            HatliTextField(
                value = state.billAmountText,
                onValueChange = { onEvent(CreateRequestUiEvent.BillAmountChanged(it)) },
                label = stringResource(R.string.create_request_bill_amount_label),
            )
        }
        RequestCategory.Custom -> {
            HatliTextField(
                value = state.customTitle,
                onValueChange = { onEvent(CreateRequestUiEvent.CustomTitleChanged(it)) },
                label = stringResource(R.string.create_request_custom_title_label),
            )
            HatliTextField(
                value = state.customBody,
                onValueChange = { onEvent(CreateRequestUiEvent.CustomBodyChanged(it)) },
                label = stringResource(R.string.create_request_custom_body_label),
            )
            HatliTextField(
                value = state.budgetText,
                onValueChange = { onEvent(CreateRequestUiEvent.BudgetChanged(it)) },
                label = stringResource(R.string.create_request_custom_budget_label),
            )
        }
        null -> Unit
    }
}
