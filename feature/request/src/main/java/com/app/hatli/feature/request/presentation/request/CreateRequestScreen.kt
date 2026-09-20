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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.auth.HatliAuthTopBar
import com.app.hatli.core.designsystem.theme.HatliColors
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
            onEvent = viewModel::onEvent
        )

        // Full-screen Order Confirmation Screen
        RequestCreatedSuccessOverlay(
            isVisible = showSuccessOverlay,
            onReturnToHome = {
                showSuccessOverlay = false
                onSubmitted()
            }
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
        CreateWizardStep.Details -> state.category?.label().orEmpty()
        CreateWizardStep.FeeReview -> stringResource(R.string.create_request_step_review_title)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = when (state.step) {
            CreateWizardStep.Category -> 0.33f
            CreateWizardStep.Details -> 0.66f
            CreateWizardStep.FeeReview -> 1.0f
        },
        animationSpec = tween(durationMillis = 300),
        label = "WizardProgressAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        HatliAuthTopBar(
            title = stringResource(R.string.create_request_screen_title),
            onBack = { onEvent(CreateRequestUiEvent.Back) }
        )

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = HatliColors.BrandTeal,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            drawStopIndicator = {}
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(HatliSpacing.lg)
        ) {
            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stepTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = HatliColors.Teal900.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = when (state.step) {
                            CreateWizardStep.Category -> stringResource(R.string.create_request_wizard_step_1)
                            CreateWizardStep.Details -> stringResource(R.string.create_request_wizard_step_2)
                            CreateWizardStep.FeeReview -> stringResource(R.string.create_request_wizard_step_3)
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = HatliColors.BrandTeal,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(HatliSpacing.md))

            // Step Content with Animation
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
                            animationSpec = tween(300),
                            initialOffsetX = { width -> slideDirection * width }
                        ) + fadeIn(tween(300))).togetherWith(
                            slideOutHorizontally(
                                animationSpec = tween(300),
                                targetOffsetX = { width -> -slideDirection * width }
                            ) + fadeOut(tween(300))
                        )
                    },
                    label = "WizardStepAnimation"
                ) { step ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md)
                    ) {
                        when (step) {
                            CreateWizardStep.Category -> CategoryStep(onEvent)
                            CreateWizardStep.Details -> DetailsStep(state, onEvent)
                            CreateWizardStep.FeeReview -> FeeReviewStep(state, onEvent)
                        }
                    }
                }

                AnimatedVisibility(visible = state.fieldError != null) {
                    state.fieldError?.let { err ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = err,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(HatliSpacing.md)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(HatliSpacing.md))

            // Navigation CTA Buttons
            when (state.step) {
                CreateWizardStep.Category -> {
                    HatliSecondaryButton(
                        text = stringResource(R.string.create_request_cancel),
                        onClick = { onEvent(CreateRequestUiEvent.Back) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                CreateWizardStep.Details -> {
                    HatliPrimaryButton(
                        text = stringResource(R.string.create_request_continue),
                        onClick = { onEvent(CreateRequestUiEvent.Next) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(HatliSpacing.sm))
                    HatliSecondaryButton(
                        text = stringResource(R.string.create_request_back),
                        onClick = { onEvent(CreateRequestUiEvent.Back) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                CreateWizardStep.FeeReview -> {
                    if (state.isSubmitting) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = HatliColors.BrandTeal)
                        }
                    } else {
                        HatliPrimaryButton(
                            text = stringResource(R.string.create_request_confirm),
                            onClick = { onEvent(CreateRequestUiEvent.Submit) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(HatliSpacing.sm))
                        HatliSecondaryButton(
                            text = stringResource(R.string.create_request_back),
                            onClick = { onEvent(CreateRequestUiEvent.Back) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
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