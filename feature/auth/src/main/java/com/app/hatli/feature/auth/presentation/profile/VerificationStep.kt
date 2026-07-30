package com.app.hatli.feature.auth.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme

@Composable
internal fun VerificationStep(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
    HatliTextField(
        value = state.nationalId,
        onValueChange = { onEvent(CreateProfileUiEvent.NationalId(it)) },
        label = "National ID number",
    )

    ComingSoonUploadCard(
        title = "ID image",
        body = "Photo of your national ID will be required in a future version.",
    )
    ComingSoonUploadCard(
        title = "Selfie",
        body = "A quick selfie check will be enabled once secure storage is available.",
    )

    HatliCard {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
            Text(
                text = "You can finish registration now",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "We'll mark your verification as Pending. Image upload opens after we enable Firebase Storage.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ComingSoonUploadCard(
    title: String,
    body: String,
) {
    val extended = HatliTheme.extendedColors
    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Coming soon",
                style = MaterialTheme.typography.labelLarge,
                color = extended.warning,
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = HatliSpacing.xxs),
            )
        }
    }
}
