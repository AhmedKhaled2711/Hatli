package com.app.hatli.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.AmberPending
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas

@Composable
fun RunnerDetailsSection(
    transportType: String,
    vehicleInfo: String?,
    nationalId: String?,
    verificationStatus: String
) {
    val extendedColors = LocalHatliExtendedColors.current

    Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)) {
        // 3-Metric Runner Performance Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricCard(
                title = "الطلبات",
                value = "48",
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "التقييم العام",
                value = "4.9 ★",
                valueColor = AmberPending,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "نسبة القبول",
                value = "98%",
                valueColor = BrandTeal,
                modifier = Modifier.weight(1f)
            )
        }

        ProfileSectionCard(title = "مستندات وبيانات الكابتن") {
            ProfileInfoRow(
                icon = Icons.Default.Shield,
                label = "حالة الاعتماد",
                value = if (verificationStatus.equals("VERIFIED", true)) "معتمد ورسمي 🟢" else "قيد المراجعة 🟡",
                valueColor = if (verificationStatus.equals("VERIFIED", true)) extendedColors.success else extendedColors.warning
            )
            ProfileInfoRow(
                icon = Icons.Default.LocalShipping,
                label = "وسيلة التوصيل",
                value = transportType
            )
            vehicleInfo?.takeIf { it.isNotBlank() }?.let {
                ProfileInfoRow(
                    icon = Icons.Default.Badge,
                    label = "بيانات المركبة",
                    value = it
                )
            }
            nationalId?.takeIf { it.isNotBlank() }?.let {
                ProfileInfoRow(
                    icon = Icons.Default.Verified,
                    label = "الرقم القومي",
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
    valueColor: Color = CharcoalDark,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                color = valueColor,
                textAlign = TextAlign.Center
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = CharcoalMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
