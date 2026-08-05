package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingStrings

/**
 * Flush under the status bar: Hatli wordmark · Skip.
 * Uses [MaterialTheme.typography] (Mada) — no raw fontSize.
 */
@Composable
fun OnboardingTopBar(
    showSkip: Boolean,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
    darkHeader: Boolean = false,
) {
    val onHeader = if (darkHeader) HatliColors.White else HatliColors.OnPrimary
    val skipColor = onHeader.copy(alpha = 0.55f)
    val typography = MaterialTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HatliSpacing.md),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(OnboardingStrings.brand),
            style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = onHeader,
        )
        if (showSkip) {
            CompositionLocalProvider(
                LocalMinimumInteractiveComponentSize provides Dp.Unspecified,
            ) {
                TextButton(
                    onClick = onSkip,
                    contentPadding = PaddingValues(horizontal = HatliSpacing.sm, vertical = 0.dp),
                ) {
                    Text(
                        text = stringResource(OnboardingStrings.skip).uppercase(),
                        style = typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp,
                        ),
                        color = skipColor,
                    )
                }
            }
        }
    }
}
