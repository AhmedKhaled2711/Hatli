package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.detail.RequestDetailUiEvent
import com.app.hatli.feature.request.presentation.detail.RequestDetailUiState

@Composable
fun CancelWorkflowSection(
    state: RequestDetailUiState,
    onEvent: (RequestDetailUiEvent) -> Unit,
) {
    val req = state.request ?: return
    val extendedColors = LocalHatliExtendedColors.current
    val canCancel = req.status !in setOf(
        RequestStatus.Completed,
        RequestStatus.Cancelled,
        RequestStatus.Delivered,
    )

    if (!canCancel) return

    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = state.showCancelSheet,
            enter = fadeIn() + expandVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
            exit = fadeOut() + shrinkVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium))
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = extendedColors.warningContainer.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = HatliSpacing.sm)
            ) {
                Column(
                    modifier = Modifier.padding(HatliSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)
                ) {
                    Text(
                        text = stringResource(R.string.request_detail_cancel_title),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = extendedColors.warning
                    )

                    HatliTextField(
                        value = state.cancelReason,
                        onValueChange = { onEvent(RequestDetailUiEvent.CancelReasonChanged(it)) },
                        label = stringResource(R.string.request_detail_cancel_reason_label),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs)
                    ) {
                        BounceScaleClickable {
                            HatliPrimaryButton(
                                text = if (state.isCancelling) {
                                    stringResource(R.string.request_detail_cancelling)
                                } else {
                                    stringResource(R.string.request_detail_confirm_cancel)
                                },
                                onClick = { onEvent(RequestDetailUiEvent.ConfirmCancel) },
                                enabled = !state.isCancelling && state.cancelReason.isNotBlank(),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        BounceScaleClickable {
                            HatliSecondaryButton(
                                text = stringResource(R.string.request_detail_dismiss),
                                onClick = { onEvent(RequestDetailUiEvent.HideCancel) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        if (!state.showCancelSheet) {
            BounceScaleClickable {
                HatliSecondaryButton(
                    text = stringResource(R.string.request_detail_cancel_order),
                    onClick = { onEvent(RequestDetailUiEvent.ShowCancel) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
