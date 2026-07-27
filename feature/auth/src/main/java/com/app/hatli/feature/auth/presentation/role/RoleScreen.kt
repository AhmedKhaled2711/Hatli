package com.app.hatli.feature.auth.presentation.role

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
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun RoleRoute(
    onCreateProfile: () -> Unit,
    viewModel: RoleViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            RoleUiEffect.GoCreateProfile -> onCreateProfile()
        }
    }
    RoleScreen(state = state, onEvent = viewModel::onEvent)
}

@Composable
fun RoleScreen(
    state: RoleUiState,
    onEvent: (RoleUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text("Choose your role", style = MaterialTheme.typography.headlineLarge)
        Text(
            "You can only set this once during profile creation.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HatliSecondaryButton(
            text = if (state.selected == UserRole.Requester) "✓ Requester" else "Requester",
            onClick = { onEvent(RoleUiEvent.Select(UserRole.Requester)) },
        )
        HatliSecondaryButton(
            text = if (state.selected == UserRole.Runner) "✓ Runner" else "Runner",
            onClick = { onEvent(RoleUiEvent.Select(UserRole.Runner)) },
        )
        HatliPrimaryButton(
            text = "Continue",
            onClick = { onEvent(RoleUiEvent.Continue) },
            enabled = state.selected != null,
        )
    }
}
