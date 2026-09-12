package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Request
import com.app.hatli.feature.request.R

@Composable
fun OrderInfoCard(req: Request) {
    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)
        ) {
            Text(
                text = stringResource(R.string.request_detail_summary_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            DetailRowItem(
                icon = Icons.Default.ReceiptLong,
                label = stringResource(R.string.request_detail_description),
                value = req.description
            )

            DetailRowItem(
                icon = Icons.Default.Money,
                label = stringResource(R.string.request_detail_delivery_fee),
                value = stringResource(
                    R.string.request_detail_delivery_fee_format,
                    req.deliveryFee.toInt()
                ),
                valueColor = HatliColors.Teal700
            )
        }
    }
}
