package com.app.hatli.feature.auth.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun SplashRoute(
    onOnboarding: () -> Unit,
    onLogin: () -> Unit,
    onCreateProfile: () -> Unit,
    onHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            SplashUiEffect.GoOnboarding -> onOnboarding()
            SplashUiEffect.GoLogin -> onLogin()
            SplashUiEffect.GoCreateProfile -> onCreateProfile()
            SplashUiEffect.GoHome -> onHome()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Hatli",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
