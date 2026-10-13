package com.app.hatli.feature.request.presentation.request.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState

@Composable
fun ShoppingDetailsSection(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shopLabel = when (state.categorySubtype) {
        "pharmacy" -> stringResource(R.string.create_request_pharmacy_shop_label)
        "grocery" -> stringResource(R.string.create_request_grocery_shop_label)
        else -> stringResource(R.string.create_request_shop_name_label)
    }

    val itemsPlaceholder = when (state.categorySubtype) {
        "pharmacy" -> stringResource(R.string.create_request_pharmacy_items_hint)
        "grocery" -> stringResource(R.string.create_request_grocery_items_hint)
        else -> stringResource(R.string.create_request_items_supporting)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        HatliTextField(
            value = state.shopName,
            onValueChange = { onEvent(CreateRequestUiEvent.ShopNameChanged(it)) },
            label = shopLabel,
            modifier = Modifier.fillMaxWidth(),
        )

        // Multiline Items Description Input with Item Counter
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xxs)) {
            HatliTextField(
                value = state.itemsText,
                onValueChange = { onEvent(CreateRequestUiEvent.ItemsChanged(it)) },
                label = stringResource(R.string.create_request_items_label),
                placeholder = itemsPlaceholder,
                singleLine = false,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = stringResource(R.string.create_request_char_count, state.itemsText.length),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                    ),
                )
            }
        }

        HatliTextField(
            value = state.notes,
            onValueChange = { onEvent(CreateRequestUiEvent.NotesChanged(it)) },
            label = stringResource(R.string.create_request_notes_label),
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.budgetText,
            onValueChange = { onEvent(CreateRequestUiEvent.BudgetChanged(it)) },
            label = stringResource(R.string.create_request_budget_label),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
