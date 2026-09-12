package com.app.hatli.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.profile.components.ProfileErrorState
import com.app.hatli.feature.profile.components.ProfileHeader
import com.app.hatli.feature.profile.components.ProfileInfoRow
import com.app.hatli.feature.profile.components.ProfileSectionCard
import com.app.hatli.feature.profile.components.RunnerDetailsSection
import com.app.hatli.feature.profile.components.UserRoleBadgeCard

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
    val extendedColors = LocalHatliExtendedColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.isLoading && state.profile == null -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = HatliColors.BrandTeal
                )
            }

            state.profile == null -> {
                ProfileErrorState(
                    errorMessage = state.errorMessage,
                    onRetry = { onEvent(ProfileUiEvent.Refresh) }
                )
            }

            else -> {
                val profile = state.profile
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(if (showBack) Modifier.systemBarsPadding() else Modifier.statusBarsPadding())
                        .verticalScroll(rememberScrollState())
                ) {
                    ProfileHeader(
                        fullName = profile.fullName,
                        selfieUrl = profile.selfieUrl,
                        showBack = showBack,
                        onBack = onBack
                    )

                    Column(
                        modifier = Modifier.padding(HatliSpacing.lg),
                        verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg)
                    ) {
                        UserRoleBadgeCard(role = profile.role)

                        if (profile.role == UserRole.Runner) {
                            RunnerDetailsSection(
                                transportType = profile.transportType?.name ?: "N/A",
                                vehicleInfo = profile.vehicleInfo,
                                nationalId = profile.nationalId,
                                verificationStatus = profile.verificationStatus?.name ?: "Pending"
                            )
                        }

                        ProfileSectionCard(title = "Personal Information") {
                            ProfileInfoRow(
                                icon = Icons.Default.Phone,
                                label = "Phone Number",
                                value = profile.phone
                            )
                            ProfileInfoRow(
                                icon = Icons.Default.Email,
                                label = "Email Address",
                                value = profile.email
                            )
                            ProfileInfoRow(
                                icon = Icons.Default.Person,
                                label = "Gender",
                                value = profile.gender.name
                            )
                        }

                        profile.address?.let { address ->
                            ProfileSectionCard(title = "Primary Delivery Address") {
                                ProfileInfoRow(
                                    icon = Icons.Default.LocationOn,
                                    label = "Location",
                                    value = address.formattedOneLine()
                                )
                                address.notes?.takeIf { it.isNotBlank() }?.let { notes ->
                                    ProfileInfoRow(
                                        icon = Icons.Default.Home,
                                        label = "Address Notes",
                                        value = notes
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(HatliSpacing.md))

                        HatliPrimaryButton(
                            text = "Log out",
                            onClick = { onEvent(ProfileUiEvent.Logout) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
