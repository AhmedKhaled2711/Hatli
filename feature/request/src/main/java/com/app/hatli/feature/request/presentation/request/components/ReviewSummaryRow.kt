package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors

@Composable
fun ReviewSummaryRow(
    label: String,
    value: String
) {
    val extendedColors = LocalHatliExtendedColors.current

    Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = extendedColors.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
