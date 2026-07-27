package com.app.hatli.feature.auth.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.app.hatli.core.designsystem.component.HatliTextButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun OnboardingRoute(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            OnboardingUiEffect.GoLogin -> onFinished()
        }
    }
    OnboardingScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun OnboardingScreen(
    state: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
) {
    val page = state.pages[state.pageIndex]
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.padding(bottom = HatliSpacing.xl)) {
            HatliTextButton(text = "Skip", onClick = { onEvent(OnboardingUiEvent.Skip) })
        }
        Text(page.title, style = MaterialTheme.typography.headlineLarge)
        Text(
            page.body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(modifier = Modifier.weight(1f)) { }
        HatliPrimaryButton(
            text = if (state.isLast) "Get Started" else "Next",
            onClick = {
                if (state.isLast) onEvent(OnboardingUiEvent.GetStarted)
                else onEvent(OnboardingUiEvent.Next)
            },
        )
    }
}
