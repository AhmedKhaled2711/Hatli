package com.app.hatli.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun AuthRoute(
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            AuthUiEffect.NavigateBack -> onBack()
            AuthUiEffect.LoginSuccess -> onLoginSuccess()
        }
    }

    AuthScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun AuthScreen(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text("Sign in", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Auth feature shell — wire real auth in a later phase",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HatliTextField(
            value = state.email,
            onValueChange = { onEvent(AuthUiEvent.EmailChanged(it)) },
            label = "Email",
        )
        HatliTextField(
            value = state.password,
            onValueChange = { onEvent(AuthUiEvent.PasswordChanged(it)) },
            label = "Password",
        )
        state.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        HatliPrimaryButton(
            text = if (state.isLoading) "Signing in…" else "Sign in",
            onClick = { onEvent(AuthUiEvent.Submit) },
            enabled = !state.isLoading,
        )
        HatliSecondaryButton(
            text = "Back",
            onClick = { onEvent(AuthUiEvent.Back) },
        )
    }
}
