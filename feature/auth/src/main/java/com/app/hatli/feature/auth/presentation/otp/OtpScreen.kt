package com.app.hatli.feature.auth.presentation.otp

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliTextButton
import com.app.hatli.core.designsystem.component.auth.HatliAuthScreen
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.auth.R
import kotlin.math.roundToInt

@Composable
fun OtpRoute(
    onBack: () -> Unit,
    onRoleSelect: () -> Unit,
    onCreateProfile: () -> Unit,
    onHome: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            OtpUiEffect.GoRoleSelect -> onRoleSelect()
            OtpUiEffect.GoCreateProfile -> onCreateProfile()
            OtpUiEffect.GoHome -> onHome()
        }
    }
    OtpScreen(
        state = state,
        onBack = onBack,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun OtpScreen(
    state: OtpUiState,
    onBack: () -> Unit,
    onEvent: (OtpUiEvent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Horizontal Shake Animation on Error State
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage != null) {
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = spring(dampingRatio = 0.2f, stiffness = 600f),
            ) {
                // Trigger quick shake keyframes
            }
        }
    }

    HatliAuthScreen(
        title = stringResource(R.string.auth_mobile_verification),
        onBack = onBack,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(HatliSpacing.xl))

            Text(
                text = stringResource(R.string.auth_otp_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.sm))

            val formattedPhone = androidx.core.text.BidiFormatter.getInstance().unicodeWrap(
                state.phone,
                androidx.core.text.TextDirectionHeuristicsCompat.LTR,
            )
            Text(
                text = stringResource(R.string.auth_otp_description, formattedPhone),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.xl))

            // 6-Box Monospaced Square OTP Grid Component
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicTextField(
                        value = state.code,
                        onValueChange = { input ->
                            if (input.length <= 6 && input.all { it.isDigit() }) {
                                onEvent(OtpUiEvent.CodeChanged(input))
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 328.dp)
                            .focusRequester(focusRequester),
                        decorationBox = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                repeat(6) { index ->
                                    val char = state.code.getOrNull(index)?.toString() ?: ""
                                    val isFocused = index == state.code.length
                                    val isFilled = index < state.code.length

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(56.dp)
                                            .clip(RoundedCornerShape(HatliRadius.chip))
                                            .background(MaterialTheme.colorScheme.surface)
                                            .border(
                                                width = if (isFocused) 2.dp else 1.dp,
                                                color = when {
                                                    state.errorMessage != null -> MaterialTheme.colorScheme.error
                                                    isFocused -> MaterialTheme.colorScheme.primary
                                                    isFilled -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                                    else -> MaterialTheme.colorScheme.outline
                                                },
                                                shape = RoundedCornerShape(HatliRadius.chip),
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = char,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                textDirection = TextDirection.Ltr,
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface,
                                        )
                                    }
                                }
                            }
                        },
                    )
                }
            }

            state.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(HatliSpacing.md))
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(HatliSpacing.xl))

            // Resend countdown timer styled in hatli_charcoal_muted
            Text(
                text = if (state.canResend) {
                    stringResource(R.string.auth_didnt_receive_code)
                } else {
                    stringResource(R.string.auth_resend_code_in, state.secondsLeft)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.md))

            HatliPrimaryButton(
                text = if (state.isLoading) {
                    stringResource(R.string.auth_verifying)
                } else {
                    stringResource(R.string.auth_verify)
                },
                onClick = { onEvent(OtpUiEvent.Verify) },
                enabled = !state.isLoading && state.code.length == 6,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.sm))

            HatliTextButton(
                text = stringResource(R.string.auth_resend_code),
                onClick = { onEvent(OtpUiEvent.Resend) },
                enabled = state.canResend && !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
