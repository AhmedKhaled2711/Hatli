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
    nextCta: String?,
    isConfirming: Boolean,
    isAdvancing: Boolean,
    onEvent: (RequestDetailUiEvent) -> Unit,
) {
    val canChat = req.status != RequestStatus.Pending && req.status != RequestStatus.Cancelled

    Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
        if (canChat) {
            BounceScaleClickable {
                HatliPrimaryButton(
                    text = stringResource(R.string.request_detail_open_chat),
                    onClick = { onEvent(RequestDetailUiEvent.OpenChat) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (isRunner && nextCta != null) {
            BounceScaleClickable {
                HatliPrimaryButton(
                    text = if (isAdvancing) {
                        stringResource(R.string.request_detail_updating)
                    } else nextCta,
                    onClick = { onEvent(RequestDetailUiEvent.AdvanceStatus) },
                    enabled = !isAdvancing,
                    modifier = Modifier.fillMaxWidth()
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
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (isRequester && req.status == RequestStatus.Completed && req.rating == null) {
            BounceScaleClickable {
                HatliSecondaryButton(
                    text = stringResource(R.string.request_detail_rate_runner),
                    onClick = { onEvent(RequestDetailUiEvent.OpenRate) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
