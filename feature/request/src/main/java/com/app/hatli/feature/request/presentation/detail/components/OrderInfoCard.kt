package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.CoralContainer
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.request_detail_summary_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CharcoalDark
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CoralContainer,
                    modifier = Modifier.padding(2.dp)
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Text(
                            text = "${req.deliveryFee.toInt()} EGP",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = BrandCoral,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            DetailRowItem(
                icon = Icons.Default.ReceiptLong,
                label = stringResource(R.string.request_detail_description),
                value = req.description
            )

            if (!req.pickupLabel.isNullOrBlank()) {
                DetailRowItem(
                    icon = Icons.Default.LocationOn,
                    label = "مكان الاستلام / المحل",
                    value = req.pickupLabel ?: "",
                    valueColor = BrandTeal
                )
            }

            if (!req.dropoffLabel.isNullOrBlank()) {
                DetailRowItem(
                    icon = Icons.Default.Navigation,
                    label = "عنوان التوصيل",
                    value = req.dropoffLabel ?: ""
                )
            }

            HorizontalDivider(
                color = HatliColors.OffWhiteCanvas,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.request_detail_delivery_fee),
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalMuted
                )
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        text = stringResource(
                            R.string.request_detail_delivery_fee_format,
                            req.deliveryFee.toInt()
                        ),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BrandCoral
                    )
                }
            }
        }
    }
}
