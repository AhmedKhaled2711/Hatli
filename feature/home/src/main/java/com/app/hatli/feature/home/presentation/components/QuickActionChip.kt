package com.app.hatli.feature.home.presentation.components


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.model.QuickAction

@Composable
fun QuickActionChip(
    action: QuickAction,
    isAr: Boolean,
    onClick: () -> Unit,
) {
    HatliCard(
        onClick = onClick,
    ) {
        Text(
            text = if (isAr) {
                action.labelAr
            } else {
                action.labelEn
            },
            style = MaterialTheme.typography.labelLarge,
        )
    }
}