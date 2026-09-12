package com.app.hatli.feature.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors

@Composable
fun ProfileErrorState(
    errorMessage: String?,
    onRetry: () -> Unit
) {
    val extendedColors = LocalHatliExtendedColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorMessage ?: "Unable to load profile data",
            style = MaterialTheme.typography.bodyLarge,
            color = extendedColors.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(HatliSpacing.md))
        HatliSecondaryButton(
            text = "Retry",
            onClick = onRetry
        )
    }
}
