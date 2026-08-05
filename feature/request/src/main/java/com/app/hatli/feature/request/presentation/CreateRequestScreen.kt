package com.app.hatli.feature.request.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestDefaults
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun CreateRequestRoute(
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
    onMessage: (String) -> Unit,
    viewModel: CreateRequestViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            CreateRequestUiEffect.NavigateBack -> onBack()
            CreateRequestUiEffect.Submitted -> onSubmitted()
            is CreateRequestUiEffect.ShowMessage -> onMessage(effect.message)
        }
    }
    CreateRequestScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun CreateRequestScreen(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(HatliSpacing.lg),
    ) {
        Text(
            text = when (state.step) {
                CreateWizardStep.Category -> "What do you need?"
                CreateWizardStep.Details -> state.category?.label().orEmpty()
                CreateWizardStep.FeeReview -> "Review & fee"
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(HatliSpacing.md))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
        ) {
            when (state.step) {
                CreateWizardStep.Category -> CategoryStep(onEvent)
                CreateWizardStep.Details -> DetailsStep(state, onEvent)
                CreateWizardStep.FeeReview -> FeeReviewStep(state, onEvent)
            }
            state.fieldError?.let { err ->
                Text(
                    text = err,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        Spacer(modifier = Modifier.height(HatliSpacing.md))

        when (state.step) {
            CreateWizardStep.Category -> {
                HatliSecondaryButton(text = "Cancel", onClick = { onEvent(CreateRequestUiEvent.Back) })
            }
            CreateWizardStep.Details -> {
                HatliPrimaryButton(text = "Continue", onClick = { onEvent(CreateRequestUiEvent.Next) })
                Spacer(modifier = Modifier.height(HatliSpacing.sm))
                HatliSecondaryButton(text = "Back", onClick = { onEvent(CreateRequestUiEvent.Back) })
            }
            CreateWizardStep.FeeReview -> {
                if (state.isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    HatliPrimaryButton(
                        text = "Confirm request",
                        onClick = { onEvent(CreateRequestUiEvent.Submit) },
                    )
                    Spacer(modifier = Modifier.height(HatliSpacing.sm))
                    HatliSecondaryButton(text = "Back", onClick = { onEvent(CreateRequestUiEvent.Back) })
                }
            }
        }
    }
}

@Composable
private fun CategoryStep(onEvent: (CreateRequestUiEvent) -> Unit) {
    RequestCategory.entries.forEach { category ->
        HatliCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(CreateRequestUiEvent.CategorySelected(category)) },
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
                Text(
                    text = category.label(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = category.subtitle(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun DetailsStep(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    when (state.category) {
        RequestCategory.Shopping -> {
            HatliTextField(
                value = state.shopName,
                onValueChange = { onEvent(CreateRequestUiEvent.ShopNameChanged(it)) },
                label = "Shop name (optional)",
            )
            HatliTextField(
                value = state.itemsText,
                onValueChange = { onEvent(CreateRequestUiEvent.ItemsChanged(it)) },
                label = "What do you need?",
                supportingText = "e.g. 2kg meat, 3L milk",
            )
            HatliTextField(
                value = state.notes,
                onValueChange = { onEvent(CreateRequestUiEvent.NotesChanged(it)) },
                label = "Notes (optional)",
            )
            HatliTextField(
                value = state.budgetText,
                onValueChange = { onEvent(CreateRequestUiEvent.BudgetChanged(it)) },
                label = "Expected budget EGP (optional)",
            )
        }
        RequestCategory.Pickup -> {
            HatliTextField(
                value = state.pickupAddress,
                onValueChange = { onEvent(CreateRequestUiEvent.PickupAddressChanged(it)) },
                label = "Pickup address",
            )
            HatliTextField(
                value = state.dropoffAddress,
                onValueChange = { onEvent(CreateRequestUiEvent.DropoffAddressChanged(it)) },
                label = "Drop-off address",
            )
            HatliTextField(
                value = state.pickupItemDescription,
                onValueChange = { onEvent(CreateRequestUiEvent.PickupItemChanged(it)) },
                label = "Item description",
            )
        }
        RequestCategory.Bills -> {
            HatliTextField(
                value = state.billType,
                onValueChange = { onEvent(CreateRequestUiEvent.BillTypeChanged(it)) },
                label = "Bill type",
                supportingText = "e.g. Electricity, Water, Internet",
            )
            HatliTextField(
                value = state.accountRef,
                onValueChange = { onEvent(CreateRequestUiEvent.AccountRefChanged(it)) },
                label = "Account / reference number",
            )
            HatliTextField(
                value = state.billAmountText,
                onValueChange = { onEvent(CreateRequestUiEvent.BillAmountChanged(it)) },
                label = "Amount EGP",
            )
        }
        RequestCategory.Custom -> {
            HatliTextField(
                value = state.customTitle,
                onValueChange = { onEvent(CreateRequestUiEvent.CustomTitleChanged(it)) },
                label = "Title",
            )
            HatliTextField(
                value = state.customBody,
                onValueChange = { onEvent(CreateRequestUiEvent.CustomBodyChanged(it)) },
                label = "Describe your request",
            )
            HatliTextField(
                value = state.budgetText,
                onValueChange = { onEvent(CreateRequestUiEvent.BudgetChanged(it)) },
                label = "Budget EGP (optional)",
            )
        }
        null -> Unit
    }
}

@Composable
private fun FeeReviewStep(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    Text(
        text = "Suggested delivery fee is ${RequestDefaults.SUGGESTED_DELIVERY_FEE_EGP.toInt()} EGP. You can change it.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    HatliTextField(
        value = state.deliveryFeeText,
        onValueChange = { onEvent(CreateRequestUiEvent.DeliveryFeeChanged(it)) },
        label = "Delivery fee EGP",
    )
    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            Text("Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Category: ${state.category?.label().orEmpty()}")
            Text(
                text = when (state.category) {
                    RequestCategory.Shopping -> state.itemsText
                    RequestCategory.Pickup ->
                        "${state.pickupItemDescription}\n${state.pickupAddress} → ${state.dropoffAddress}"
                    RequestCategory.Bills ->
                        "${state.billType} · ${state.accountRef} · ${state.billAmountText} EGP"
                    RequestCategory.Custom -> "${state.customTitle}\n${state.customBody}"
                    null -> ""
                },
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun RequestCategory.label(): String = when (this) {
    RequestCategory.Shopping -> "Shopping"
    RequestCategory.Pickup -> "Pickup"
    RequestCategory.Bills -> "Bills"
    RequestCategory.Custom -> "Custom Request"
}

private fun RequestCategory.subtitle(): String = when (this) {
    RequestCategory.Shopping -> "Supermarket, butcher, bakery, and more"
    RequestCategory.Pickup -> "Pick up from A and deliver to B"
    RequestCategory.Bills -> "Pay a bill or subscription"
    RequestCategory.Custom -> "Anything else nearby"
}
