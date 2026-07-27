package com.app.hatli.feature.auth.presentation.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun CreateProfileRoute(
    onHome: () -> Unit,
    viewModel: CreateProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            CreateProfileUiEffect.GoHome -> onHome()
        }
    }
    CreateProfileScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun CreateProfileScreen(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
    val idPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onEvent(CreateProfileUiEvent.IdImage(it.toString())) }
    }
    val selfiePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onEvent(CreateProfileUiEvent.Selfie(it.toString())) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text("Create profile", style = MaterialTheme.typography.headlineLarge)
        Text("Role: ${state.role.name}", style = MaterialTheme.typography.titleMedium)

        HatliTextField(
            value = state.firstName,
            onValueChange = { onEvent(CreateProfileUiEvent.FirstName(it)) },
            label = "First name",
        )
        HatliTextField(
            value = state.lastName,
            onValueChange = { onEvent(CreateProfileUiEvent.LastName(it)) },
            label = "Last name",
        )
        HatliTextField(
            value = state.email,
            onValueChange = { onEvent(CreateProfileUiEvent.Email(it)) },
            label = "Email",
        )
        HatliTextField(
            value = state.phone,
            onValueChange = {},
            label = "Phone",
            enabled = false,
        )

        Text("Gender", style = MaterialTheme.typography.labelLarge)
        Gender.entries.forEach { gender ->
            HatliSecondaryButton(
                text = if (state.gender == gender) "✓ ${gender.name}" else gender.name,
                onClick = { onEvent(CreateProfileUiEvent.GenderSelected(gender)) },
            )
        }

        if (state.isRunner) {
            HatliTextField(
                value = state.nationalId,
                onValueChange = { onEvent(CreateProfileUiEvent.NationalId(it)) },
                label = "National ID",
            )
            HatliSecondaryButton(
                text = if (state.idImageUri != null) "ID image selected" else "Pick ID image",
                onClick = { idPicker.launch("image/*") },
            )
            HatliSecondaryButton(
                text = if (state.selfieUri != null) "Selfie selected" else "Pick selfie",
                onClick = { selfiePicker.launch("image/*") },
            )
            Text("Transport", style = MaterialTheme.typography.labelLarge)
            TransportType.entries.forEach { type ->
                HatliSecondaryButton(
                    text = if (state.transportType == type) "✓ ${type.name}" else type.name,
                    onClick = { onEvent(CreateProfileUiEvent.Transport(type)) },
                )
            }
            HatliTextField(
                value = state.vehicleInfo,
                onValueChange = { onEvent(CreateProfileUiEvent.VehicleInfo(it)) },
                label = "Vehicle info",
            )
        }

        state.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        HatliPrimaryButton(
            text = if (state.isLoading) "Saving…" else "Save profile",
            onClick = { onEvent(CreateProfileUiEvent.Submit) },
            enabled = !state.isLoading,
        )
    }
}
