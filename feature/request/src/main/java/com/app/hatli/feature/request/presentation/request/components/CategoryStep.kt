package com.app.hatli.feature.request.presentation.request.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.icon
import com.app.hatli.feature.request.presentation.request.label
import com.app.hatli.feature.request.presentation.request.subtitle

@Composable
fun CategoryStep(onEvent: (CreateRequestUiEvent) -> Unit) {
    val extendedColors = LocalHatliExtendedColors.current

    RequestCategory.entries.forEach { category ->
        HatliCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(CreateRequestUiEvent.CategorySelected(category)) },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(HatliSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(HatliColors.Teal900),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon(),
                        contentDescription = null,
                        tint = HatliColors.BrandTeal,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(HatliSpacing.md))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)
                ) {
                    Text(
                        text = category.label(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = category.subtitle(),
                        style = MaterialTheme.typography.bodySmall,
                        color = extendedColors.onSurfaceVariant
                    )
                }
            }
        }
    }
}
