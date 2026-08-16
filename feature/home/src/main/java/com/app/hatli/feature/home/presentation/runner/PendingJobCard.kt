package com.app.hatli.feature.home.presentation.runner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.feature.home.presentation.components.RequestRowCard

@Composable
fun PendingJobCard(
    summary: RequestSummary,
    isAr: Boolean,
    onAccept: () -> Unit,
    onDetails: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        RequestRowCard(
            summary = summary,
            isAr = isAr,
            onClick = onDetails,
        )

        HatliPrimaryButton(
            text = if (isAr) "قبول" else "Accept",
            onClick = onAccept,
        )
    }
}