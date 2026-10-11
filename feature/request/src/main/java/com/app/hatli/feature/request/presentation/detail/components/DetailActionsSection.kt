package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.detail.RequestDetailUiEvent

@Composable
fun DetailActionsSection(
    req: Request,
    isRequester: Boolean,
    isRunner: Boolean,
    nextStatus: RequestStatus?,
    nextCta: String?,
    isConfirming: Boolean,
    isAdvancing: Boolean,
    onEvent: (RequestDetailUiEvent) -> Unit,
) {
    val runnerButtonText = when (nextStatus) {
        RequestStatus.InProgress -> stringResource(R.string.request_detail_runner_start_work)
        RequestStatus.Purchased -> stringResource(R.string.request_detail_runner_mark_purchased)
        RequestStatus.OnTheWay -> stringResource(R.string.request_detail_runner_on_the_way)
        RequestStatus.Delivered -> stringResource(R.string.request_detail_runner_mark_delivered)
        else -> nextCta ?: ""
    }

    Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
        if (isRunner && (nextStatus != null || nextCta != null)) {
            BounceScaleClickable {
                HatliPrimaryButton(
                    text = if (isAdvancing) {
                        stringResource(R.string.request_detail_updating)
                    } else runnerButtonText,
                    onClick = { onEvent(RequestDetailUiEvent.AdvanceStatus) },
                    enabled = !isAdvancing,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        if (isRequester && req.status == RequestStatus.Delivered) {
            BounceScaleClickable {
                HatliPrimaryButton(
                    text = if (isConfirming) {
                        stringResource(R.string.request_detail_confirming)
                    } else {
                        stringResource(R.string.request_detail_confirm_delivery)
                    },
                    onClick = { onEvent(RequestDetailUiEvent.ConfirmDelivery) },
                    enabled = !isConfirming,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        if (isRequester && req.status == RequestStatus.Completed && req.rating == null) {
            BounceScaleClickable {
                HatliPrimaryButton(
                    text = stringResource(R.string.request_detail_rate_runner),
                    onClick = { onEvent(RequestDetailUiEvent.OpenRate) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
