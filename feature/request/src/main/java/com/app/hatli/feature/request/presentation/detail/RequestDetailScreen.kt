package com.app.hatli.feature.request.presentation.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.auth.HatliAuthTopBar
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.detail.components.BounceScaleClickable
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
    RequestDetailScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}

@Composable
fun RequestDetailScreen(
    state: RequestDetailUiState,
    onEvent: (RequestDetailUiEvent) -> Unit,
    onBack: () -> Unit = { onEvent(RequestDetailUiEvent.Back) }
) {
    val isAr = androidx.compose.ui.platform.LocalLayoutDirection.current == androidx.compose.ui.unit.LayoutDirection.Rtl

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AnimatedContent(
            targetState = Pair(state.isLoading, state.request == null),
            transitionSpec = {
                fadeIn(animationSpec = tween(400)) togetherWith
                        fadeOut(animationSpec = tween(200))
            },
            label = "ScreenStateTransition"
        ) { (isLoading, isNull) ->
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = HatliColors.BrandTeal
                        )
                    }
                }

                isNull -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.request_detail_not_found),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                else -> {
                    val req = state.request!!

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                    ) {
                        // Fixed Top Bar
                        HatliAuthTopBar(
                            title = stringResource(R.string.request_detail_title),
                            onBack = onBack
                        )

                        // Scrollable Content
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(HatliSpacing.lg),
                            verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg)
                        ) {
                            EntranceAnimatedContainer(delayMillis = 0) {
                                StatusHeroCard(
                                    req = req,
                                    isAr = isAr
                                )
                            }

                            EntranceAnimatedContainer(delayMillis = 100) {
                                DetailStatusTrack(
                                    req = req,
                                    isAr = isAr
                                )
                            }

                            EntranceAnimatedContainer(delayMillis = 200) {
                                OrderInfoCard(req = req)
                            }

                            EntranceAnimatedContainer(delayMillis = 300) {
                                CounterpartCard(
                                    req = req,
                                    isRequester = state.isRequester
                                )
                            }

                            if (req.statusHistory.isNotEmpty()) {
                                EntranceAnimatedContainer(delayMillis = 400) {
                                    TimelineCard(
                                        req = req,
                                        isAr = isAr
                                    )
                                }
                            }

                            EntranceAnimatedContainer(delayMillis = 500) {
                                DetailActionsSection(
                                    req = req,
                                    isRequester = state.isRequester,
                                    isRunner = state.isRunner,
                                    nextCta = state.runnerNextCta,
                                    isConfirming = state.isConfirming,
                                    isAdvancing = state.isAdvancing,
                                    onEvent = onEvent
                                )
                            }

                            EntranceAnimatedContainer(delayMillis = 600) {
                                CancelWorkflowSection(
                                    state = state,
                                    onEvent = onEvent
                                )
                            }

                            Spacer(
                                modifier = Modifier.height(HatliSpacing.sm)
                            )

                            BounceScaleClickable {
                                HatliSecondaryButton(
                                    text = stringResource(R.string.request_detail_back),
                                    onClick = {
                                        onEvent(RequestDetailUiEvent.Back)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
