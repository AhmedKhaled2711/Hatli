package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.icon
import com.app.hatli.feature.request.presentation.request.label
import com.app.hatli.feature.request.presentation.request.subtitle

private data class CategoryVisual(
    val category: RequestCategory,
    val containerColor: Color,
    val iconColor: Color,
)

@Composable
private fun rememberCategoryVisuals(): List<CategoryVisual> {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val primary = MaterialTheme.colorScheme.primary
    val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    val tertiary = MaterialTheme.colorScheme.tertiary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant

    return androidx.compose.runtime.remember(primaryContainer, primary, secondaryContainer, secondary, tertiaryContainer, tertiary, surfaceVariant, onSurfaceVariant) {
        listOf(
            CategoryVisual(
                category = RequestCategory.Shopping,
                containerColor = primaryContainer,
                iconColor = primary,
            ),
            CategoryVisual(
                category = RequestCategory.Pickup,
                containerColor = secondaryContainer,
                iconColor = secondary,
            ),
            CategoryVisual(
                category = RequestCategory.Bills,
                containerColor = tertiaryContainer,
                iconColor = tertiary,
            ),
            CategoryVisual(
                category = RequestCategory.Custom,
                containerColor = surfaceVariant,
                iconColor = onSurfaceVariant,
            ),
        )
    }
}

@Composable
fun CategoryStep(onEvent: (CreateRequestUiEvent) -> Unit) {
    val categoryVisuals = rememberCategoryVisuals()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        categoryVisuals.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.md),
            ) {
                rowItems.forEach { visual ->
                    CategoryMatrixCard(
                        category = visual.category,
                        containerColor = visual.containerColor,
                        iconColor = visual.iconColor,
                        modifier = Modifier.weight(1f),
                        onClick = { onEvent(CreateRequestUiEvent.CategorySelected(visual.category)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryMatrixCard(
    category: RequestCategory,
    containerColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, iconColor.copy(alpha = 0.2f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HatliSpacing.md),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = category.icon(),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = category.label(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = category.subtitle(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
