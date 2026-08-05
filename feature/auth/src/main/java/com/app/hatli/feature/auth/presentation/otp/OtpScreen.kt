package com.app.hatli.feature.auth.presentation.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliTextButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun OtpRoute(
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
    OtpScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun OtpScreen(
    state: OtpUiState,
    onEvent: (OtpUiEvent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text("Verify OTP", style = typography.headlineLarge)
        Text(
            text = "Code sent to ${state.phone}",
            style = typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HatliTextField(
            value = state.code,
            onValueChange = { onEvent(OtpUiEvent.CodeChanged(it)) },
            label = "6-digit code",
            modifier = Modifier.focusRequester(focusRequester),
            isError = state.errorMessage != null,
            supportingText = state.errorMessage,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        )
        Text(
            text = if (state.canResend) "You can resend now"
            else "Resend in ${state.secondsLeft}s",
            style = typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HatliPrimaryButton(
            text = if (state.isLoading) "Verifying…" else "Verify",
            onClick = { onEvent(OtpUiEvent.Verify) },
            enabled = !state.isLoading && state.code.length == 6,
        )
        HatliTextButton(
            text = "Resend code",
            onClick = { onEvent(OtpUiEvent.Resend) },
            enabled = state.canResend && !state.isLoading,
        )
    }
}
