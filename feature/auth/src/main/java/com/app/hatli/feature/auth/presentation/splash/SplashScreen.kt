package com.app.hatli.feature.auth.presentation.splash

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.auth.R

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
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
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
            Surface(
                modifier = Modifier
                    .size(152.dp)
                    .scale(pulseScale),
                shape = CircleShape,
                shadowElevation = 10.dp,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Image(
                    painter = painterResource(com.app.hatli.core.designsystem.R.drawable.ic_hatli_logo),
                    contentDescription = stringResource(R.string.splash_logo_content_description),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                )
            }

            Spacer(modifier = Modifier.height(HatliSpacing.lg))

            Text(
                text = stringResource(R.string.splash_brand),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.xs))

            Text(
                text = stringResource(R.string.splash_tagline),
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
