package com.app.hatli.feature.request.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.lifecycleSteps
import com.app.hatli.core.model.requesterLabelAr
import com.app.hatli.core.model.shortLabelAr
import com.app.hatli.core.model.shortLabelEn
import com.app.hatli.core.model.stepperIndex
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun RequestDetailRoute(
    onBack: () -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenRate: (String) -> Unit = {},
    onMessage: (String) -> Unit,
    viewModel: RequestDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            RequestDetailUiEffect.NavigateBack -> onBack()
            is RequestDetailUiEffect.NavigateChat -> onOpenChat(effect.requestId)
            is RequestDetailUiEffect.NavigateRate -> onOpenRate(effect.requestId)
            is RequestDetailUiEffect.ShowMessage -> onMessage(effect.message)
        }
    }
    RequestDetailScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun RequestDetailScreen(
    state: RequestDetailUiState,
    onEvent: (RequestDetailUiEvent) -> Unit,
) {
    val isAr = LocalConfiguration.current.locales[0]?.language == "ar"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(HatliSpacing.lg)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text(
            text = if (isAr) "تفاصيل الطلب" else "Request details",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            state.request == null -> Text(
                text = if (isAr) "الطلب غير موجود" else "Request not found",
                color = MaterialTheme.colorScheme.error,
            )
            else -> {
                val req = state.request
                DetailHeaderCard(req = req, isAr = isAr)
                DetailStatusTrack(req = req, isAr = isAr)
                if (req.statusHistory.isNotEmpty()) {
                    Text(
                        text = if (isAr) "الجدول الزمني" else "Timeline",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    HatliCard {
                        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
                            req.statusHistory.forEach { entry ->
                                Text(
                                    text = if (isAr) entry.status.shortLabelAr() else entry.status.shortLabelEn(),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                }
                DetailActions(
                    req = req,
                    isRequester = state.isRequester,
                    isRunner = state.isRunner,
                    isAr = isAr,
                    nextCta = state.runnerNextCta,
                    isConfirming = state.isConfirming,
                    isAdvancing = state.isAdvancing,
                    onEvent = onEvent,
                )
                CancelSection(state = state, isAr = isAr, onEvent = onEvent)
            }
        }
        Spacer(modifier = Modifier.height(HatliSpacing.sm))
        HatliSecondaryButton(
            text = if (isAr) "رجوع" else "Back",
            onClick = { onEvent(RequestDetailUiEvent.Back) },
        )
    }
}

@Composable
private fun DetailHeaderCard(req: Request, isAr: Boolean) {
    HatliCard {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            Text(req.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                text = if (isAr) {
                    req.status.requesterLabelAr(req.runnerName)
                } else {
                    req.status.shortLabelEn()
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
            )
            Text(req.description, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = if (isAr) {
                    "الرسوم: ${req.deliveryFee.toInt()} جنيه"
                } else {
                    "Fee: ${req.deliveryFee.toInt()} EGP"
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            req.runnerName?.let {
                Text(
                    text = if (isAr) "الـ Runner: $it" else "Runner: $it",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            req.requesterName?.let {
                Text(
                    text = if (isAr) "صاحب الطلب: $it" else "Requester: $it",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun DetailStatusTrack(req: Request, isAr: Boolean) {
    if (req.status == RequestStatus.Pending || req.status == RequestStatus.Cancelled) return
    val steps = lifecycleSteps(req.category)
    val current = req.status.stepperIndex(req.category).coerceIn(0, steps.lastIndex)
    Text(
        text = if (isAr) "حالة الطلب" else "Status",
        style = MaterialTheme.typography.titleMedium,
    )
    HatliCard {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                steps.forEachIndexed { index, _ ->
                    val reached = index <= current
                    val isCurrent = index == current
                    Box(
                        modifier = Modifier
                            .size(if (isCurrent) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCurrent -> MaterialTheme.colorScheme.primary
                                    reached -> MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                                    else -> MaterialTheme.colorScheme.outlineVariant
                                },
                            ),
                    )
                    if (index < steps.lastIndex) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(2.dp)
                                .background(
                                    if (index < current) {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                                    } else {
                                        MaterialTheme.colorScheme.outlineVariant
                                    },
                                ),
                        )
                    }
                }
            }
            steps.forEachIndexed { index, step ->
                val label = if (isAr) step.shortLabelAr() else step.shortLabelEn()
                Text(
                    text = if (index <= current) "✓ $label" else label,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (index <= current) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = if (index == current) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
private fun DetailActions(
    req: Request,
    isRequester: Boolean,
    isRunner: Boolean,
    isAr: Boolean,
    nextCta: String?,
    isConfirming: Boolean,
    isAdvancing: Boolean,
    onEvent: (RequestDetailUiEvent) -> Unit,
) {
    if (req.status != RequestStatus.Pending && req.status != RequestStatus.Cancelled) {
        HatliPrimaryButton(
            text = if (isAr) "فتح المحادثة" else "Open chat",
            onClick = { onEvent(RequestDetailUiEvent.OpenChat) },
        )
    }
    if (isRunner && nextCta != null) {
        HatliPrimaryButton(
            text = if (isAdvancing) {
                if (isAr) "جاري التحديث…" else "Updating…"
            } else {
                nextCta
            },
            onClick = { onEvent(RequestDetailUiEvent.AdvanceStatus) },
            enabled = !isAdvancing,
        )
    }
    if (isRequester && req.status == RequestStatus.Delivered) {
        HatliPrimaryButton(
            text = if (isConfirming) {
                if (isAr) "جاري التأكيد…" else "Confirming…"
            } else if (isAr) {
                "تأكيد الاستلام"
            } else {
                "Confirm received"
            },
            onClick = { onEvent(RequestDetailUiEvent.ConfirmDelivery) },
            enabled = !isConfirming,
        )
    }
    if (isRequester && req.status == RequestStatus.Completed && req.rating == null) {
        HatliPrimaryButton(
            text = if (isAr) "قيّم الـ Runner" else "Rate Runner",
            onClick = { onEvent(RequestDetailUiEvent.OpenRate) },
        )
    }
}

@Composable
private fun CancelSection(
    state: RequestDetailUiState,
    isAr: Boolean,
    onEvent: (RequestDetailUiEvent) -> Unit,
) {
    val req = state.request ?: return
    val canCancel = req.status !in setOf(
        RequestStatus.Completed,
        RequestStatus.Cancelled,
        RequestStatus.Delivered,
    )
    if (!canCancel) return
    if (state.showCancelSheet) {
        HatliTextField(
            value = state.cancelReason,
            onValueChange = { onEvent(RequestDetailUiEvent.CancelReasonChanged(it)) },
            label = if (isAr) "سبب الإلغاء" else "Cancel reason",
        )
        HatliPrimaryButton(
            text = if (state.isCancelling) {
                if (isAr) "جاري الإلغاء…" else "Cancelling…"
            } else if (isAr) {
                "تأكيد الإلغاء"
            } else {
                "Confirm cancel"
            },
            onClick = { onEvent(RequestDetailUiEvent.ConfirmCancel) },
            enabled = !state.isCancelling && state.cancelReason.isNotBlank(),
        )
        HatliSecondaryButton(
            text = if (isAr) "إبقاء الطلب" else "Keep request",
            onClick = { onEvent(RequestDetailUiEvent.HideCancel) },
        )
    } else {
        HatliSecondaryButton(
            text = if (isAr) "إلغاء الطلب" else "Cancel request",
            onClick = { onEvent(RequestDetailUiEvent.ShowCancel) },
        )
    }
}
