package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestDefaults
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState
import com.app.hatli.feature.request.presentation.request.label

@Composable
fun FeeReviewStep(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    val currentFee = state.deliveryFeeText.toIntOrNull() ?: RequestDefaults.SUGGESTED_DELIVERY_FEE_EGP.toInt()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        // Delivery Fee Interactive Stepper Card
        HatliCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(HatliSpacing.md),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                Text(
                    text = stringResource(R.string.create_request_fee_stepper_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CharcoalDark,
                        fontSize = 14.sp,
                    ),
                )

                Text(
                    text = stringResource(R.string.create_request_fee_stepper_desc),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CharcoalMuted,
                        fontSize = 12.sp,
                    ),
                )

                Spacer(modifier = Modifier.height(HatliSpacing.xxs))

                // Stepper Row: (-5 EGP) [ 35 EGP ] (+5 EGP)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Decrement Button (-5 EGP)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(CoralContainer)
                            .clickable {
                                val newFee = (currentFee - 5).coerceAtLeast(15)
                                onEvent(CreateRequestUiEvent.DeliveryFeeChanged(newFee.toString()))
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = stringResource(R.string.create_request_fee_stepper_decrease),
                            tint = BrandCoral,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    // Dynamic Fee Readout Display (Forced LTR for currency readout)
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = HatliSpacing.lg)
                                .clip(RoundedCornerShape(16.dp))
                                .background(BrandCoral.copy(alpha = 0.10f))
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "$currentFee EGP",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandCoral,
                                    fontSize = 24.sp,
                                ),
                            )
                        }
                    }

                    // Increment Button (+5 EGP)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BrandCoral)
                            .clickable {
                                val newFee = currentFee + 5
                                onEvent(CreateRequestUiEvent.DeliveryFeeChanged(newFee.toString()))
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.create_request_fee_stepper_increase),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }

        // Cost & Details Breakdown Summary Card
        HatliCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(HatliSpacing.md),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                        contentDescription = null,
                        tint = BrandTeal,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = stringResource(R.string.create_request_order_cost_summary),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark,
                            fontSize = 15.sp,
                            ),
                    )
                }

                ReviewSummaryRow(
                    label = stringResource(R.string.create_request_review_service_type),
                    value = state.category?.label().orEmpty(),
                )

                // Category-specific details
                when (state.category) {
                    RequestCategory.Pickup -> {
                        if (state.pickupAddress.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_pickup_address_label),
                                value = state.pickupAddress,
                            )
                        }
                        if (state.dropoffAddress.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_dropoff_address_label),
                                value = state.dropoffAddress,
                            )
                        }
                        if (state.pickupItemDescription.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_item_desc_label),
                                value = state.pickupItemDescription,
                            )
                        }
                    }
                    RequestCategory.Shopping -> {
                        if (state.shopName.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_shop_name_label),
                                value = state.shopName,
                            )
                        }
                        if (state.itemsText.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_items_label),
                                value = state.itemsText,
                            )
                        }
                    }
                    RequestCategory.Bills -> {
                        if (state.billType.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_bill_type_label),
                                value = state.billType,
                            )
                        }
                        if (state.accountRef.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_account_ref_label),
                                value = state.accountRef,
                            )
                        }
                        if (state.billAmountText.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_bill_amount_label),
                                value = stringResource(R.string.create_request_review_egp_suffix, state.billAmountText.toIntOrNull() ?: 0),
                            )
                        }
                    }
                    RequestCategory.Custom -> {
                        if (state.customTitle.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_custom_title_label),
                                value = state.customTitle,
                            )
                        }
                        if (state.customBody.isNotBlank()) {
                            ReviewSummaryRow(
                                label = stringResource(R.string.create_request_custom_body_label),
                                value = state.customBody,
                            )
                        }
                    }
                    null -> Unit
                }

                ReviewSummaryRow(
                    label = stringResource(R.string.create_request_review_delivery_fee),
                    value = stringResource(R.string.create_request_review_egp_suffix, currentFee),
                )

                if (state.category == RequestCategory.Shopping) {
                    ReviewSummaryRow(
                        label = stringResource(R.string.create_request_review_items_cost),
                        value = stringResource(R.string.create_request_review_items_cost_note),
                    )
                }

                ReviewSummaryRow(
                    label = stringResource(R.string.create_request_review_service_charge),
                    value = stringResource(R.string.create_request_review_service_charge_free),
                )
            }
        }
    }
}

