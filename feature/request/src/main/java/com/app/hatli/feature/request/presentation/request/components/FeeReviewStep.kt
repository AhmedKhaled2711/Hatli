package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestDefaults
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState
import com.app.hatli.feature.request.presentation.request.ReviewSummaryRow
import com.app.hatli.feature.request.presentation.request.label

@Composable
fun FeeReviewStep(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    val extendedColors = LocalHatliExtendedColors.current

    Text(
        text = stringResource(
            R.string.create_request_suggested_fee_hint,
            RequestDefaults.SUGGESTED_DELIVERY_FEE_EGP.toInt()
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = extendedColors.onSurfaceVariant,
    )

    HatliTextField(
        value = state.deliveryFeeText,
        onValueChange = { onEvent(CreateRequestUiEvent.DeliveryFeeChanged(it)) },
        label = stringResource(R.string.create_request_delivery_fee_label),
    )

    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(HatliSpacing.sm),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs)
            ) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    tint = HatliColors.BrandTeal,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(R.string.create_request_summary_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            ReviewSummaryRow(
                label = stringResource(R.string.create_request_summary_category_label),
                value = state.category?.label().orEmpty()
            )

            val detailsText = when (state.category) {
                RequestCategory.Shopping -> state.itemsText
                RequestCategory.Pickup -> stringResource(
                    R.string.create_request_summary_pickup_format,
                    state.pickupItemDescription,
                    state.pickupAddress,
                    state.dropoffAddress
                )
                RequestCategory.Bills -> stringResource(
                    R.string.create_request_summary_bills_format,
                    state.billType,
                    state.accountRef,
                    state.billAmountText
                )
                RequestCategory.Custom -> stringResource(
                    R.string.create_request_summary_custom_format,
                    state.customTitle,
                    state.customBody
                )
                null -> ""
            }

            if (detailsText.isNotBlank()) {
                ReviewSummaryRow(
                    label = stringResource(R.string.create_request_summary_details_label),
                    value = detailsText
                )
            }
        }
    }
}
