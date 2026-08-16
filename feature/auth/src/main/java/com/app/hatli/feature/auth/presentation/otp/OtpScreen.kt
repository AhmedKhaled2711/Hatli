package com.app.hatli.feature.auth.presentation.otp

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliTextButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.component.auth.HatliAuthScreen
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.auth.R

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
        onEvent = viewModel::onEvent
    )
}

@Composable
fun OtpScreen(
    state: OtpUiState,
    onBack: () -> Unit,
    onEvent: (OtpUiEvent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    HatliAuthScreen(
        title = stringResource(R.string.auth_mobile_verification),
        onBack = onBack,
    ) {
        Spacer(
            modifier = Modifier.height(HatliSpacing.xl),
        )

        Text(
            text = stringResource(R.string.auth_otp_title),
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(
            modifier = Modifier.height(HatliSpacing.sm),
        )

        Text(
            text = stringResource(
                R.string.auth_otp_description,
                state.phone,
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(
            modifier = Modifier.height(HatliSpacing.lg),
        )

        HatliTextField(
            value = state.code,
            onValueChange = {
                onEvent(OtpUiEvent.CodeChanged(it))
            },
            label = stringResource(R.string.auth_otp_code),
            modifier = Modifier.focusRequester(focusRequester),
            isError = state.errorMessage != null,
            supportingText = state.errorMessage,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
            ),
        )

        Spacer(
            modifier = Modifier.height(HatliSpacing.lg),
        )

        Text(
            text = if (state.canResend) {
                stringResource(R.string.auth_resend_code)
            } else {
                stringResource(
                    R.string.auth_resend_code_in,
                    state.secondsLeft,
                )
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(
            modifier = Modifier.height(HatliSpacing.md),
        )

        HatliPrimaryButton(
            text = if (state.isLoading) {
                stringResource(R.string.auth_verifying)
            } else {
                stringResource(R.string.auth_verify)
            },
            onClick = {
                onEvent(OtpUiEvent.Verify)
            },
            enabled = !state.isLoading && state.code.length == 6,
        )

        Spacer(
            modifier = Modifier.height(HatliSpacing.sm),
        )

        HatliTextButton(
            text = stringResource(R.string.auth_resend_code),
            onClick = {
                onEvent(OtpUiEvent.Resend)
            },
            enabled = state.canResend && !state.isLoading,
        )
    }
}
