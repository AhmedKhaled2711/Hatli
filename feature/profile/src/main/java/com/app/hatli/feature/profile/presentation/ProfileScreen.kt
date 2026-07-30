package com.app.hatli.feature.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.CollectUiEffect

@Composable
fun ProfileRoute(
    onLoggedOut: () -> Unit,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            ProfileUiEffect.LoggedOut -> onLoggedOut()
        }
    }
    ProfileScreen(state = state, onEvent = viewModel::onEvent, onBack = onBack)
}

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineLarge)

        when {
            state.isLoading && state.profile == null -> CircularProgressIndicator()
            state.profile == null -> {
                Text(state.errorMessage ?: "No profile found")
                HatliSecondaryButton(text = "Retry", onClick = { onEvent(ProfileUiEvent.Refresh) })
            }
            else -> {
                val p = state.profile
                // Selfie display requires Storage uploads (Blaze). Safe no-op while URLs are null.
                p.selfieUrl?.takeIf { it.isNotBlank() }?.let { url ->
                    AsyncImage(model = url, contentDescription = "Profile photo")
                }
                HatliCard {
                    Text(p.fullName, style = MaterialTheme.typography.titleLarge)
                    Text("Role: ${p.role.name}")
                    Text("Phone: ${p.phone}")
                    Text("Email: ${p.email}")
                    Text("Gender: ${p.gender.name}")
                    if (p.role == UserRole.Runner) {
                        Text("Vehicle: ${p.vehicleInfo.orEmpty()}")
                        Text("National ID: ${p.nationalId.orEmpty()}")
                        Text("Verification: ${p.verificationStatus?.name.orEmpty()}")
                    }
                }
            }
        }

        HatliSecondaryButton(text = "Back", onClick = onBack)
        HatliPrimaryButton(
            text = "Log out",
            onClick = { onEvent(ProfileUiEvent.Logout) },
        )
    }
}
