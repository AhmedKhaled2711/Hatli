package com.app.hatli.feature.request.presentation.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.auth.HatliAuthTopBar
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.detail.components.CancelWorkflowSection
import com.app.hatli.feature.request.presentation.detail.components.CounterpartCard
import com.app.hatli.feature.request.presentation.detail.components.DetailActionsSection
import com.app.hatli.feature.request.presentation.detail.components.DetailStatusTrack
import com.app.hatli.feature.request.presentation.detail.components.EntranceAnimatedContainer
import com.app.hatli.feature.request.presentation.detail.components.OrderInfoCard
import com.app.hatli.feature.request.presentation.detail.components.StatusHeroCard
import com.app.hatli.feature.request.presentation.detail.components.TimelineCard

@Composable
fun RequestDetailRoute(
    onBack: () -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenRate: (String) -> Unit,
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

    RequestDetailScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
    )
}

@Composable
fun RequestDetailScreen(
    state: RequestDetailUiState,
    onEvent: (RequestDetailUiEvent) -> Unit,
    onBack: () -> Unit,
) {
    val isAr = java.util.Locale.getDefault().language == "ar"
    val req = state.request

    val hasPrimaryAction = (state.isRunner && state.runnerNextCta != null) ||
            (state.isRequester && req?.status == RequestStatus.Delivered) ||
            (state.isRequester && req?.status == RequestStatus.Completed && req.rating == null)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {
            AnimatedContent(
                targetState = Pair(state.isLoading, state.request == null),
                transitionSpec = {
                    fadeIn(animationSpec = tween(400)) togetherWith
                            fadeOut(animationSpec = tween(200))
                },
                label = "ScreenStateTransition",
            ) { (isLoading, isNull) ->
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }

                    isNull -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            HatliAuthTopBar(
                                title = stringResource(R.string.request_detail_title),
                                onBack = onBack,
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(HatliSpacing.xl),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ErrorOutline,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(56.dp),
                                    )
                                    Text(
                                        text = state.errorMessage ?: stringResource(R.string.request_detail_not_found),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center,
                                    )
                                    Spacer(modifier = Modifier.height(HatliSpacing.sm))
                                    HatliPrimaryButton(
                                        text = stringResource(R.string.request_detail_back),
                                        onClick = onBack,
                                        modifier = Modifier.fillMaxWidth(0.6f),
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        val activeReq = req!!

                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            // Fixed Top Bar
                            HatliAuthTopBar(
                                title = stringResource(R.string.request_detail_title),
                                onBack = onBack,
                            )

                            // Scrollable Content
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = HatliSpacing.lg, vertical = HatliSpacing.sm),
                                verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
                            ) {
                                EntranceAnimatedContainer(delayMillis = 0) {
                                    StatusHeroCard(
                                        req = activeReq,
                                        isAr = isAr,
                                    )
                                }

                                EntranceAnimatedContainer(delayMillis = 100) {
                                    DetailStatusTrack(
                                        req = activeReq,
                                        isAr = isAr,
                                    )
                                }

                                EntranceAnimatedContainer(delayMillis = 200) {
                                    CounterpartCard(
                                        req = activeReq,
                                        isRequester = state.isRequester,
                                        runnerRating = state.runnerRating,
                                        onOpenChat = { onEvent(RequestDetailUiEvent.OpenChat) },
                                    )
                                }

                                EntranceAnimatedContainer(delayMillis = 300) {
                                    OrderInfoCard(req = activeReq)
                                }

                                if (activeReq.statusHistory.isNotEmpty()) {
                                    EntranceAnimatedContainer(delayMillis = 400) {
                                        TimelineCard(
                                            req = activeReq,
                                            isAr = isAr,
                                        )
                                    }
                                }

                                EntranceAnimatedContainer(delayMillis = 500) {
                                    CancelWorkflowSection(
                                        state = state,
                                        onEvent = onEvent,
                                    )
                                }

                                Spacer(modifier = Modifier.height(HatliSpacing.sm))
                            }

                            // Sticky Action Bar Footer when primary CTA exists
                            if (hasPrimaryAction) {
                                Surface(
                                    shadowElevation = 8.dp,
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(HatliSpacing.md)
                                            .navigationBarsPadding(),
                                    ) {
                                        DetailActionsSection(
                                            req = activeReq,
                                            isRequester = state.isRequester,
                                            isRunner = state.isRunner,
                                            nextStatus = state.runnerNextStatus,
                                            nextCta = state.runnerNextCta,
                                            isConfirming = state.isConfirming,
                                            isAdvancing = state.isAdvancing,
                                            onEvent = onEvent,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
