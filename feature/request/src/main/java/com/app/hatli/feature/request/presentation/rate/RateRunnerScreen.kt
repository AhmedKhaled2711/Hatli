package com.app.hatli.feature.request.presentation.rate

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.app.hatli.core.designsystem.theme.AmberPending
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas
import com.app.hatli.core.designsystem.theme.TealContainer
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

private val COMPLIMENT_TAGS = listOf(
    "سريع جداً ⚡",
    "أمين ومحترم 🤝",
    "حافظ الأماكن 📍",
    "تواصل ممتاز 📞"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RateRunnerRoute(
    onDone: () -> Unit,
    onMessage: (String) -> Unit,
    viewModel: RateRunnerViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTags = remember { mutableStateListOf<String>() }

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            RateUiEffect.Done -> onDone()
            is RateUiEffect.ShowMessage -> onMessage(effect.message)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhiteCanvas)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(HatliSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
    ) {
        Spacer(modifier = Modifier.height(HatliSpacing.sm))

        // Screen Header
        Text(
            text = "تقييم الكابتن",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = CharcoalDark,
            textAlign = TextAlign.Center
        )

        // Main Centered Rating Card
        HatliCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(HatliSpacing.lg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)
            ) {
                // 80dp Runner Avatar in Mint Frame with Captain Badge
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(TealContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBike,
                            contentDescription = null,
                            tint = BrandTeal,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = BrandTeal,
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.White, CircleShape)
                    )
                }

                Text(
                    text = "كيف كانت تجربتك مع كابتن التوصيل؟",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CharcoalDark,
                    textAlign = TextAlign.Center
                )

                // Interactive 5-Star Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { star ->
                        val isFilled = star <= state.stars
                        val starColor by animateColorAsState(
                            targetValue = if (isFilled) AmberPending else CharcoalMuted.copy(alpha = 0.4f),
                            label = "starColor"
                        )

                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Star $star",
                            tint = starColor,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { viewModel.onEvent(RateUiEvent.StarsChanged(star)) }
                        )
                    }
                }

                // Dynamic Arabic Micro-Copy Feedback
                val ratingFeedback = when (state.stars) {
                    5 -> "ممتاز جداً 🔥"
                    4 -> "جيد جداً 👍"
                    3 -> "متوسط 😐"
                    2 -> "سيء 😞"
                    else -> "سيء جداً ❌"
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CoralContainer
                ) {
                    Text(
                        text = ratingFeedback,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = BrandCoral,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                // Multi-select Compliment Tag Chips
                Text(
                    text = "ما الذي أعجبك في الكابتن؟",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = CharcoalMuted,
                    modifier = Modifier.padding(top = HatliSpacing.xs)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    COMPLIMENT_TAGS.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) BrandTeal else OffWhiteCanvas,
                            modifier = Modifier.clickable {
                                if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                            }
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) Color.White else CharcoalDark,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Multiline Optional Feedback Input
        HatliTextField(
            value = state.comment,
            onValueChange = { viewModel.onEvent(RateUiEvent.CommentChanged(it)) },
            label = "ملاحظات إضافية (اختياري)",
            placeholder = "اكتب رأيك أو أسرار تجربتك معنا...",
            maxLines = 4,
            modifier = Modifier.fillMaxWidth()
        )

        // Actions
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)
        ) {
            HatliPrimaryButton(
                text = if (state.isSubmitting) "جاري إرسال التقييم..." else "إرسال التقييم",
                onClick = { viewModel.onEvent(RateUiEvent.Submit) },
                enabled = !state.isSubmitting,
                modifier = Modifier.fillMaxWidth()
            )

            HatliSecondaryButton(
                text = "تخطي الآن",
                onClick = { viewModel.onEvent(RateUiEvent.Back) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
