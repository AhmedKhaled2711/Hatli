package com.app.hatli.feature.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
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
    showBack: Boolean = true,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            ProfileUiEffect.LoggedOut -> onLoggedOut()
        }
    }
    ProfileScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        showBack = showBack,
    )
}

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
    onBack: () -> Unit,
    showBack: Boolean = true,
) {
    val typography = MaterialTheme.typography
    val onVariant = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Tab chrome owns nav-bar insets; standalone needs full system bars.
            .then(if (showBack) Modifier.systemBarsPadding() else Modifier.statusBarsPadding())
            .verticalScroll(rememberScrollState())
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text("Profile", style = typography.headlineLarge)

        when {
            state.isLoading && state.profile == null -> CircularProgressIndicator()
            state.profile == null -> {
                Text(
                    text = state.errorMessage ?: "No profile found",
                    style = typography.bodyLarge,
                    color = onVariant,
                )
                HatliSecondaryButton(text = "Retry", onClick = { onEvent(ProfileUiEvent.Refresh) })
            }
            else -> {
                val p = state.profile
                p.selfieUrl?.takeIf { it.isNotBlank() }?.let { url ->
                    AsyncImage(model = url, contentDescription = "Profile photo")
                }
                HatliCard {
                    Text(p.fullName, style = typography.titleLarge)
                    Text("Role: ${p.role.name}", style = typography.bodyMedium, color = onVariant)
                    Text("Phone: ${p.phone}", style = typography.bodyMedium, color = onVariant)
                    Text("Email: ${p.email}", style = typography.bodyMedium, color = onVariant)
                    Text("Gender: ${p.gender.name}", style = typography.bodyMedium, color = onVariant)
                    p.address?.let { address ->
                        Text(
                            text = "Address: ${address.formattedOneLine()}",
                            style = typography.bodyMedium,
                            color = onVariant,
                        )
                        address.notes?.takeIf { it.isNotBlank() }?.let { notes ->
                            Text("Notes: $notes", style = typography.bodyMedium, color = onVariant)
                        }
                    }
                    if (p.role == UserRole.Runner) {
                        Text(
                            text = "Transport: ${p.transportType?.name.orEmpty()}",
                            style = typography.bodyMedium,
                            color = onVariant,
                        )
                        Text(
                            text = "Vehicle: ${p.vehicleInfo.orEmpty()}",
                            style = typography.bodyMedium,
                            color = onVariant,
                        )
                        Text(
                            text = "National ID: ${p.nationalId.orEmpty()}",
                            style = typography.bodyMedium,
                            color = onVariant,
                        )
                        Text(
                            text = "Verification: ${p.verificationStatus?.name.orEmpty()}",
                            style = typography.bodyMedium,
                            color = onVariant,
                        )
                    }
                }
            }
        }

        if (showBack) {
            HatliSecondaryButton(text = "Back", onClick = onBack)
        }
        HatliPrimaryButton(
            text = "Log out",
            onClick = { onEvent(ProfileUiEvent.Logout) },
        )
    }
}
