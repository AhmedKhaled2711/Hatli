package com.app.hatli.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.TealContainer
import com.app.hatli.core.model.UserRole

@Composable
fun UserRoleBadgeCard(role: UserRole) {
    val isRunner = role == UserRole.Runner

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isRunner) TealContainer else CoralContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRunner) Icons.Default.DirectionsBike else Icons.Default.Person,
                    contentDescription = null,
                    tint = if (isRunner) BrandTeal else BrandCoral,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(HatliSpacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isRunner) "نمط الكابتن (مستعد للتوصيل)" else "نمط العميل (طالب خدمات)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CharcoalDark
                )
                Text(
                    text = if (isRunner) "معتمد وقادر على استقبال طلبات التوصيل" else "تصفح واطلب أغراضك وسيوصلها لك أقرب كابتن",
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalMuted
                )
            }
        }
    }
}