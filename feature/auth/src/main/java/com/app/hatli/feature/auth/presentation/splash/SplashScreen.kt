package com.app.hatli.feature.auth.presentation.splash

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.hatli.core.designsystem.component.HatliLottieAnimation
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.ui.base.CollectUiEffect

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
    val extended = HatliTheme.extendedColors
    val infiniteTransition = rememberInfiniteTransition(label = "SplashPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "PulseScaleAnimation",
    )

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
            Box(
                modifier = Modifier.size(280.dp),
                contentAlignment = Alignment.Center,
            ) {
                // Pulsing focal ring behind brand mark
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .scale(pulseScale)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                            shape = CircleShape,
                        ),
                )
                HatliLottieAnimation(
                    assetPath = SPLASH_LOTTIE_ASSET,
                    modifier = Modifier.size(240.dp),
                )
            }

            Spacer(modifier = Modifier.height(HatliSpacing.lg))

            Text(
                text = "Hatli",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.xs))

            Text(
                text = "كل اللي تحتاجه، من مكانك",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = HatliSpacing.xxl)
                .size(32.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = extended.coralContainer,
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
