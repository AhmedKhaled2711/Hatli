package com.app.hatli.feature.profile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.feature.profile.presentation.ProfileInfoRow
import com.app.hatli.feature.profile.presentation.ProfileSectionCard

@Composable
fun RunnerDetailsSection(
    transportType: String,
    vehicleInfo: String?,
    nationalId: String?,
    verificationStatus: String
) {
    val extendedColors = LocalHatliExtendedColors.current

    ProfileSectionCard(title = "Captain Vehicle & Security") {
        ProfileInfoRow(
            icon = Icons.Default.Shield,
            label = "Verification Status",
            value = verificationStatus,
            valueColor = if (verificationStatus.equals("VERIFIED", true)) extendedColors.success else extendedColors.warning
        )
        ProfileInfoRow(
            icon = Icons.Default.LocalShipping,
            label = "Transport Mode",
            value = transportType
        )
        vehicleInfo?.takeIf { it.isNotBlank() }?.let {
            ProfileInfoRow(
                icon = Icons.Default.Badge,
                label = "Vehicle Info",
                value = it
            )
        }
        nationalId?.takeIf { it.isNotBlank() }?.let {
            ProfileInfoRow(
                icon = Icons.Default.Verified,
                label = "National ID",
                value = it
            )
        }
    }
}
