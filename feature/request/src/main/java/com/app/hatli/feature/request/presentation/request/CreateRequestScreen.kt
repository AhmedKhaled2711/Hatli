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
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas
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

        // Full-screen Order Confirmation Overlay
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
        CreateWizardStep.Category -> "اختر نوع الخدمة المطلوب"
        CreateWizardStep.Details -> state.category?.label() ?: "تفاصيل الطلب"
        CreateWizardStep.FeeReview -> "مراجعة وحساب التكلفة"
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
        color = OffWhiteCanvas,
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
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { onEvent(CreateRequestUiEvent.Back) },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = CharcoalDark,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    Text(
                        text = "طلب جديد",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark,
                        ),
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(CoralContainer)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = when (state.step) {
                            CreateWizardStep.Category -> "الخطوة 1 من 3"
                            CreateWizardStep.Details -> "الخطوة 2 من 3"
                            CreateWizardStep.FeeReview -> "الخطوة 3 من 3"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = BrandCoral,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                        ),
                    )
                }
            }

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = BrandCoral,
                trackColor = CoralContainer,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = HatliSpacing.lg, vertical = HatliSpacing.md),
            ) {
                // Step Title & Subtitle Header
                Text(
                    text = stepTitle,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CharcoalDark,
                        fontSize = 20.sp,
                    ),
                )

                Spacer(modifier = Modifier.height(HatliSpacing.sm))

                // Scrollable Wizard Content Body
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
                                CreateWizardStep.Category -> CategoryStep(onEvent)
                                CreateWizardStep.Details -> DetailsStep(state, onEvent)
                                CreateWizardStep.FeeReview -> FeeReviewStep(state, onEvent)
                            }
                        }
                    }

                    AnimatedVisibility(visible = state.fieldError != null) {
                        state.fieldError?.let { err ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    text = err,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
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
                            text = "إلغاء",
                            onClick = { onEvent(CreateRequestUiEvent.Back) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    CreateWizardStep.Details -> {
                        HatliPrimaryButton(
                            text = "متابعة وإدخال التفاصيل",
                            onClick = { onEvent(CreateRequestUiEvent.Next) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(HatliSpacing.xs))
                        HatliSecondaryButton(
                            text = "السابق",
                            onClick = { onEvent(CreateRequestUiEvent.Back) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    CreateWizardStep.FeeReview -> {
                        if (state.isSubmitting) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(color = BrandCoral)
                            }
                        } else {
                            HatliPrimaryButton(
                                text = "تأكيد وإرسال الطلب الآن",
                                onClick = { onEvent(CreateRequestUiEvent.Submit) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Spacer(modifier = Modifier.height(HatliSpacing.xs))
                            HatliSecondaryButton(
                                text = "السابق",
                                onClick = { onEvent(CreateRequestUiEvent.Back) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RequestCategory.label(): String = when (this) {
    RequestCategory.Shopping -> "شراء طلبات وسوبر ماركت"
    RequestCategory.Pickup -> "توصيل طرد ومستندات"
    RequestCategory.Bills -> "دفع فواتير ومرافق"
    RequestCategory.Custom -> "مشوار خاص وطلب مخصص"
}

@Composable
fun RequestCategory.subtitle(): String = when (this) {
    RequestCategory.Shopping -> "بقالة، صيدلية، مطاعم ومستلزمات منزلية"
    RequestCategory.Pickup -> "استلام ونقل الأغراض بين الأحياء بسرعة"
    RequestCategory.Bills -> "سداد الكهرباء، المياه والخدمات الحكومية"
    RequestCategory.Custom -> "أي مهمة خاصة في مدينتك حسب رغبتك"
}

fun RequestCategory.icon(): ImageVector = when (this) {
    RequestCategory.Shopping -> Icons.Default.ShoppingBag
    RequestCategory.Pickup -> Icons.Default.LocationOn
    RequestCategory.Bills -> Icons.Default.Payment
    RequestCategory.Custom -> Icons.Default.Category
}