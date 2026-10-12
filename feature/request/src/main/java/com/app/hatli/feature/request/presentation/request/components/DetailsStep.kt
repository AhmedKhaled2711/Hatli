package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState
import com.app.hatli.feature.request.presentation.request.icon
import com.app.hatli.feature.request.presentation.request.label
import com.app.hatli.feature.request.presentation.request.subtitle

private val LocalLandmarkResIds = listOf(
    R.string.create_request_landmark_old_square,
    R.string.create_request_landmark_sea_street,
    R.string.create_request_landmark_hospital,
    R.string.create_request_landmark_bus_station,
    R.string.create_request_landmark_governorate,
)

@Composable
fun DetailsStep(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    val extended = HatliTheme.extendedColors

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        // Active Category Header Pill with Change Service Action
        state.category?.let { cat ->
            val icon = when (state.categorySubtype) {
                "pharmacy" -> Icons.Default.LocalPharmacy
                "grocery" -> Icons.Default.ShoppingBag
                else -> cat.icon()
            }
            val title = when (state.categorySubtype) {
                "pharmacy" -> stringResource(R.string.create_request_pharmacy_shop_label).substringBefore("(")
                "grocery" -> stringResource(R.string.create_request_grocery_shop_label).substringBefore("(")
                else -> cat.label()
            }
            val accentColor = when (state.categorySubtype) {
                "pharmacy" -> BrandTeal
                else -> BrandCoral
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                border = BorderStroke(1.dp, extended.border),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = HatliSpacing.md, vertical = HatliSpacing.sm),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                        modifier = Modifier.weight(1f),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(22.dp),
                            )
                        }

                        Column {
                            Text(
                                text = title.trim(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp,
                                ),
                            )
                            Text(
                                text = cat.subtitle(),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp,
                                ),
                                maxLines = 1,
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onEvent(CreateRequestUiEvent.ChangeCategory) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp),
                            )
                            Text(
                                text = stringResource(R.string.create_request_change_category),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                ),
                            )
                        }
                    }
                }
            }
        }

        when (state.category) {
            RequestCategory.Shopping -> {
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

            RequestCategory.Pickup -> {
                // Local Landmark Selector Chips for Pickup Point
                Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
                    Text(
                        text = stringResource(R.string.create_request_common_landmarks),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                        ),
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
                        items(LocalLandmarkResIds) { landmarkRes ->
                            val landmark = stringResource(landmarkRes)
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onEvent(CreateRequestUiEvent.PickupAddressChanged(landmark)) },
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, extended.border),
                            ) {
                                Text(
                                    text = landmark,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 12.sp,
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                )
                            }
                        }
                    }
                }

                HatliTextField(
                    value = state.pickupAddress,
                    onValueChange = { onEvent(CreateRequestUiEvent.PickupAddressChanged(it)) },
                    label = stringResource(R.string.create_request_pickup_address_label),
                    modifier = Modifier.fillMaxWidth(),
                )

                // Saved Address Preview Card for Dropoff
                SavedAddressCard(
                    addressTitle = stringResource(R.string.create_request_dropoff_address_label),
                    addressDetail = state.dropoffAddress.ifBlank { stringResource(R.string.create_request_default_dropoff_address) },
                )

                HatliTextField(
                    value = state.pickupItemDescription,
                    onValueChange = { onEvent(CreateRequestUiEvent.PickupItemChanged(it)) },
                    label = stringResource(R.string.create_request_item_desc_label),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            RequestCategory.Bills -> {
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

            RequestCategory.Custom -> {
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

            null -> Unit
        }
    }
}

@Composable
private fun SavedAddressCard(
    addressTitle: String,
    addressDetail: String,
) {
    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HatliSpacing.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BrandCoral.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = BrandCoral,
                        modifier = Modifier.size(20.dp),
                    )
                }

                Column {
                    Text(
                        text = addressTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                        ),
                    )
                    Text(
                        text = addressDetail,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                        ),
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

