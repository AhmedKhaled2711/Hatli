package com.app.hatli.core.designsystem.theme

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.HatliTextField

@Preview(name = "Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HatliThemePreview() {
    HatliTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HatliSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
        ) {
            Text(
                text = "Hatli",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = if (HatliTheme.isDark) "Dark theme" else "Light theme",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            HatliCard {
                Text(
                    text = "Surface card",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Success / warning use HatliTheme.extendedColors",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HatliTheme.extendedColors.textSecondary,
                )
            }
            HatliTextField(value = "", onValueChange = {}, label = "Phone")
            HatliPrimaryButton(text = "Primary", onClick = {})
            HatliSecondaryButton(text = "Secondary", onClick = {})
        }
    }
}
