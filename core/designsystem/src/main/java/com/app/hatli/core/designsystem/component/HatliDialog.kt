package com.app.hatli.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HatliDialog(
    title: String,
    message: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissLabel: String? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            HatliTextButton(text = confirmLabel, onClick = onConfirm)
        },
        dismissButton = dismissLabel?.let {
            { HatliTextButton(text = it, onClick = onDismiss) }
        },
    )
}
