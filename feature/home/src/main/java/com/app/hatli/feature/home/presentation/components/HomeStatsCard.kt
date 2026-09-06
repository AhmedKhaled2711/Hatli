package com.app.hatli.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.hatli.core.designsystem.theme.HatliSpacing

data class HomeStatItem(
    val title: String,
    val value: Int,
)

@Composable
fun HomeStatsCard(
    firstStat: HomeStatItem,
    secondStat: HomeStatItem,
    isAr: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(HatliSpacing.lg),
            horizontalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
        ) {
            HomeStat(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.CheckCircle,
                title = firstStat.title,
                value = firstStat.value,
            )

            HomeStat(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.LocalShipping,
                title = secondStat.title,
                value = secondStat.value,
            )
        }
    }
}

@Composable
fun HomeStat(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: Int,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}