package com.app.hatli.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.app.hatli.core.designsystem.theme.HatliTheme

@Composable
fun HatliTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val extended = HatliTheme.extendedColors
    val scheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        isError = isError,
        singleLine = singleLine,
        textStyle = typography.bodyLarge,
        label = { Text(label, style = typography.bodySmall) },
        supportingText = supportingText?.let {
            { Text(it, style = typography.bodySmall) }
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = scheme.primary,
            unfocusedBorderColor = extended.border,
            disabledBorderColor = extended.disabledContainer,
            errorBorderColor = scheme.error,
            focusedLabelColor = scheme.primary,
            unfocusedLabelColor = scheme.onSurfaceVariant,
            disabledLabelColor = extended.disabled,
            cursorColor = scheme.primary,
            focusedTextColor = scheme.onSurface,
            unfocusedTextColor = scheme.onSurface,
            disabledTextColor = extended.disabled,
            focusedContainerColor = scheme.surface,
            unfocusedContainerColor = scheme.surface,
            disabledContainerColor = extended.disabledContainer,
            errorContainerColor = scheme.surface,
        ),
    )
}
