package com.app.hatli.feature.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.model.UserRole

@Composable
fun UserRoleBadgeCard(role: UserRole) {
    val extendedColors = LocalHatliExtendedColors.current
    val isRunner = role == UserRole.Runner

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isRunner) HatliColors.Teal900 else extendedColors.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isRunner) Icons.Default.LocalShipping else Icons.Default.Person,
                contentDescription = null,
                tint = if (isRunner) HatliColors.BrandTeal else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(HatliSpacing.md))
            Column {
                Text(
                    text = if (isRunner) "Delivery Captain (Runner)" else "Client (Requester)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isRunner) HatliColors.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isRunner) "Authorized to fulfill & deliver orders" else "Order items & get them delivered to home",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isRunner) HatliColors.Teal100 else extendedColors.onSurfaceVariant
                )
            }
        }
    }
}