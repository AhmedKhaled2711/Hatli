package com.app.hatli.feature.home.presentation.requester.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.feature.home.R

data class QuickServiceCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val containerColor: Color,
    val iconColor: Color,
)

@Composable
fun rememberQuickServiceCategories(): List<QuickServiceCategory> {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val primary = MaterialTheme.colorScheme.primary
    val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    val tertiary = MaterialTheme.colorScheme.tertiary

    val groceryTitle = stringResource(R.string.home_category_grocery_title)
    val grocerySub = stringResource(R.string.home_category_grocery_subtitle)
    val pharmacyTitle = stringResource(R.string.home_category_pharmacy_title)
    val pharmacySub = stringResource(R.string.home_category_pharmacy_subtitle)
    val courierTitle = stringResource(R.string.home_category_courier_title)
    val courierSub = stringResource(R.string.home_category_courier_subtitle)
    val billsTitle = stringResource(R.string.home_category_bills_title)
    val billsSub = stringResource(R.string.home_category_bills_subtitle)
    val customTitle = stringResource(R.string.home_category_custom_title)
    val customSub = stringResource(R.string.home_category_custom_subtitle)

    return remember(
        primaryContainer, primary, secondaryContainer, secondary, tertiaryContainer, tertiary,
        groceryTitle, grocerySub, pharmacyTitle, pharmacySub,
        courierTitle, courierSub, billsTitle, billsSub, customTitle, customSub,
    ) {
        listOf(
            QuickServiceCategory(
                id = "grocery",
                title = groceryTitle,
                subtitle = grocerySub,
                icon = Icons.Default.ShoppingBag,
                containerColor = CoralContainer,
                iconColor = BrandCoral,
            ),
            QuickServiceCategory(
                id = "pharmacy",
                title = pharmacyTitle,
                subtitle = pharmacySub,
                icon = Icons.Default.LocalPharmacy,
                containerColor = BrandTeal.copy(alpha = 0.12f),
                iconColor = BrandTeal,
            ),
            QuickServiceCategory(
                id = "courier",
                title = courierTitle,
                subtitle = courierSub,
                icon = Icons.Default.LocalShipping,
                containerColor = primaryContainer,
                iconColor = primary,
            ),
            QuickServiceCategory(
                id = "bills",
                title = billsTitle,
                subtitle = billsSub,
                icon = Icons.Default.Payment,
                containerColor = secondaryContainer,
                iconColor = secondary,
            ),
            QuickServiceCategory(
                id = "custom_errand",
                title = customTitle,
                subtitle = customSub,
                icon = Icons.Default.MoreHoriz,
                containerColor = tertiaryContainer,
                iconColor = tertiary,
            ),
        )
    }
}

@Composable
fun QuickServiceCategoriesSection(
    categories: List<QuickServiceCategory>,
    onQuickAction: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        Text(
            text = stringResource(R.string.home_direct_delivery_services),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            ),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            contentPadding = PaddingValues(horizontal = 2.dp),
        ) {
            items(
                items = categories,
                key = { it.id },
            ) { cat ->
                CategoryCard(category = cat, onClick = { onQuickAction(cat.id) })
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: QuickServiceCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = HatliTheme.extendedColors

    Surface(
        modifier = modifier
            .width(148.dp)
            .height(138.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.sm,
        shadowElevation = HatliElevation.sm,
        border = BorderStroke(1.dp, extended.border),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(HatliSpacing.md),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(category.iconColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        tint = category.iconColor,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = category.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                    ),
                )

                Text(
                    text = category.subtitle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                    ),
                )
            }
        }
    }
}
