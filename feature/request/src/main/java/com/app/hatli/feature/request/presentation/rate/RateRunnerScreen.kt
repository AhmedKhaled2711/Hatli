package com.app.hatli.feature.request.presentation.rate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.domain.usecase.request.SubmitRatingUseCase
import com.app.hatli.core.ui.base.BaseViewModel
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.core.ui.base.UiEffect
import com.app.hatli.core.ui.base.UiEvent
import com.app.hatli.core.ui.base.UiState
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
            is RateUiEvent.StarsChanged -> setState { copy(stars = event.stars.coerceIn(1, 5)) }
            is RateUiEvent.CommentChanged -> setState { copy(comment = event.value) }
            RateUiEvent.Submit -> viewModelScope.launch {
                setState { copy(isSubmitting = true) }
                when (
                    val result = submitRating(
                        requestId,
                        uiState.value.stars,
                        uiState.value.comment,
                    )
                ) {
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text("Rate Runner", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
        HatliCard {
            Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                Text("Stars: ${state.stars}", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                    (1..5).forEach { star ->
                        HatliSecondaryButton(
                            text = "$star",
                            onClick = { viewModel.onEvent(RateUiEvent.StarsChanged(star)) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
        HatliTextField(
            value = state.comment,
            onValueChange = { viewModel.onEvent(RateUiEvent.CommentChanged(it)) },
            label = "Comment (optional)",
        )
        HatliPrimaryButton(
            text = if (state.isSubmitting) "Submitting…" else "Submit rating",
            onClick = { viewModel.onEvent(RateUiEvent.Submit) },
            enabled = !state.isSubmitting,
        )
        HatliSecondaryButton(text = "Skip", onClick = { viewModel.onEvent(RateUiEvent.Back) })
    }
}
