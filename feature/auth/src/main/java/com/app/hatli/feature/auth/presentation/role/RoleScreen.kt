package com.app.hatli.feature.auth.presentation.role

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
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
            .systemBarsPadding()
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text(
            text = "How will you use Hatli?",
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = "Choose once to tailor your setup. You can ask Support later if you need a role change.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(HatliSpacing.sm))

        HatliCard(
            selected = state.selected == UserRole.Requester,
            onClick = { onEvent(RoleUiEvent.Select(UserRole.Requester)) },
        ) {
            Text("I need deliveries", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Requester — post pickups and track runners.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = HatliSpacing.xs),
            )
        }

        HatliCard(
            selected = state.selected == UserRole.Runner,
            onClick = { onEvent(RoleUiEvent.Select(UserRole.Runner)) },
        ) {
            Text("I deliver", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Runner — accept nearby jobs and earn on your schedule.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = HatliSpacing.xs),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        HatliPrimaryButton(
            text = "Continue",
            onClick = { onEvent(RoleUiEvent.Continue) },
            enabled = state.selected != null,
        )
    }
}
