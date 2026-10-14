package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState
import com.app.hatli.feature.request.presentation.request.components.details.BillsDetailsSection
import com.app.hatli.feature.request.presentation.request.components.details.CategoryHeaderCard
import com.app.hatli.feature.request.presentation.request.components.details.CustomDetailsSection
import com.app.hatli.feature.request.presentation.request.components.details.PickupDetailsSection
import com.app.hatli.feature.request.presentation.request.components.details.ShoppingDetailsSection

/**
 * Step 2: Form inputs corresponding to the chosen [RequestCategory].
 *
 * Dispatches to domain-specific sections:
 * - [ShoppingDetailsSection] for Shopping, Supermarket, Pharmacy, Grocery
 * - [PickupDetailsSection] for Parcel pickup & dropoff
 * - [BillsDetailsSection] for Utility & government bills
 * - [CustomDetailsSection] for Custom errand requests
 */
@Composable
fun DetailsStep(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        state.category?.let { category ->
            CategoryHeaderCard(
                category = category,
                categorySubtype = state.categorySubtype,
                onChangeCategory = { onEvent(CreateRequestUiEvent.ChangeCategory) },
            )
        }

        when (state.category) {
            RequestCategory.Shopping -> {
                ShoppingDetailsSection(
                    state = state,
                    onEvent = onEvent,
                )
            }

            RequestCategory.Pickup -> {
                PickupDetailsSection(
                    state = state,
                    onEvent = onEvent,
                )
            }

            RequestCategory.Bills -> {
                BillsDetailsSection(
                    state = state,
                    onEvent = onEvent,
                )
            }

            RequestCategory.Custom -> {
                CustomDetailsSection(
                    state = state,
                    onEvent = onEvent,
                )
            }

            null -> Unit
        }
    }
}
