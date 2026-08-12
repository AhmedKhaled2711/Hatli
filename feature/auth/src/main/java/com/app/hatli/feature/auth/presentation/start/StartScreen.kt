package com.app.hatli.feature.auth.presentation.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSocialButton
import com.app.hatli.core.designsystem.component.HatliTextButton
import com.app.hatli.core.designsystem.component.auth.HatliAuthDivider
import com.app.hatli.core.designsystem.component.auth.HatliAuthTopBar
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.designsystem.theme.HatliThemeMode
import com.app.hatli.feature.auth.R
import kotlinx.coroutines.launch

@Composable
fun StartRoute(
    onBack: () -> Unit,
    onPhoneLogin: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val authComingSoon = stringResource(R.string.auth_coming_soon)
    fun showComingSoon() {
        scope.launch {
            snackbarHostState.showSnackbar(
                message =  authComingSoon
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->

        StartScreen(
            modifier = Modifier.padding(paddingValues),
            onBack = onBack,
            onFacebookClick = ::showComingSoon,
            onGoogleClick = ::showComingSoon,
            onPhoneClick = onPhoneLogin,
            onSignUpClick = ::showComingSoon,
        )
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onFacebookClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onPhoneClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = HatliSpacing.lg),
    ) {
        Spacer(
            modifier = Modifier.height(HatliSpacing.sm),
        )

        HatliAuthTopBar(
            title = stringResource(R.string.onboarding_brand),
            onBack = onBack,
        )

        Spacer(
            modifier = Modifier.weight(1f),
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.auth_lets_get_started),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
            )

            Spacer(
                modifier = Modifier.height(HatliSpacing.lg),
            )

            HatliSocialButton(
                text = stringResource(R.string.auth_continue_with_facebook),
                onClick = onFacebookClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_facebook),
                    contentDescription = stringResource(
                        R.string.auth_continue_with_facebook,
                    ),
                    tint = Color.Unspecified,
                )
            }

            Spacer(
                modifier = Modifier.height(HatliSpacing.sm),
            )

            HatliSocialButton(
                text = stringResource(R.string.auth_continue_with_google),
                onClick = onGoogleClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = stringResource(
                        R.string.auth_continue_with_google,
                    ),
                    tint = Color.Unspecified,
                )
            }

            Spacer(
                modifier = Modifier.height(HatliSpacing.lg),
            )

            HatliAuthDivider()

            Spacer(
                modifier = Modifier.height(HatliSpacing.lg),
            )

            HatliPrimaryButton(
                text = stringResource(R.string.auth_sign_in_with_phone),
                onClick = onPhoneClick,
            )

            Spacer(
                modifier = Modifier.height(HatliSpacing.lg),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.auth_dont_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                HatliTextButton(
                    text = stringResource(R.string.auth_sign_up),
                    onClick = onSignUpClick,
                )
            }
        }

        Spacer(
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(
    name = "Start Screen - Light",
    showBackground = true,
)
@Composable
private fun StartScreenLightPreview() {
    HatliTheme {
        StartScreen(
            onBack = {},
            onFacebookClick = {},
            onGoogleClick = {},
            onPhoneClick = {},
            onSignUpClick = {},
        )
    }
}

@Preview(
    name = "Start Screen - Dark",
    showBackground = true,
)
@Composable
private fun StartScreenDarkPreview() {
    HatliTheme(
        themeMode = HatliThemeMode.Dark,
    ) {
        StartScreen(
            onBack = {},
            onFacebookClick = {},
            onGoogleClick = {},
            onPhoneClick = {},
            onSignUpClick = {},
        )
    }
}

@Preview(
    name = "Start Screen - Arabic Light",
    showBackground = true,
    locale = "ar",
)
@Composable
private fun StartScreenArabicLightPreview() {
    HatliTheme {
        StartScreen(
            onBack = {},
            onFacebookClick = {},
            onGoogleClick = {},
            onPhoneClick = {},
            onSignUpClick = {},
        )
    }
}

@Preview(
    name = "Start Screen - Arabic Dark",
    showBackground = true,
    locale = "ar",
)
@Composable
private fun StartScreenArabicDarkPreview() {
    HatliTheme(
        themeMode = HatliThemeMode.Dark,
    ) {
        StartScreen(
            onBack = {},
            onFacebookClick = {},
            onGoogleClick = {},
            onPhoneClick = {},
            onSignUpClick = {},
        )
    }
}