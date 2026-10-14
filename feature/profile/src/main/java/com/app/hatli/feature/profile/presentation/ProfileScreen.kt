package com.app.hatli.feature.profile.presentation

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.common.util.LocaleHelper
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.AppThemeMode
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.model.VerificationStatus
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.profile.R
import com.app.hatli.feature.profile.components.ProfileErrorState
import com.app.hatli.feature.profile.components.ProfileHeader
import com.app.hatli.feature.profile.components.ProfileInfoRow
import com.app.hatli.feature.profile.components.ProfileSectionCard
import com.app.hatli.feature.profile.components.RunnerDetailsSection
import com.app.hatli.feature.profile.components.UserRoleBadgeCard
import com.app.hatli.feature.profile.presentation.dialog.LanguageSelectionDialog
import com.app.hatli.feature.profile.presentation.dialog.LogoutConfirmationDialog
import com.app.hatli.feature.profile.presentation.dialog.PaymentInfoDialog
import com.app.hatli.feature.profile.presentation.dialog.SupportDialog
import com.app.hatli.feature.profile.presentation.dialog.ThemeSelectionDialog

@Composable
fun ProfileRoute(
    onLoggedOut: () -> Unit,
    onBack: () -> Unit,
    showBack: Boolean = true,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            ProfileUiEffect.LoggedOut -> onLoggedOut()
            is ProfileUiEffect.AppLanguageChanged -> {
                LocaleHelper.applyLocale(context, effect.languageCode)
                (context as? Activity)?.recreate()
            }
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
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            state.isLoading && state.profile == null -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
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
                val isRunner = profile.role == UserRole.Runner
                val isVerifiedRunner = isRunner && profile.verificationStatus == VerificationStatus.Approved

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(if (showBack) Modifier.systemBarsPadding() else Modifier.statusBarsPadding())
                        .verticalScroll(rememberScrollState())
                ) {
                    ProfileHeader(
                        fullName = profile.fullName,
                        selfieUrl = profile.selfieUrl,
                        isVerified = isVerifiedRunner,
                        showBack = showBack,
                        onBack = onBack
                    )

                    Column(
                        modifier = Modifier.padding(HatliSpacing.lg),
                        verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg)
                    ) {
                        UserRoleBadgeCard(role = profile.role)

                        if (isRunner) {
                            RunnerDetailsSection(
                                transportType = profile.transportType,
                                vehicleInfo = profile.vehicleInfo,
                                nationalId = profile.nationalId,
                                verificationStatus = profile.verificationStatus,
                                ordersCount = state.runnerOrdersCount,
                                rating = state.runnerRating,
                                acceptanceRate = state.runnerAcceptanceRate,
                            )
                        }

                        // Personal Information
                        ProfileSectionCard(title = stringResource(R.string.profile_section_personal_info)) {
                            ProfileInfoRow(
                                icon = Icons.Default.Phone,
                                label = stringResource(R.string.profile_label_phone),
                                value = profile.phone
                            )
                            if (profile.email.isNotBlank()) {
                                ProfileInfoRow(
                                    icon = Icons.Default.Email,
                                    label = stringResource(R.string.profile_label_email),
                                    value = profile.email
                                )
                            }
                            ProfileInfoRow(
                                icon = Icons.Default.Person,
                                label = stringResource(R.string.profile_label_gender),
                                value = stringResource(
                                    if (profile.gender == Gender.Male) {
                                        R.string.profile_gender_male
                                    } else {
                                        R.string.profile_gender_female
                                    }
                                )
                            )
                        }

                        // Delivery Address Section (for Requesters or when available)
                        val address = profile.address
                        if (address != null && !address.isEmpty) {
                            ProfileSectionCard(title = stringResource(R.string.profile_section_delivery_address)) {
                                ProfileInfoRow(
                                    icon = Icons.Default.LocationOn,
                                    label = stringResource(R.string.profile_label_address_location),
                                    value = address.formattedOneLine()
                                )
                                address.notes?.takeIf { it.isNotBlank() }?.let { notes ->
                                    ProfileInfoRow(
                                        icon = Icons.Default.Home,
                                        label = stringResource(R.string.profile_label_address_notes),
                                        value = notes
                                    )
                                }
                            }
                        } else if (!isRunner) {
                            ProfileSectionCard(title = stringResource(R.string.profile_section_delivery_address)) {
                                ProfileInfoRow(
                                    icon = Icons.Outlined.LocationOn,
                                    label = stringResource(R.string.profile_address_empty_title),
                                    value = stringResource(R.string.profile_address_empty_description),
                                    valueColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // App Preferences Section
                        ProfileSectionCard(title = stringResource(R.string.profile_section_preferences)) {
                            // 1. Language Row
                            ProfileInfoRow(
                                icon = Icons.Default.Language,
                                label = stringResource(R.string.profile_label_language),
                                value = stringResource(
                                    if (state.appLanguage == "en") R.string.profile_value_language_en else R.string.profile_value_language_ar
                                ),
                                showChevron = true,
                                onClick = { showLanguageDialog = true }
                            )

                            // 2. Appearance & Theme Row
                            val themeIcon = when (state.themeMode) {
                                AppThemeMode.Dark -> Icons.Default.DarkMode
                                AppThemeMode.Light -> Icons.Default.LightMode
                                AppThemeMode.FollowSystem -> Icons.Default.BrightnessAuto
                            }
                            val themeLabel = when (state.themeMode) {
                                AppThemeMode.Dark -> stringResource(R.string.profile_theme_dark)
                                AppThemeMode.Light -> stringResource(R.string.profile_theme_light)
                                AppThemeMode.FollowSystem -> stringResource(R.string.profile_theme_system)
                            }
                            ProfileInfoRow(
                                icon = themeIcon,
                                label = stringResource(R.string.profile_label_theme),
                                value = themeLabel,
                                showChevron = true,
                                onClick = { showThemeDialog = true }
                            )

                            // 3. Payment Methods Row
                            ProfileInfoRow(
                                icon = Icons.Default.Payment,
                                label = stringResource(R.string.profile_label_payments),
                                value = stringResource(R.string.profile_value_payments),
                                showChevron = true,
                                onClick = { showPaymentDialog = true }
                            )

                            // 4. Support Row
                            ProfileInfoRow(
                                icon = Icons.AutoMirrored.Filled.HelpOutline,
                                label = stringResource(R.string.profile_label_support),
                                value = stringResource(R.string.profile_value_support),
                                showChevron = true,
                                onClick = { showSupportDialog = true }
                            )
                        }

                        // Red Outlined Logout Button
                        OutlinedButton(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Text(
                                text = stringResource(R.string.profile_logout_button),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        // Balanced spacing at the bottom ensuring content clears the floating bottom bar without excessive empty space
                        Spacer(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .height(if (showBack) 16.dp else 80.dp)
                        )
                    }
                }
            }
        }
    }

    // Language Selection Dialog
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = state.appLanguage,
            onLanguageSelected = { onEvent(ProfileUiEvent.SetAppLanguage(it)) },
            onDismiss = { showLanguageDialog = false },
        )
    }

    // Theme Mode Selection Dialog
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentThemeMode = state.themeMode,
            onThemeModeSelected = { onEvent(ProfileUiEvent.SetThemeMode(it)) },
            onDismiss = { showThemeDialog = false },
        )
    }

    // Payment Methods Info Dialog
    if (showPaymentDialog) {
        PaymentInfoDialog(
            onDismiss = { showPaymentDialog = false },
        )
    }

    // Customer Support Dialog
    if (showSupportDialog) {
        SupportDialog(
            onDismiss = { showSupportDialog = false },
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = { onEvent(ProfileUiEvent.Logout) },
            onDismiss = { showLogoutDialog = false },
        )
    }
}

