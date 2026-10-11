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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.LocalHatliExtendedColors
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas
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
    var showLogoutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhiteCanvas)
    ) {
        when {
            state.isLoading && state.profile == null -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = BrandTeal
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
                                transportType = profile.transportType?.name ?: "غير محدد",
                                vehicleInfo = profile.vehicleInfo,
                                nationalId = profile.nationalId,
                                verificationStatus = profile.verificationStatus?.name ?: "Pending"
                            )
                        }

                        ProfileSectionCard(title = "البيانات الشخصية") {
                            ProfileInfoRow(
                                icon = Icons.Default.Phone,
                                label = "رقم الهاتف",
                                value = profile.phone
                            )
                            if (!profile.email.isNullOrBlank()) {
                                ProfileInfoRow(
                                    icon = Icons.Default.Email,
                                    label = "البريد الإلكتروني",
                                    value = profile.email
                                )
                            }
                            ProfileInfoRow(
                                icon = Icons.Default.Person,
                                label = "النوع",
                                value = if (profile.gender.name == "Male") "ذكر" else "أنثى"
                            )
                        }

                        profile.address?.let { address ->
                            ProfileSectionCard(title = "عنوان التوصيل الرئيسي") {
                                ProfileInfoRow(
                                    icon = Icons.Default.LocationOn,
                                    label = "الموقع",
                                    value = address.formattedOneLine()
                                )
                                address.notes?.takeIf { it.isNotBlank() }?.let { notes ->
                                    ProfileInfoRow(
                                        icon = Icons.Default.Home,
                                        label = "ملاحظات العنوان",
                                        value = notes
                                    )
                                }
                            }
                        }

                        ProfileSectionCard(title = "تفضيلات التطبيق") {
                            ProfileInfoRow(
                                icon = Icons.Default.Language,
                                label = "لغة التطبيق",
                                value = "العربية (مصر)"
                            )
                            ProfileInfoRow(
                                icon = Icons.Default.Payment,
                                label = "وسائل الدفع",
                                value = "نقدي عند الاستلام 💵"
                            )
                            ProfileInfoRow(
                                icon = Icons.Default.HelpOutline,
                                label = "الدعم الفني والشكاوى",
                                value = "تواصل مع خدمة العملاء"
                            )
                        }

                        Spacer(modifier = Modifier.height(HatliSpacing.xs))

                        // Red Outlined Logout Button
                        OutlinedButton(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.error)
                        ) {
                            Text(
                                text = "تسجيل الخروج",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Spacer(modifier = Modifier.height(if (showBack) 32.dp else 128.dp))
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "تسجيل الخروج",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = CharcoalDark
                )
            },
            text = {
                Text(
                    text = "هل أنت تأكد من رغبتك في تسجيل الخروج من تطبيق هاتلي؟",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onEvent(ProfileUiEvent.Logout)
                    }
                ) {
                    Text(
                        text = "نعم، خروج",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(text = "إلغاء", color = CharcoalDark)
                }
            }
        )
    }
}
