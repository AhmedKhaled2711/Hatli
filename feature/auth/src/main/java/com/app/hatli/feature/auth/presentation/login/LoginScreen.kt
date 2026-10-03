package com.app.hatli.feature.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.component.auth.HatliAuthScreen
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.core.ui.util.findActivity
import com.app.hatli.feature.auth.R

@Composable
fun LoginRoute(
    onBack: () -> Unit,
    onOtp: (verificationId: String, phone: String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context.findActivity()

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
    val extended = HatliTheme.extendedColors

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
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(HatliSpacing.xl))

            Text(
                text = stringResource(R.string.auth_phone_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.sm))

            Text(
                text = stringResource(R.string.auth_phone_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.xl))

            // Phone Field with Integrated Egyptian Flag + "+20" Country Pill Badge
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                HatliTextField(
                    value = state.phone,
                    onValueChange = { onEvent(LoginUiEvent.PhoneChanged(it)) },
                    label = stringResource(R.string.auth_phone_number),
                    placeholder = "01007394856",
                    isError = state.errorMessage != null,
                    supportingText = state.errorMessage,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = HatliSpacing.xs)
                                .clip(RoundedCornerShape(HatliRadius.chip))
                                .background(extended.successContainer)
                                .padding(horizontal = HatliSpacing.xs, vertical = HatliSpacing.xxs),
                        ) {
                            Text(
                                text = "🇪🇬 +20",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    textDirection = TextDirection.Ltr,
                                ),
                                color = extended.onHeaderBackground,
                            )
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.height(HatliSpacing.xl))

            HatliPrimaryButton(
                text = if (state.isLoading) {
                    stringResource(R.string.auth_sending)
                } else {
                    "إرسال كود التحقق"
                },
                onClick = { onEvent(LoginUiEvent.Submit) },
                enabled = !state.isLoading && state.phone.isNotBlank(),
            )
        }
    }
}
