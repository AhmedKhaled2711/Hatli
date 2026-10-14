package com.app.hatli.feature.request.presentation.request

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.detail.components.animateFloatAsState
import com.app.hatli.feature.request.presentation.request.components.CategoryStep
import com.app.hatli.feature.request.presentation.request.components.DetailsStep
import com.app.hatli.feature.request.presentation.request.components.FeeReviewStep
import com.app.hatli.feature.request.presentation.request.components.RequestCreatedSuccessOverlay

@Composable
fun CreateRequestRoute(
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
    onMessage: (String) -> Unit,
    viewModel: CreateRequestViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showSuccessOverlay by remember { mutableStateOf(false) }

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            CreateRequestUiEffect.NavigateBack -> onBack()
            CreateRequestUiEffect.Submitted -> {
                showSuccessOverlay = true
            }
            is CreateRequestUiEffect.ShowMessage -> onMessage(effect.message)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CreateRequestScreen(
            state = state,
            onEvent = viewModel::onEvent,
        )

        RequestCreatedSuccessOverlay(
            isVisible = showSuccessOverlay,
            onReturnToHome = {
                showSuccessOverlay = false
                onSubmitted()
            },
        )
    }
}

@Composable
fun CreateRequestScreen(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val stepTitle = when (state.step) {
        CreateWizardStep.Category -> stringResource(R.string.create_request_step_category_title)
        CreateWizardStep.Details -> state.category?.label() ?: stringResource(R.string.create_request_step_details_title)
        CreateWizardStep.FeeReview -> stringResource(R.string.create_request_step_review_title)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = when (state.step) {
            CreateWizardStep.Category -> 0.33f
            CreateWizardStep.Details -> 0.66f
            CreateWizardStep.FeeReview -> 1.0f
        },
        animationSpec = tween(durationMillis = 300),
        label = "WizardProgressAnimation",
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding(),
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = HatliSpacing.lg, vertical = HatliSpacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { onEvent(CreateRequestUiEvent.Back) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.request_detail_close),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    Text(
                        text = stringResource(R.string.create_request_screen_title),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Text(
                        text = when (state.step) {
                            CreateWizardStep.Category -> stringResource(R.string.create_request_wizard_step_1)
                            CreateWizardStep.Details -> stringResource(R.string.create_request_wizard_step_2)
                            CreateWizardStep.FeeReview -> stringResource(R.string.create_request_wizard_step_3)
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }

            // High-grade 3-step Wizard Visual Progress Track
            WizardStepIndicator(currentStep = state.step)

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = HatliSpacing.lg, vertical = HatliSpacing.md),
            ) {
                Text(
                    text = stepTitle,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(HatliSpacing.sm))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
                ) {
                    AnimatedContent(
                        targetState = state.step,
                        transitionSpec = {
                            val slideDirection = if (targetState.ordinal > initialState.ordinal) {
                                if (isRtl) -1 else 1
                            } else {
                                if (isRtl) 1 else -1
                            }
                            (slideInHorizontally(
                                animationSpec = tween(280),
                                initialOffsetX = { width -> slideDirection * width },
                            ) + fadeIn(tween(280))).togetherWith(
                                slideOutHorizontally(
                                    animationSpec = tween(280),
                                    targetOffsetX = { width -> -slideDirection * width },
                                ) + fadeOut(tween(280)),
                            )
                        },
                        label = "WizardStepAnimation",
                    ) { step ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
                        ) {
                            when (step) {
                                CreateWizardStep.Category -> CategoryStep(
                                    selectedCategory = state.category,
                                    onEvent = onEvent,
                                )
                                CreateWizardStep.Details -> DetailsStep(state, onEvent)
                                CreateWizardStep.FeeReview -> FeeReviewStep(state, onEvent)
                            }
                        }
                    }

                    AnimatedVisibility(visible = state.fieldError != null) {
                        state.fieldError?.let { err ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    text = err,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(HatliSpacing.md),
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(HatliSpacing.md))

                // Navigation CTA Buttons Footer
                when (state.step) {
                    CreateWizardStep.Category -> {
                        HatliSecondaryButton(
                            text = stringResource(R.string.create_request_cancel),
                            onClick = { onEvent(CreateRequestUiEvent.Back) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    CreateWizardStep.Details -> {
                        HatliPrimaryButton(
                            text = stringResource(R.string.create_request_continue),
                            onClick = { onEvent(CreateRequestUiEvent.Next) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(HatliSpacing.xs))
                        HatliSecondaryButton(
                            text = stringResource(R.string.create_request_back),
                            onClick = { onEvent(CreateRequestUiEvent.Back) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    CreateWizardStep.FeeReview -> {
                        HatliPrimaryButton(
                            text = stringResource(R.string.create_request_confirm),
                            onClick = { onEvent(CreateRequestUiEvent.Submit) },
                            modifier = Modifier.fillMaxWidth(),
                            isLoading = state.isSubmitting,
                        )
                        Spacer(modifier = Modifier.height(HatliSpacing.xs))
                        HatliSecondaryButton(
                            text = stringResource(R.string.create_request_back),
                            onClick = { onEvent(CreateRequestUiEvent.Back) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !state.isSubmitting,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WizardStepIndicator(currentStep: CreateWizardStep) {
    val steps = listOf(
        CreateWizardStep.Category to stringResource(R.string.create_request_step_1_name),
        CreateWizardStep.Details to stringResource(R.string.create_request_step_2_name),
        CreateWizardStep.FeeReview to stringResource(R.string.create_request_step_3_name),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HatliSpacing.lg, vertical = HatliSpacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        steps.forEachIndexed { index, (step, label) ->
            val isCompleted = step.ordinal < currentStep.ordinal
            val isCurrent = step == currentStep

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> MaterialTheme.colorScheme.primary
                                isCurrent -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                        )
                        .border(
                            width = if (isCurrent) 1.5.dp else 0.dp,
                            color = if (isCurrent) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(14.dp),
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                            ),
                            color = if (isCurrent) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp,
                    ),
                    color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(2.dp)
                        .background(
                            if (index < currentStep.ordinal) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                        ),
                )
            }
        }
    }
}

@Composable
fun RequestCategory.label(): String = when (this) {
    RequestCategory.Shopping -> stringResource(R.string.category_shopping_label)
    RequestCategory.Pickup -> stringResource(R.string.category_pickup_label)
    RequestCategory.Bills -> stringResource(R.string.category_bills_label)
    RequestCategory.Custom -> stringResource(R.string.category_custom_label)
}

@Composable
fun RequestCategory.subtitle(): String = when (this) {
    RequestCategory.Shopping -> stringResource(R.string.category_shopping_subtitle)
    RequestCategory.Pickup -> stringResource(R.string.category_pickup_subtitle)
    RequestCategory.Bills -> stringResource(R.string.category_bills_subtitle)
    RequestCategory.Custom -> stringResource(R.string.category_custom_subtitle)
}

fun RequestCategory.icon(): ImageVector = when (this) {
    RequestCategory.Shopping -> Icons.Default.ShoppingBag
    RequestCategory.Pickup -> Icons.Default.LocationOn
    RequestCategory.Bills -> Icons.Default.Payment
    RequestCategory.Custom -> Icons.Default.Category
}