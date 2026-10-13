package com.app.hatli.feature.request.presentation.rate

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.common.result.userMessage
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.component.auth.HatliAuthTopBar
import com.app.hatli.core.designsystem.theme.AmberPending
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.domain.usecase.request.SubmitRatingUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState
import com.app.hatli.feature.request.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RateUiState(
    val stars: Int = 5,
    val comment: String = "",
    val isSubmitting: Boolean = false,
) : UiState

sealed interface RateUiEvent : UiEvent {
    data class StarsChanged(val stars: Int) : RateUiEvent
    data class CommentChanged(val value: String) : RateUiEvent
    data object Submit : RateUiEvent
    data object Back : RateUiEvent
}

sealed interface RateUiEffect : UiEffect {
    data object Done : RateUiEffect
    data class ShowMessage(val message: String) : RateUiEffect
}

@HiltViewModel
class RateRunnerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val submitRating: SubmitRatingUseCase,
) : BaseViewModel<RateUiState, RateUiEvent, RateUiEffect>(RateUiState()) {
    private val requestId: String = checkNotNull(savedStateHandle["requestId"])

    override fun handleEvent(event: RateUiEvent) {
        when (event) {
            is RateUiEvent.StarsChanged -> setState { copy(stars = event.stars) }
            is RateUiEvent.CommentChanged -> setState { copy(comment = event.value) }
            RateUiEvent.Submit -> viewModelScope.launch {
                setState { copy(isSubmitting = true) }
                val current = uiState.value
                when (val result = submitRating(requestId, current.stars, current.comment.ifBlank { null })) {
                    is Result.Success -> {
                        setState { copy(isSubmitting = false) }
                        sendEffect(RateUiEffect.Done)
                    }
                    is Result.Error -> {
                        setState { copy(isSubmitting = false) }
                        sendEffect(RateUiEffect.ShowMessage(result.userMessage()))
                    }
                    Result.Loading -> Unit
                }
            }
            RateUiEvent.Back -> sendEffect(RateUiEffect.Done)
        }
    }
}

@Composable
fun RateRunnerRoute(
    onDone: () -> Unit,
    onMessage: (String) -> Unit,
    viewModel: RateRunnerViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            RateUiEffect.Done -> onDone()
            is RateUiEffect.ShowMessage -> onMessage(effect.message)
        }
    }

    RateRunnerScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onDone,
    )
}

@Composable
fun RateRunnerScreen(
    state: RateUiState,
    onEvent: (RateUiEvent) -> Unit,
    onBack: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {
            HatliAuthTopBar(
                title = stringResource(R.string.rate_runner_title),
                onBack = onBack,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(HatliSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(HatliSpacing.xs))

                // Avatar with Captain Badge
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.DirectionsBike,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(44.dp),
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = stringResource(R.string.request_detail_verified_captain),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape),
                    )
                }

                Text(
                    text = stringResource(R.string.rate_runner_subtitle),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )

                // Interactive 5-Star Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    (1..5).forEach { star ->
                        val isFilled = star <= state.stars
                        val starColor by animateColorAsState(
                            targetValue = if (isFilled) AmberPending else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            label = "starColor",
                        )

                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = stringResource(R.string.rate_runner_star_desc, star),
                            tint = starColor,
                            modifier = Modifier
                                .size(44.dp)
                                .clickable { onEvent(RateUiEvent.StarsChanged(star)) },
                        )
                    }
                }

                // Dynamic Rating Feedback
                val ratingFeedback = when (state.stars) {
                    5 -> stringResource(R.string.rate_feedback_5)
                    4 -> stringResource(R.string.rate_feedback_4)
                    3 -> stringResource(R.string.rate_feedback_3)
                    2 -> stringResource(R.string.rate_feedback_2)
                    else -> stringResource(R.string.rate_feedback_1)
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Text(
                        text = ratingFeedback,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    )
                }

                HatliCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(HatliSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                    ) {
                        HatliTextField(
                            value = state.comment,
                            onValueChange = { onEvent(RateUiEvent.CommentChanged(it)) },
                            label = stringResource(R.string.rate_runner_comment_hint),
                            placeholder = stringResource(R.string.rate_runner_comment_hint),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                HatliPrimaryButton(
                    text = stringResource(R.string.rate_runner_submit),
                    onClick = { onEvent(RateUiEvent.Submit) },
                    enabled = !state.isSubmitting,
                    modifier = Modifier.fillMaxWidth(),
                )

                HatliSecondaryButton(
                    text = stringResource(R.string.create_request_back),
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
