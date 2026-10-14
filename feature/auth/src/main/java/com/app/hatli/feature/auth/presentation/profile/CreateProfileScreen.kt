package com.app.hatli.feature.auth.presentation.profile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.feature.auth.R
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun CreateProfileRoute(
    onHome: () -> Unit,
    onRunnerApproval: () -> Unit = onHome,
    viewModel: CreateProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            CreateProfileUiEffect.GoHome -> onHome()
            CreateProfileUiEffect.GoRunnerApproval -> onRunnerApproval()
        }
    }
    CreateProfileScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun CreateProfileScreen(
    state: CreateProfileUiState,
    onEvent: (CreateProfileUiEvent) -> Unit,
) {
    BackHandler(enabled = !state.isFirstStep) {
        onEvent(CreateProfileUiEvent.Back)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = state.progress,
        animationSpec = tween(durationMillis = 350),
        label = "profileSetupProgress",
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = OffWhiteCanvas,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .padding(horizontal = HatliSpacing.lg, vertical = HatliSpacing.md),
        ) {
            ProfileSetupHeader(
                isRunner = state.isRunner,
                stepTitle = localizedStepTitle(state.step),
                stepDescription = localizedStepDescription(state.step),
                stepNumber = state.stepNumber,
                totalSteps = state.totalSteps,
                progress = animatedProgress,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.md))

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
}

@Composable
private fun ProfileSetupHeader(
    isRunner: Boolean,
    stepTitle: String,
    stepDescription: String,
    stepNumber: Int,
    totalSteps: Int,
    progress: Float,
) {
    val stepCounterText = stringResource(R.string.profile_setup_step_counter, stepNumber, totalSteps)
    Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (isRunner) {
                    stringResource(R.string.profile_setup_title_runner)
                } else {
                    stringResource(R.string.profile_setup_title_requester)
                },
                style = MaterialTheme.typography.labelLarge.copy(
                    color = BrandCoral,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Text(
                text = stepCounterText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CharcoalMuted,
                    fontSize = 13.sp,
                ),
            )
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .semantics {
                    contentDescription = stepCounterText
                },
            color = BrandCoral,
            trackColor = BrandCoral.copy(alpha = 0.15f),
        )

        Spacer(modifier = Modifier.height(HatliSpacing.xxs))

        Text(
            text = stepTitle,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = CharcoalDark,
                fontSize = 22.sp,
            ),
        )
        Text(
            text = stepDescription,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = CharcoalMuted,
                lineHeight = 20.sp,
            ),
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
                text = stringResource(R.string.profile_setup_btn_prev),
                onClick = onBack,
                modifier = Modifier.weight(1f),
                enabled = !isLoading,
            )
        }
        HatliPrimaryButton(
            text = when {
                isLoading -> stringResource(R.string.profile_setup_btn_saving)
                isLastStep -> stringResource(R.string.profile_setup_btn_complete)
                else -> stringResource(R.string.profile_setup_btn_next)
            },
            onClick = onNext,
            modifier = Modifier.weight(1f),
            enabled = !isLoading,
        )
    }
}

@Composable
private fun localizedStepTitle(step: ProfileSetupStep): String = when (step) {
    ProfileSetupStep.Personal -> stringResource(R.string.profile_setup_step_personal_title)
    ProfileSetupStep.Address -> stringResource(R.string.profile_setup_step_address_title)
    ProfileSetupStep.Transport -> stringResource(R.string.profile_setup_step_transport_title)
    ProfileSetupStep.Verification -> stringResource(R.string.profile_setup_step_verification_title)
}

@Composable
private fun localizedStepDescription(step: ProfileSetupStep): String = when (step) {
    ProfileSetupStep.Personal -> stringResource(R.string.profile_setup_step_personal_sub)
    ProfileSetupStep.Address -> stringResource(R.string.profile_setup_step_address_sub)
    ProfileSetupStep.Transport -> stringResource(R.string.profile_setup_step_transport_sub)
    ProfileSetupStep.Verification -> stringResource(R.string.profile_setup_step_verification_sub)
}

