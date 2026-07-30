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
import com.app.hatli.core.model.Gender

@Composable
internal fun PersonalInfoStep(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
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
        label = "Phone number",
        enabled = false,
    )

    Text(
        text = "Gender",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = HatliSpacing.xs),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        HatliCard(
            modifier = Modifier.weight(1f),
            selected = state.gender == Gender.Male,
            onClick = { onEvent(CreateProfileUiEvent.GenderSelected(Gender.Male)) },
        ) {
            Text(text = "Male", style = MaterialTheme.typography.titleMedium)
        }
        HatliCard(
            modifier = Modifier.weight(1f),
            selected = state.gender == Gender.Female,
            onClick = { onEvent(CreateProfileUiEvent.GenderSelected(Gender.Female)) },
        ) {
            Text(text = "Female", style = MaterialTheme.typography.titleMedium)
        }
    }
}
