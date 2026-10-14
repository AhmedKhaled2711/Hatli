package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.feature.home.R

@Composable
fun HomeQuickActions(
    onCreateRequest: () -> Unit,
    onOpenRequests: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        HatliCard(
            modifier = Modifier.weight(1f),
            onClick = onCreateRequest,
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
            )

            Text(
                text = stringResource(
                    R.string.home_quick_action_new_request,
                ),
            )
        }

        HatliCard(
            modifier = Modifier.weight(1f),
            onClick = onOpenRequests,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ListAlt,
                contentDescription = null,
            )

            Text(
                text = stringResource(
                    R.string.home_quick_action_my_requests,
                ),
            )
        }
    }
}