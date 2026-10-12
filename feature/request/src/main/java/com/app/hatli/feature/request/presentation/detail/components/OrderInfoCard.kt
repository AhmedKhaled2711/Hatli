package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Request
import com.app.hatli.feature.request.R

@Composable
fun OrderInfoCard(req: Request) {
    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.request_detail_summary_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f, fill = false),
                )

                Spacer(modifier = Modifier.width(HatliSpacing.sm))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Text(
                        text = stringResource(
                            R.string.request_detail_delivery_fee_format,
                            req.deliveryFee.toInt(),
                        ),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }

            DetailRowItem(
                icon = Icons.AutoMirrored.Filled.ReceiptLong,
                label = stringResource(R.string.request_detail_description),
                value = req.description,
            )

            if (!req.pickupLabel.isNullOrBlank()) {
                DetailRowItem(
                    icon = Icons.Default.LocationOn,
                    label = stringResource(R.string.request_detail_pickup_location),
                    value = req.pickupLabel ?: "",
                    valueColor = MaterialTheme.colorScheme.primary,
                )
            }

            if (!req.dropoffLabel.isNullOrBlank()) {
                DetailRowItem(
                    icon = Icons.Default.Navigation,
                    label = stringResource(R.string.request_detail_dropoff_location),
                    value = req.dropoffLabel ?: "",
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.request_detail_delivery_fee),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f, fill = false),
                )

                Spacer(modifier = Modifier.width(HatliSpacing.sm))

                Text(
                    text = stringResource(
                        R.string.request_detail_delivery_fee_format,
                        req.deliveryFee.toInt(),
                    ),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    softWrap = false,
                )
            }
        }
    }
}
