package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.feature.request.R

@Composable
fun RequestStatus.timelineLabel(): String = when (this) {
    RequestStatus.Pending -> stringResource(R.string.request_status_step_pending)
    RequestStatus.Accepted -> stringResource(R.string.request_status_step_accepted)
    RequestStatus.InProgress -> stringResource(R.string.request_status_step_in_progress)
    RequestStatus.Purchased -> stringResource(R.string.request_status_step_purchased)
    RequestStatus.OnTheWay -> stringResource(R.string.request_status_step_on_the_way)
    RequestStatus.Delivered -> stringResource(R.string.request_status_step_delivered)
    RequestStatus.Completed -> stringResource(R.string.request_status_step_completed)
    RequestStatus.Cancelled -> stringResource(R.string.request_status_step_cancelled)
}

@Composable
fun TimelineCard(req: Request, isAr: Boolean = false) {
    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)
        ) {
            Text(
                text = stringResource(R.string.request_detail_timeline_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            req.statusHistory.forEach { entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(HatliColors.BrandTeal)
                    )
                    Text(
                        text = entry.status.timelineLabel(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
