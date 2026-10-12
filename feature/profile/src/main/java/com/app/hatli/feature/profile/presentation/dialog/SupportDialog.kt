package com.app.hatli.feature.profile.presentation.dialog

import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import com.app.hatli.feature.profile.R

@Composable
fun SupportDialog(
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.profile_pref_dialog_support_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = stringResource(R.string.profile_pref_dialog_support_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:support@hatli.app".toUri()
                        putExtra(Intent.EXTRA_SUBJECT, "Hatli Support Request")
                    }
                    if (emailIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(emailIntent)
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.profile_pref_dialog_support_email_action),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.profile_dialog_action_close),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}
