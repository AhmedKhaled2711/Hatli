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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Gender

@Composable
internal fun PersonalInfoStep(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        // 96dp Avatar Placeholder with Camera Badge
        Box(
            modifier = Modifier.padding(vertical = HatliSpacing.xs),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(BrandCoral.copy(alpha = 0.12f))
                    .border(2.dp, BrandCoral.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = BrandCoral,
                    modifier = Modifier.size(48.dp),
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(BrandCoral)
                    .shadow(4.dp, CircleShape)
                    .clickable { /* Photo upload trigger placeholder */ },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "تحميل صورة شخصية",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        HatliTextField(
            value = state.firstName,
            onValueChange = { onEvent(CreateProfileUiEvent.FirstName(it)) },
            label = "الاسم الأول",
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.lastName,
            onValueChange = { onEvent(CreateProfileUiEvent.LastName(it)) },
            label = "اسم العائلة",
            modifier = Modifier.fillMaxWidth(),
        )

        HatliTextField(
            value = state.email,
            onValueChange = { onEvent(CreateProfileUiEvent.Email(it)) },
            label = "البريد الإلكتروني (اختياري)",
            modifier = Modifier.fillMaxWidth(),
        )

        // Phone Number Field (Disabled with LTR Layout)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            HatliTextField(
                value = state.phone,
                onValueChange = {},
                label = "رقم الهاتف المحمول",
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(HatliSpacing.xxs))

        // Gender Selection Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
        ) {
            Text(
                text = "النوع / الجنس",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = CharcoalDark,
                ),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                GenderOptionCard(
                    title = "ذكر",
                    selected = state.gender == Gender.Male,
                    modifier = Modifier.weight(1f),
                    onClick = { onEvent(CreateProfileUiEvent.GenderSelected(Gender.Male)) },
                )

                GenderOptionCard(
                    title = "أنثى",
                    selected = state.gender == Gender.Female,
                    modifier = Modifier.weight(1f),
                    onClick = { onEvent(CreateProfileUiEvent.GenderSelected(Gender.Female)) },
                )
            }
        }
    }
}

@Composable
private fun GenderOptionCard(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) BrandCoral else Color(0xFFE5E7EB),
        label = "genderBorder",
    )
    val cardBg by animateColorAsState(
        targetValue = if (selected) BrandCoral.copy(alpha = 0.08f) else Color.White,
        label = "genderBg",
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(cardBg)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable(onClick = onClick)
            .padding(vertical = HatliSpacing.md),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) BrandCoral else CharcoalDark,
                fontSize = 15.sp,
            ),
        )
    }
}

