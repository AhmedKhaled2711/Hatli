package com.app.hatli.feature.auth.presentation.profile

import androidx.compose.foundation.layout.Arrangement
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
import com.app.hatli.core.model.TransportType

private val WizardTransportOptions = listOf(
    TransportType.Car,
    TransportType.Motorcycle,
    TransportType.Bicycle,
    TransportType.Walking,
)

@Composable
internal fun TransportStep(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
    Text(
        text = "Select one option",
        style = MaterialTheme.typography.titleMedium,
    )

    WizardTransportOptions.chunked(2).forEach { rowItems ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        ) {
            rowItems.forEach { type ->
                HatliCard(
                    modifier = Modifier.weight(1f),
                    selected = state.transportType == type,
                    onClick = { onEvent(CreateProfileUiEvent.Transport(type)) },
                ) {
                    Text(
                        text = transportTitle(type),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = transportSubtitle(type),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = HatliSpacing.xxs),
                    )
                }
            }
        }
    }

    if (state.transportType != null && state.transportType != TransportType.Walking) {
        HatliTextField(
            value = state.vehicleInfo,
            onValueChange = { onEvent(CreateProfileUiEvent.VehicleInfo(it)) },
            label = "Vehicle details",
        )
        Text(
            text = "Example: Honda Civic · White · plate ending 1234",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun transportTitle(type: TransportType): String = when (type) {
    TransportType.Car -> "Car"
    TransportType.Motorcycle -> "Motorcycle"
    TransportType.Bicycle -> "Bicycle"
    TransportType.Walking -> "Walking"
    TransportType.Other -> "Other"
}

private fun transportSubtitle(type: TransportType): String = when (type) {
    TransportType.Car -> "Larger parcels & distance"
    TransportType.Motorcycle -> "Fast city deliveries"
    TransportType.Bicycle -> "Short nearby trips"
    TransportType.Walking -> "Nearby on foot"
    TransportType.Other -> "Something else"
}
