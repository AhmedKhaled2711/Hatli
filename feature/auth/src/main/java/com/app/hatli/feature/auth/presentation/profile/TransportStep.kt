package com.app.hatli.feature.auth.presentation.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.TransportType

private val WizardTransportOptions = listOf(
    TransportType.Car,
    TransportType.Motorcycle,
    TransportType.Bicycle,
    TransportType.Walking,
)

@Composable
internal fun TransportStep(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text(
            text = "اختر وسيلة للتوصيل (واحدة على الأقل)",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = CharcoalDark,
            ),
        )

        WizardTransportOptions.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                rowItems.forEach { type ->
                    TransportTile(
                        type = type,
                        selected = state.transportType == type,
                        modifier = Modifier.weight(1f),
                        onClick = { onEvent(CreateProfileUiEvent.Transport(type)) },
                    )
                }
            }
        }

        if (state.transportType != null && state.transportType != TransportType.Walking) {
            Spacer(modifier = Modifier.height(HatliSpacing.xxs))

            HatliTextField(
                value = state.vehicleInfo,
                onValueChange = { onEvent(CreateProfileUiEvent.VehicleInfo(it)) },
                label = "بيانات المركبة (الموديل · اللون · رقم اللوحة)",
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "مثال: هيونداي فيرنا · أبيض · ط ر س 1234",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CharcoalMuted,
                    fontSize = 12.sp,
                ),
                modifier = Modifier.padding(horizontal = HatliSpacing.xs),
            )
        }
    }
}

@Composable
private fun TransportTile(
    type: TransportType,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) BrandTeal else Color(0xFFE5E7EB),
        label = "transportBorder",
    )
    val tileBg by animateColorAsState(
        targetValue = if (selected) BrandTeal.copy(alpha = 0.08f) else Color.White,
        label = "transportBg",
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(tileBg)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp),
            )
            .clickable(onClick = onClick)
            .padding(HatliSpacing.md),
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (selected) BrandTeal else Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = transportIcon(type),
                    contentDescription = null,
                    tint = if (selected) Color.White else CharcoalMuted,
                    modifier = Modifier.size(20.dp),
                )
            }

            Text(
                text = transportTitle(type),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = CharcoalDark,
                    fontSize = 14.sp,
                ),
            )
            Text(
                text = transportSubtitle(type),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CharcoalMuted,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                ),
            )
        }
    }
}

private fun transportIcon(type: TransportType): ImageVector = when (type) {
    TransportType.Car -> Icons.Default.DirectionsCar
    TransportType.Motorcycle -> Icons.Default.TwoWheeler
    TransportType.Bicycle -> Icons.Default.DirectionsBike
    TransportType.Walking -> Icons.Default.DirectionsRun
    TransportType.Other -> Icons.Default.DirectionsCar
}

private fun transportTitle(type: TransportType): String = when (type) {
    TransportType.Car -> "سيارة"
    TransportType.Motorcycle -> "دراجة نارية / سكوتر"
    TransportType.Bicycle -> "دراجة هوائية"
    TransportType.Walking -> "مشياً على الأقدام"
    TransportType.Other -> "وسيلة أخرى"
}

private fun transportSubtitle(type: TransportType): String = when (type) {
    TransportType.Car -> "للطلبات الكبيرة والمسافات"
    TransportType.Motorcycle -> "توصيل سريع داخل المدينة"
    TransportType.Bicycle -> "مشاوير قصيرة وسريعة"
    TransportType.Walking -> "توصيل الأحياء المجاورة"
    TransportType.Other -> "وسيلة نقل مخصصة"
}

