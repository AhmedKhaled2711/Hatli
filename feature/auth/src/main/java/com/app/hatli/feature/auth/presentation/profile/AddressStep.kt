package com.app.hatli.feature.auth.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliSpacing

@Composable
internal fun AddressStep(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        HatliCard {
            Row(
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BrandCoral.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = BrandCoral,
                        modifier = Modifier.size(22.dp),
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xxs)) {
                    Text(
                        text = "عنوان استلام الطلبات",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark,
                        ),
                    )
                    Text(
                        text = "إدخال يدوي مؤقتاً. سيتم تفعيل تحديد الموقع التلقائي عبر الخريطة قريباً.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CharcoalMuted,
                            fontSize = 12.sp,
                        ),
                    )
                }
            }
        }

        HatliTextField(
            value = state.city,
            onValueChange = { onEvent(CreateProfileUiEvent.City(it)) },
            label = "المدينة / المحافظة",
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.area,
            onValueChange = { onEvent(CreateProfileUiEvent.Area(it)) },
            label = "المنطقة / الحي",
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.street,
            onValueChange = { onEvent(CreateProfileUiEvent.Street(it)) },
            label = "الشارع / اسم الشارع",
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.building,
            onValueChange = { onEvent(CreateProfileUiEvent.Building(it)) },
            label = "رقم المبنى / العمارة",
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        ) {
            HatliTextField(
                value = state.floor,
                onValueChange = { onEvent(CreateProfileUiEvent.Floor(it)) },
                label = "الطابق (اختياري)",
                modifier = Modifier.weight(1f),
            )
            HatliTextField(
                value = state.apartment,
                onValueChange = { onEvent(CreateProfileUiEvent.Apartment(it)) },
                label = "الشقة (اختياري)",
                modifier = Modifier.weight(1f),
            )
        }

        HatliTextField(
            value = state.addressNotes,
            onValueChange = { onEvent(CreateProfileUiEvent.AddressNotes(it)) },
            label = "علامة مميزة أو تفاصيل إضافية (اختياري)",
            singleLine = false,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = "نصيحة: أضف ألمع معلَم قريب من عارتك (مثل: بجوار صيدلية أو مسجد) لمساعدة الكابتن.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = CharcoalMuted,
                fontSize = 12.sp,
            ),
            modifier = Modifier.padding(horizontal = HatliSpacing.xs),
        )
    }
}

