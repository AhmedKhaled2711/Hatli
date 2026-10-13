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
fun CustomDetailsSection(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        HatliTextField(
            value = state.customTitle,
            onValueChange = { onEvent(CreateRequestUiEvent.CustomTitleChanged(it)) },
            label = stringResource(R.string.create_request_custom_title_label),
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.customBody,
            onValueChange = { onEvent(CreateRequestUiEvent.CustomBodyChanged(it)) },
            label = stringResource(R.string.create_request_custom_body_label),
            singleLine = false,
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.budgetText,
            onValueChange = { onEvent(CreateRequestUiEvent.BudgetChanged(it)) },
            label = stringResource(R.string.create_request_custom_budget_label),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
