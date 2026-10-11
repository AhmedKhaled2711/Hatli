package com.app.hatli.feature.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.VerificationStatus
import com.app.hatli.feature.profile.R
import java.util.Locale

@Composable
fun RunnerDetailsSection(
    transportType: TransportType?,
    vehicleInfo: String?,
    nationalId: String?,
    verificationStatus: VerificationStatus?,
    modifier: Modifier = Modifier,
    ordersCount: Int? = null,
    rating: Double? = null,
    acceptanceRate: Int? = null,
) {
    val extendedColors = LocalHatliExtendedColors.current

    val (statusLabel, statusColor) = when (verificationStatus) {
        VerificationStatus.Approved -> {
            stringResource(R.string.profile_status_approved) to extendedColors.success
        }
        VerificationStatus.UnderReview -> {
            stringResource(R.string.profile_status_under_review) to extendedColors.warning
        }
        VerificationStatus.Pending -> {
            stringResource(R.string.profile_status_pending) to extendedColors.warning
        }
        VerificationStatus.Rejected -> {
            stringResource(R.string.profile_status_rejected) to MaterialTheme.colorScheme.error
        }
        null -> {
            stringResource(R.string.profile_status_unspecified) to MaterialTheme.colorScheme.onSurfaceVariant
        }
    }

    val transportText = when (transportType) {
        TransportType.Bicycle -> stringResource(R.string.profile_transport_bicycle)
        TransportType.Motorcycle -> stringResource(R.string.profile_transport_motorcycle)
        TransportType.Car -> stringResource(R.string.profile_transport_car)
        TransportType.Walking -> stringResource(R.string.profile_transport_walking)
        TransportType.Other -> stringResource(R.string.profile_transport_other)
        null -> stringResource(R.string.profile_status_unspecified)
    }

    val ordersDisplay = ordersCount?.toString() ?: stringResource(R.string.profile_metric_empty_value)
    val ratingDisplay = if (rating != null && rating > 0.0) {
        String.format(Locale.US, "%.1f ★", rating)
    } else {
        "— ★"
    }
    val acceptanceDisplay = acceptanceRate?.let { "$it%" } ?: stringResource(R.string.profile_metric_empty_value)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)
    ) {
        // 3-Metric Runner Performance Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                title = stringResource(R.string.profile_metric_orders_title),
                value = ordersDisplay,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = stringResource(R.string.profile_metric_rating_title),
                value = ratingDisplay,
                valueColor = extendedColors.amberPending,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = stringResource(R.string.profile_metric_acceptance_title),
                value = acceptanceDisplay,
                valueColor = extendedColors.success,
                modifier = Modifier.weight(1f)
            )
        }

        // Captain Documents & Info
        ProfileSectionCard(title = stringResource(R.string.profile_section_runner_documents)) {
            ProfileInfoRow(
                icon = Icons.Default.Shield,
                label = stringResource(R.string.profile_label_verification_status),
                value = statusLabel,
                valueColor = statusColor,
                iconTint = statusColor
            )
            ProfileInfoRow(
                icon = Icons.Default.LocalShipping,
                label = stringResource(R.string.profile_label_transport_type),
                value = transportText
            )
            vehicleInfo?.takeIf { it.isNotBlank() }?.let {
                ProfileInfoRow(
                    icon = Icons.Default.Badge,
                    label = stringResource(R.string.profile_label_vehicle_info),
                    value = it
                )
            }
            nationalId?.takeIf { it.isNotBlank() }?.let {
                ProfileInfoRow(
                    icon = Icons.Default.CreditCard,
                    label = stringResource(R.string.profile_label_national_id),
                    value = it
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        shadowElevation = 1.dp,
        tonalElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = valueColor,
                textAlign = TextAlign.Center
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
