package com.app.hatli.feature.home.presentation.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.feature.home.R

@Composable
fun RunnerPresenceCard(
    isOnline: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HatliCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)) {
            Text(
                text = if (isOnline) stringResource(R.string.home_runner_status_online) else stringResource(R.string.home_runner_status_offline),
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = if (isOnline) {
                    stringResource(R.string.home_runner_online_desc)
                } else {
                    stringResource(R.string.home_runner_offline_desc)
                },
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (isOnline) {
                HatliSecondaryButton(text = stringResource(R.string.home_runner_action_go_offline), onClick = onToggle)
            } else {
                HatliPrimaryButton(text = stringResource(R.string.home_runner_action_go_online), onClick = onToggle)
            }
        }
    }
}
