package com.app.hatli.feature.auth.presentation.approval

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsBike
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.ManageSearch
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.TwoWheeler
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.VerificationStatus
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.auth.R

@Composable
fun RunnerApprovalRoute(
    onApproved: () -> Unit,
    onLogout: () -> Unit,
    viewModel: RunnerApprovalViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            RunnerApprovalUiEffect.GoHome -> onApproved()
            RunnerApprovalUiEffect.GoAuth -> onLogout()
            is RunnerApprovalUiEffect.ShowMessage -> {
                snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = HatliTheme.extendedColors.offWhiteCanvas,
    ) { innerPadding ->
        RunnerApprovalScreen(
            state = state,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun RunnerApprovalScreen(
    state: RunnerApprovalUiState,
    onEvent: (RunnerApprovalUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val extended = HatliTheme.extendedColors

    val (badgeIcon, badgeColor, containerBg, titleRes, badgeRes, descRes) = when (state.status) {
        VerificationStatus.Approved -> ApprovalVisualConfig(
            icon = Icons.Rounded.CheckCircle,
            accentColor = extended.success,
            containerColor = extended.successContainer,
            titleRes = R.string.runner_approval_status_approved_title,
            badgeRes = R.string.runner_approval_status_approved_badge,
            descRes = R.string.runner_approval_status_approved_desc,
        )

        VerificationStatus.UnderReview -> ApprovalVisualConfig(
            icon = Icons.Rounded.ManageSearch,
            accentColor = extended.coralPrimary,
            containerColor = extended.coralContainer,
            titleRes = R.string.runner_approval_status_under_review_title,
            badgeRes = R.string.runner_approval_status_under_review_badge,
            descRes = R.string.runner_approval_status_under_review_desc,
        )

        VerificationStatus.Pending -> ApprovalVisualConfig(
            icon = Icons.Rounded.HourglassTop,
            accentColor = extended.amberPending,
            containerColor = Color(0xFFFFF8E1),
            titleRes = R.string.runner_approval_status_pending_title,
            badgeRes = R.string.runner_approval_status_pending_badge,
            descRes = R.string.runner_approval_status_pending_desc,
        )

        VerificationStatus.Rejected -> ApprovalVisualConfig(
            icon = Icons.Rounded.Cancel,
            accentColor = MaterialTheme.colorScheme.error,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            titleRes = R.string.runner_approval_status_rejected_title,
            badgeRes = R.string.runner_approval_status_rejected_badge,
            descRes = R.string.runner_approval_status_rejected_desc,
        )

        null -> ApprovalVisualConfig(
            icon = Icons.Rounded.HelpOutline,
            accentColor = CharcoalMuted,
            containerColor = extended.surfaceVariant,
            titleRes = R.string.runner_approval_status_unknown_title,
            badgeRes = R.string.runner_approval_status_unknown_badge,
            descRes = R.string.runner_approval_status_unknown_desc,
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = HatliSpacing.lg),
    ) {
        // Top App Bar Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = HatliSpacing.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.runner_approval_screen_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = CharcoalDark,
                ),
            )

            IconButton(
                onClick = { onEvent(RunnerApprovalUiEvent.Logout) },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = stringResource(R.string.runner_approval_logout_button),
                    tint = CharcoalMuted,
                )
            }
        }

        // Scrollable Body Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
        ) {
            Spacer(modifier = Modifier.height(HatliSpacing.xs))

            // Hero Status Card
            val cardShape = RoundedCornerShape(24.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        shape = cardShape,
                        spotColor = badgeColor.copy(alpha = 0.18f),
                    )
                    .clip(cardShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, extended.border.copy(alpha = 0.65f), cardShape)
                    .padding(HatliSpacing.lg),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
                ) {
                    // Glowing Icon Container
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(containerBg)
                            .border(1.5.dp, badgeColor.copy(alpha = 0.35f), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = badgeIcon,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(40.dp),
                        )
                    }

                    // Status Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(HatliRadius.pill))
                            .background(containerBg)
                            .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(HatliRadius.pill))
                            .padding(horizontal = HatliSpacing.sm, vertical = HatliSpacing.xxs),
                    ) {
                        Text(
                            text = stringResource(badgeRes),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = badgeColor,
                            ),
                        )
                    }

                    // Title
                    Text(
                        text = stringResource(titleRes),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark,
                            fontSize = 20.sp,
                        ),
                        textAlign = TextAlign.Center,
                    )

                    // Description
                    Text(
                        text = stringResource(descRes),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CharcoalMuted,
                            lineHeight = 22.sp,
                        ),
                        textAlign = TextAlign.Center,
                    )

                    // Last Checked Indicator
                    if (state.lastCheckedEpochMs != null) {
                        Text(
                            text = stringResource(R.string.runner_approval_last_checked_just_now),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CharcoalMuted.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                            ),
                        )
                    }
                }
            }

            // Captain Registration Summary Card
            if (state.runnerName.isNotBlank() || state.phone.isNotBlank()) {
                HatliCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(HatliSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.runner_approval_captain_badge),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = extended.coralPrimary,
                                ),
                            )
                        }

                        if (state.runnerName.isNotBlank()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = CharcoalMuted,
                                    modifier = Modifier.size(18.dp),
                                )
                                Text(
                                    text = state.runnerName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = CharcoalDark,
                                    ),
                                )
                            }
                        }

                        if (state.phone.isNotBlank()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Phone,
                                    contentDescription = null,
                                    tint = CharcoalMuted,
                                    modifier = Modifier.size(18.dp),
                                )
                                Text(
                                    text = state.phone,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = CharcoalDark,
                                    ),
                                )
                            }
                        }

                        val transportIcon = when (state.transportType) {
                            TransportType.Car -> Icons.Rounded.DirectionsCar
                            TransportType.Motorcycle -> Icons.Rounded.TwoWheeler
                            TransportType.Bicycle -> Icons.AutoMirrored.Rounded.DirectionsBike
                            TransportType.Walking -> Icons.AutoMirrored.Rounded.DirectionsRun
                            else -> Icons.Rounded.DirectionsCar
                        }

                        if (state.transportType != null) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = transportIcon,
                                    contentDescription = null,
                                    tint = extended.coralPrimary,
                                    modifier = Modifier.size(18.dp),
                                )
                                val vehicleLabel = state.vehicleInfo?.takeIf { it.isNotBlank() }
                                    ?: state.transportType.name
                                Text(
                                    text = vehicleLabel,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = CharcoalDark,
                                    ),
                                )
                            }
                        }
                    }
                }
            }

            // Verification Steps Guidance Card
            HatliCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(HatliSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                ) {
                    Text(
                        text = stringResource(R.string.runner_approval_steps_title),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark,
                        ),
                    )

                    ApprovalStepRow(
                        stepNumber = "1",
                        text = stringResource(R.string.runner_approval_step_1),
                        accentColor = extended.coralPrimary,
                    )
                    ApprovalStepRow(
                        stepNumber = "2",
                        text = stringResource(R.string.runner_approval_step_2),
                        accentColor = extended.coralPrimary,
                    )
                    ApprovalStepRow(
                        stepNumber = "3",
                        text = stringResource(R.string.runner_approval_step_3),
                        accentColor = extended.coralPrimary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(HatliSpacing.md))
        }

        // Bottom CTAs
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        ) {
            HatliPrimaryButton(
                text = if (state.isRefreshing) {
                    stringResource(R.string.runner_approval_refreshing)
                } else {
                    stringResource(R.string.runner_approval_refresh_button)
                },
                onClick = { onEvent(RunnerApprovalUiEvent.Refresh) },
                enabled = !state.isRefreshing && !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )

            HatliSecondaryButton(
                text = stringResource(R.string.runner_approval_logout_button),
                onClick = { onEvent(RunnerApprovalUiEvent.Logout) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ApprovalStepRow(
    stepNumber: String,
    text: String,
    accentColor: Color,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stepNumber,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                ),
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = CharcoalDark.copy(alpha = 0.85f),
                fontSize = 13.sp,
            ),
        )
    }
}

private data class ApprovalVisualConfig(
    val icon: ImageVector,
    val accentColor: Color,
    val containerColor: Color,
    val titleRes: Int,
    val badgeRes: Int,
    val descRes: Int,
)
