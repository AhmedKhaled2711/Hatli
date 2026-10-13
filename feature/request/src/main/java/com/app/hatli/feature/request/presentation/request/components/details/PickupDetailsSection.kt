package com.app.hatli.feature.request.presentation.request.components.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState

private val LocalLandmarkResIds = listOf(
    R.string.create_request_landmark_old_square,
    R.string.create_request_landmark_sea_street,
    R.string.create_request_landmark_hospital,
    R.string.create_request_landmark_bus_station,
    R.string.create_request_landmark_governorate,
)

@Composable
fun PickupDetailsSection(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = HatliTheme.extendedColors
    val defaultDropoff = stringResource(R.string.create_request_default_dropoff_address)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        // 1. Pickup Address Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(BrandTeal.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = BrandTeal,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Text(
                    text = stringResource(R.string.create_request_pickup_address_label),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                    ),
                )
            }

            // Local Landmark Selector Chips for Pickup Point
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                contentPadding = PaddingValues(vertical = 2.dp),
            ) {
                items(LocalLandmarkResIds) { landmarkRes ->
                    val landmark = stringResource(landmarkRes)
                    val isSelected = state.pickupAddress == landmark
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onEvent(CreateRequestUiEvent.PickupAddressChanged(landmark)) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) BrandTeal.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (isSelected) BrandTeal else extended.border),
                    ) {
                        Text(
                            text = landmark,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (isSelected) BrandTeal else MaterialTheme.colorScheme.primary,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 12.sp,
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        )
                    }
                }
            }

            HatliTextField(
                value = state.pickupAddress,
                onValueChange = { onEvent(CreateRequestUiEvent.PickupAddressChanged(it)) },
                label = stringResource(R.string.create_request_pickup_address_label),
                placeholder = stringResource(R.string.create_request_pickup_address_placeholder),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // 2. Drop-off Address Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(BrandCoral.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = BrandCoral,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    Text(
                        text = stringResource(R.string.create_request_dropoff_address_label),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                        ),
                    )
                }

                // Quick preset chip: Use saved address
                val isUsingSaved = (!state.savedDeliveryAddress.isNullOrBlank() && state.dropoffAddress == state.savedDeliveryAddress) ||
                    (state.savedDeliveryAddress.isNullOrBlank() && state.dropoffAddress.isNotBlank() && state.dropoffAddress == defaultDropoff)

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onEvent(CreateRequestUiEvent.UseSavedAddress(defaultDropoff))
                        },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isUsingSaved) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    },
                    border = BorderStroke(
                        1.dp,
                        if (isUsingSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        if (isUsingSaved) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(13.dp),
                            )
                        }
                        Text(
                            text = stringResource(R.string.create_request_use_saved_address),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = if (isUsingSaved) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp,
                            ),
                        )
                    }
                }
            }

            HatliTextField(
                value = state.dropoffAddress,
                onValueChange = { onEvent(CreateRequestUiEvent.DropoffAddressChanged(it)) },
                label = stringResource(R.string.create_request_dropoff_address_label),
                placeholder = stringResource(R.string.create_request_dropoff_address_placeholder),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // 3. Item Description Section
        HatliTextField(
            value = state.pickupItemDescription,
            onValueChange = { onEvent(CreateRequestUiEvent.PickupItemChanged(it)) },
            label = stringResource(R.string.create_request_item_desc_label),
            placeholder = stringResource(R.string.create_request_item_desc_placeholder),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
