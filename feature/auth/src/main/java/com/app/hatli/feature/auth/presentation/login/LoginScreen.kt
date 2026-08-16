package com.app.hatli.feature.auth.presentation.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.component.auth.HatliAuthScreen
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.auth.R

@Composable
fun LoginRoute(
    onBack: () -> Unit,
    onOtp: (verificationId: String, phone: String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is LoginUiEffect.GoOtp -> {
                onOtp(
                    effect.verificationId,
                    effect.phone,
                )
            }
        }
    }

    LoginScreen(
        state = state,
        onBack = onBack,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onBack: () -> Unit,
    onEvent: (LoginUiEvent) -> Unit,
) {
    HatliAuthScreen(
        title = stringResource(R.string.auth_mobile_verification),
        onBack = onBack,
    ) {
        Spacer(
            modifier = Modifier.height(HatliSpacing.xl),
        )

        Text(
            text = stringResource(R.string.auth_phone_title),
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(
            modifier = Modifier.height(HatliSpacing.sm),
        )

        Text(
            text = stringResource(R.string.auth_phone_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(
            modifier = Modifier.height(HatliSpacing.lg),
        )

        HatliTextField(
            value = state.phone,
            onValueChange = {
                onEvent(LoginUiEvent.PhoneChanged(it))
            },
            label = stringResource(R.string.auth_phone_number),
            isError = state.errorMessage != null,
            supportingText = state.errorMessage,
        )

        Spacer(
            modifier = Modifier.height(HatliSpacing.lg),
        )

        HatliPrimaryButton(
            text = if (state.isLoading) {
                stringResource(R.string.auth_sending)
            } else {
                stringResource(R.string.auth_continue)
            },
            onClick = {
                onEvent(LoginUiEvent.Submit)
            },
            enabled = !state.isLoading && state.phone.isNotBlank(),
        )
    }
}
