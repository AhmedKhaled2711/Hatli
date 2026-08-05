package com.app.hatli.feature.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun LoginRoute(
    onOtp: (verificationId: String, phone: String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is LoginUiEffect.GoOtp -> onOtp(effect.verificationId, effect.phone)
        }
    }
    LoginScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text("Sign in", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Enter your phone number with country code",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HatliTextField(
            value = state.phone,
            onValueChange = { onEvent(LoginUiEvent.PhoneChanged(it)) },
            label = "Phone (+20…)",
            isError = state.errorMessage != null,
            supportingText = state.errorMessage,
        )
        HatliPrimaryButton(
            text = if (state.isLoading) "Sending…" else "Send OTP",
            onClick = { onEvent(LoginUiEvent.Submit) },
            enabled = !state.isLoading && state.phone.isNotBlank(),
        )
    }
}
