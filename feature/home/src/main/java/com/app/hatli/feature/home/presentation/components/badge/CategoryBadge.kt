package com.app.hatli.feature.home.presentation.components.badge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.AmberPending
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.formatting.isAppArabic

@Composable
fun CategoryBadge(
    category: RequestCategory?,
    isAr: Boolean = isAppArabic(),
    modifier: Modifier = Modifier,
) {
    val (labelRes, icon, color) = when (category) {
        RequestCategory.Shopping -> Triple(
            R.string.home_category_badge_shopping,
            Icons.Default.ShoppingBag,
            BrandCoral,
        )
        RequestCategory.Pickup -> Triple(
            R.string.home_category_badge_pickup,
            Icons.Default.LocalShipping,
            BrandTeal,
        )
        RequestCategory.Bills -> Triple(
            R.string.home_category_badge_bills,
            Icons.Default.Receipt,
            Color(0xFF7B1FA2),
        )
        RequestCategory.Custom -> Triple(
            R.string.home_category_badge_custom,
            Icons.Default.MoreHoriz,
            AmberPending,
        )
        null -> Triple(
            R.string.home_category_badge_general,
            Icons.Default.ShoppingBag,
            MaterialTheme.colorScheme.primary,
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.25f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(13.dp),
            )
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                ),
                color = color,
            )
        }
    }
}
