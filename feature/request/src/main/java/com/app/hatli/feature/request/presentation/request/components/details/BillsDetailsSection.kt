package com.app.hatli.feature.request.presentation.request.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState

@Composable
fun BillsDetailsSection(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        HatliTextField(
            value = state.billType,
            onValueChange = { onEvent(CreateRequestUiEvent.BillTypeChanged(it)) },
            label = stringResource(R.string.create_request_bill_type_label),
            placeholder = stringResource(R.string.create_request_bill_type_supporting),
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.accountRef,
            onValueChange = { onEvent(CreateRequestUiEvent.AccountRefChanged(it)) },
            label = stringResource(R.string.create_request_account_ref_label),
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.billAmountText,
            onValueChange = { onEvent(CreateRequestUiEvent.BillAmountChanged(it)) },
            label = stringResource(R.string.create_request_bill_amount_label),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
