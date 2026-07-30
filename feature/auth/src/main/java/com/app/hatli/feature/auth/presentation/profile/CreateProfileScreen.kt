package com.app.hatli.feature.auth.presentation.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun CreateProfileRoute(
    onHome: () -> Unit,
    viewModel: CreateProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            CreateProfileUiEffect.GoHome -> onHome()
        }
    }
    CreateProfileScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun CreateProfileScreen(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = tween(durationMillis = 350),
        label = "profileSetupProgress",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.lg),
    ) {
        ProfileSetupHeader(
            roleLabel = state.role.name,
            stepTitle = state.stepTitle,
            stepDescription = state.stepDescription,
            stepNumber = state.stepNumber,
            totalSteps = state.totalSteps,
            progress = animatedProgress,
        )

        Spacer(modifier = Modifier.height(HatliSpacing.lg))

        AnimatedContent(
            targetState = state.step,
            transitionSpec = {
                val forward = targetState.ordinal >= initialState.ordinal
                if (forward) {
                    (slideInHorizontally { it / 3 } + fadeIn()) togetherWith
                        (slideOutHorizontally { -it / 3 } + fadeOut())
                } else {
                    (slideInHorizontally { -it / 3 } + fadeIn()) togetherWith
                        (slideOutHorizontally { it / 3 } + fadeOut())
                }
            },
            label = "profileSetupStep",
            modifier = Modifier.weight(1f),
        ) { step ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
            ) {
                when (step) {
                    ProfileSetupStep.Personal -> PersonalInfoStep(state = state, onEvent = onEvent)
                    ProfileSetupStep.Address -> AddressStep(state = state, onEvent = onEvent)
                    ProfileSetupStep.Transport -> TransportStep(state = state, onEvent = onEvent)
                    ProfileSetupStep.Verification -> VerificationStep(state = state, onEvent = onEvent)
                }
            }
        }

        state.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = HatliSpacing.sm),
            )
        }

        Spacer(modifier = Modifier.height(HatliSpacing.md))

        ProfileSetupFooter(
            isFirstStep = state.isFirstStep,
            isLastStep = state.isLastStep,
            isLoading = state.isLoading,
            onBack = { onEvent(CreateProfileUiEvent.Back) },
            onNext = {
                if (state.isLastStep) onEvent(CreateProfileUiEvent.Submit)
                else onEvent(CreateProfileUiEvent.Next)
            },
        )
    }
}

@Composable
private fun ProfileSetupHeader(
    roleLabel: String,
    stepTitle: String,
    stepDescription: String,
    stepNumber: Int,
    totalSteps: Int,
    progress: Float,
) {
    Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
        Text(
            text = "Setting up as $roleLabel",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .semantics {
                    contentDescription = "Step $stepNumber of $totalSteps"
                },
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Text(
            text = "Step $stepNumber of $totalSteps",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stepTitle,
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = stepDescription,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ProfileSetupFooter(
    isFirstStep: Boolean,
    isLastStep: Boolean,
    isLoading: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (!isFirstStep) {
            HatliSecondaryButton(
                text = "Back",
                onClick = onBack,
                modifier = Modifier.weight(1f),
                enabled = !isLoading,
            )
        }
        HatliPrimaryButton(
            text = when {
                isLoading -> "Saving…"
                isLastStep -> "Finish"
                else -> "Next"
            },
            onClick = onNext,
            modifier = Modifier.weight(1f),
            enabled = !isLoading,
        )
    }
}
