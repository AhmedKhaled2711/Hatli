package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.model.Request
import com.app.hatli.feature.request.R

@Composable
fun CounterpartCard(
    req: Request,
    isRequester: Boolean
) {
    val extendedColors = LocalHatliExtendedColors.current
    val targetName = if (isRequester) req.runnerName else req.requesterName
    val roleTitle = if (isRequester) {
        stringResource(R.string.request_detail_runner_title)
    } else {
        stringResource(R.string.request_detail_client_title)
    }

    if (targetName.isNullOrBlank()) return

    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(HatliColors.Teal900),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRequester) Icons.Default.DirectionsBike else Icons.Default.Person,
                    contentDescription = null,
                    tint = HatliColors.BrandTeal,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(HatliSpacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = roleTitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = extendedColors.onSurfaceVariant
                )
                Text(
                    text = targetName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
