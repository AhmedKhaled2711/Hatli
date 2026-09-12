package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.requesterLabelAr
import com.app.hatli.core.model.shortLabelEn
import com.app.hatli.feature.request.R

@Composable
fun StatusHeroCard(req: Request, isAr: Boolean) {
    val extendedColors = LocalHatliExtendedColors.current
    val isCancelled = req.status == RequestStatus.Cancelled
    val isCompleted = req.status == RequestStatus.Completed

    val bannerBg by animateColorAsState(
        targetValue = when {
            isCancelled -> extendedColors.warningContainer
            isCompleted -> extendedColors.successContainer
            else -> HatliColors.Teal900
        },
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "heroColorMorph"
    )

    val contentColor = when {
        isCancelled -> extendedColors.warning
        isCompleted -> extendedColors.success
        else -> HatliColors.BrandTeal
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bannerBg,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(HatliSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.request_detail_current_status),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isCancelled || isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else HatliColors.Teal100
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = contentColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (isAr) req.status.requesterLabelAr(req.runnerName) else req.status.shortLabelEn(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = contentColor
                    )
                }
            }

            Text(
                text = req.title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = if (isCancelled || isCompleted) MaterialTheme.colorScheme.onSurface else HatliColors.White
            )
        }
    }
}
