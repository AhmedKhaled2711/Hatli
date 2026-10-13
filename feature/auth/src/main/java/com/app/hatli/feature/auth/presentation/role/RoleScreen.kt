package com.app.hatli.feature.auth.presentation.role

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.auth.R

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
    val extended = HatliTheme.extendedColors

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = extended.offWhiteCanvas,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = HatliSpacing.lg, vertical = HatliSpacing.md),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
            ) {
                Spacer(modifier = Modifier.height(HatliSpacing.sm))

                // Header Section
                Text(
                    text = stringResource(R.string.role_selection_title),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = CharcoalDark,
                        fontSize = 24.sp,
                    ),
                )
                Text(
                    text = stringResource(R.string.role_selection_subtitle),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = CharcoalMuted,
                        lineHeight = 22.sp,
                    ),
                )

                Spacer(modifier = Modifier.height(HatliSpacing.xs))

                RoleSelectionCard(
                    title = stringResource(R.string.role_requester_title),
                    tagline = stringResource(R.string.role_requester_tagline),
                    icon = Icons.Default.ShoppingBag,
                    accentColor = extended.coralPrimary,
                    containerColor = extended.coralContainer,
                    selected = state.selected == UserRole.Requester,
                    benefits = listOf(
                        stringResource(R.string.role_requester_feature_1),
                        stringResource(R.string.role_requester_feature_2),
                        stringResource(R.string.role_requester_feature_3),
                    ),
                    onClick = { onEvent(RoleUiEvent.Select(UserRole.Requester)) },
                )


                RoleSelectionCard(
                    title = stringResource(R.string.role_runner_title),
                    tagline = stringResource(R.string.role_runner_tagline),
                    icon = Icons.AutoMirrored.Filled.DirectionsBike,
                    accentColor = extended.mintAccent,
                    containerColor = extended.successContainer,
                    selected = state.selected == UserRole.Runner,
                    benefits = listOf(
                        stringResource(R.string.role_runner_feature_1),
                        stringResource(R.string.role_runner_feature_2),
                        stringResource(R.string.role_runner_feature_3),
                    ),
                    onClick = { onEvent(RoleUiEvent.Select(UserRole.Runner)) },
                )
            }

            Spacer(modifier = Modifier.height(HatliSpacing.md))

            HatliPrimaryButton(
                text = stringResource(R.string.role_continue_button),
                onClick = { onEvent(RoleUiEvent.Continue) },
                enabled = state.selected != null,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun RoleSelectionCard(
    title: String,
    tagline: String,
    icon: ImageVector,
    accentColor: Color,
    containerColor: Color,
    selected: Boolean,
    benefits: List<String>,
    onClick: () -> Unit,
) {
    val extended = HatliTheme.extendedColors
    val borderColor by animateColorAsState(
        targetValue = if (selected) accentColor else extended.border,
        animationSpec = tween(durationMillis = 200),
        label = "roleBorderColor",
    )
    val borderWidth by animateDpAsState(
        targetValue = if (selected) 2.dp else 1.dp,
        animationSpec = tween(durationMillis = 200),
        label = "roleBorderWidth",
    )
    val cardBackground by animateColorAsState(
        targetValue = if (selected) containerColor else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 200),
        label = "roleCardBg",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cardBackground)
            .border(borderWidth, borderColor, RoundedCornerShape(20.dp))
            .shadow(if (selected) 4.dp else 1.dp, RoundedCornerShape(20.dp), spotColor = accentColor.copy(alpha = 0.15f))
            .clickable(onClick = onClick)
            .padding(HatliSpacing.lg),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CharcoalDark,
                            ),
                        )
                        Text(
                            text = tagline,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CharcoalMuted,
                                fontSize = 13.sp,
                            ),
                        )
                    }
                }

                // Checkbox / Radio Indicator
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (selected) accentColor else Color.Transparent)
                        .border(
                            width = if (selected) 0.dp else 2.dp,
                            color = if (selected) Color.Transparent else CharcoalMuted.copy(alpha = 0.4f),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(HatliSpacing.xxs))

            // Benefit Bullets
            benefits.forEach { benefit ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(accentColor),
                    )
                    Text(
                        text = benefit,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CharcoalDark.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                        ),
                    )
                }
            }
        }
    }
}

