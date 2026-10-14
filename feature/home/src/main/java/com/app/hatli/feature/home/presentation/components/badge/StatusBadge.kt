package com.app.hatli.feature.home.presentation.components.badge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.AmberPending
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.formatting.isAppArabic

@Composable
fun StatusBadge(
    status: RequestStatus,
    isAr: Boolean = isAppArabic(),
    runnerName: String? = null,
    modifier: Modifier = Modifier,
) {
    val (bgColor, contentColor, labelRes) = when (status) {
        RequestStatus.Pending -> Triple(
            AmberPending.copy(alpha = 0.14f),
            AmberPending,
            R.string.home_status_pending,
        )
        RequestStatus.Accepted -> Triple(
            BrandCoral.copy(alpha = 0.12f),
            BrandCoral,
            R.string.home_status_accepted,
        )
        RequestStatus.InProgress -> Triple(
            BrandCoral.copy(alpha = 0.14f),
            BrandCoral,
            R.string.home_status_in_progress,
        )
        RequestStatus.Purchased -> Triple(
            Color(0xFF7B1FA2).copy(alpha = 0.12f),
            Color(0xFF7B1FA2),
            R.string.home_status_purchased,
        )
        RequestStatus.OnTheWay -> Triple(
            BrandTeal.copy(alpha = 0.14f),
            BrandTeal,
            R.string.home_status_on_the_way,
        )
        RequestStatus.Delivered -> Triple(
            Color(0xFF2E7D32).copy(alpha = 0.14f),
            Color(0xFF2E7D32),
            R.string.home_status_delivered,
        )
        RequestStatus.Completed -> Triple(
            Color(0xFF2E7D32).copy(alpha = 0.12f),
            Color(0xFF2E7D32),
            R.string.home_status_completed,
        )
        RequestStatus.Cancelled -> Triple(
            MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
            MaterialTheme.colorScheme.error,
            R.string.home_status_cancelled,
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
        border = BorderStroke(0.5.dp, contentColor.copy(alpha = 0.3f)),
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
            ),
            color = contentColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}
