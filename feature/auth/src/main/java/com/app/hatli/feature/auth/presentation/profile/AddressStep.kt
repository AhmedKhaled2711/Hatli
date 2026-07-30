package com.app.hatli.feature.auth.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing

@Composable
internal fun AddressStep(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
    HatliCard {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            Text(
                text = "Delivery address",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Manual entry for now. Current location and map pin will be added later.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    HatliTextField(
        value = state.city,
        onValueChange = { onEvent(CreateProfileUiEvent.City(it)) },
        label = "City",
    )
    HatliTextField(
        value = state.area,
        onValueChange = { onEvent(CreateProfileUiEvent.Area(it)) },
        label = "Area",
    )
    HatliTextField(
        value = state.street,
        onValueChange = { onEvent(CreateProfileUiEvent.Street(it)) },
        label = "Street",
    )
    HatliTextField(
        value = state.building,
        onValueChange = { onEvent(CreateProfileUiEvent.Building(it)) },
        label = "Building",
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        HatliTextField(
            value = state.floor,
            onValueChange = { onEvent(CreateProfileUiEvent.Floor(it)) },
            label = "Floor (optional)",
            modifier = Modifier.weight(1f),
        )
        HatliTextField(
            value = state.apartment,
            onValueChange = { onEvent(CreateProfileUiEvent.Apartment(it)) },
            label = "Apartment (optional)",
            modifier = Modifier.weight(1f),
        )
    }

    HatliTextField(
        value = state.addressNotes,
        onValueChange = { onEvent(CreateProfileUiEvent.AddressNotes(it)) },
        label = "Additional notes (optional)",
        singleLine = false,
    )

    Text(
        text = "Tip: include a landmark if the building is hard to find.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = HatliSpacing.xxs),
    )
}
