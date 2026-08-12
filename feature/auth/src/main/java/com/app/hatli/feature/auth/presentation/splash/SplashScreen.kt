package com.app.hatli.feature.auth.presentation.splash

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.hatli.core.designsystem.component.HatliLottieAnimation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.ui.base.CollectUiEffect

/** Branded `delivery_man` Lottie under auth assets. */
private const val SPLASH_LOTTIE_ASSET = "lottie/splash/delivery_man.json"

@Composable
fun SplashRoute(
    onOnboarding: () -> Unit,
    onLogin: () -> Unit,
    onCreateProfile: () -> Unit,
    onHome: () -> Unit,
    onStart: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            SplashUiEffect.GoOnboarding -> onOnboarding()
            SplashUiEffect.GoStart -> onStart()
            SplashUiEffect.GoLogin -> onLogin()
            SplashUiEffect.GoCreateProfile -> onCreateProfile()
            SplashUiEffect.GoHome -> onHome()
        }
    }

    SplashScreen()
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = HatliSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            HatliLottieAnimation(
                assetPath = SPLASH_LOTTIE_ASSET,
                modifier = Modifier.size(280.dp),
            )
            Spacer(modifier = Modifier.height(HatliSpacing.lg))
            Text(
                text = "Hatli",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(HatliSpacing.xs))
            Text(
                text = "Deliveries you can trust",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = HatliSpacing.xxl)
                .size(32.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 3.dp,
        )
    }
}

@Preview(name = "Splash Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Splash Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SplashScreenPreview() {
    HatliTheme {
        SplashScreen()
    }
}
