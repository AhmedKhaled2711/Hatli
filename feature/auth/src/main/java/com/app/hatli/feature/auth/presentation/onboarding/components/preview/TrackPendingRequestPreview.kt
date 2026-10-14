package com.app.hatli.feature.auth.presentation.onboarding.components.preview

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingStrings

/**
 * Screen 3 Mockup: Track Pending Request.
 * Faithfully mirrors RequestDetailScreen in Pending state (searching for a captain).
 */
@Composable
internal fun TrackPendingRequestPreview(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "PendingPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseAlpha",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 7.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Mini Header Top Bar matching RequestDetailScreen (HatliAuthTopBar)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(13.dp),
            )

            Text(
                text = stringResource(OnboardingStrings.mockTrackingDetailTitle),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                ),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        // Scrollable Request Detail Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // StatusHeroCard for RequestStatus.Pending
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.md),
                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.55f),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.35f)),
            ) {
                Column(
                    modifier = Modifier.padding(7.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(12.dp),
                            )
                        }

                        Text(
                            text = stringResource(OnboardingStrings.mockStatusHeroPendingTitle),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                            ),
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.graphicsLayer { alpha = pulseAlpha },
                        )
                    }

                    Text(
                        text = stringResource(OnboardingStrings.mockStatusHeroOrderTitle),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.5.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = stringResource(OnboardingStrings.mockStatusHeroPendingDesc),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // DetailStatusTrack Card matching real lifecycle (All steps Unreached for Pending)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.sm),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = stringResource(OnboardingStrings.mockTrackProgressTitle),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    // 6 Shopping lifecycle steps — Unreached (muted gray) because request is still Pending
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ) {
                        val steps = listOf(
                            stringResource(OnboardingStrings.mockTrackStepAccepted),
                            stringResource(OnboardingStrings.mockTrackStepInProgress),
                            stringResource(OnboardingStrings.mockTrackStepPurchased),
                            stringResource(OnboardingStrings.mockTrackStepOnTheWay),
                            stringResource(OnboardingStrings.mockTrackStepDelivered),
                            stringResource(OnboardingStrings.mockTrackStepCompleted),
                        )

                        steps.forEachIndexed { index, label ->
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(2.dp)
                                            .background(
                                                if (index > 0) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                                else Color.Transparent,
                                            ),
                                    )

                                    // Unreached Step Indicator Circle
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.outlineVariant),
                                    )

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(2.dp)
                                            .background(
                                                if (index < steps.lastIndex) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                                else Color.Transparent,
                                            ),
                                    )
                                }

                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 5.5.sp,
                                        lineHeight = 7.sp,
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                )
                            }
                        }
                    }
                }
            }

            // OrderInfoCard matching RequestDetailScreen
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.sm),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(3.5.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(OnboardingStrings.mockOrderInfoTitle),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.5.sp,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                text = stringResource(OnboardingStrings.mockOrderInfoFeeBadge),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 7.sp,
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.5.dp),
                            )
                        }
                    }

                    // Description row
                    DetailRowMini(
                        icon = Icons.AutoMirrored.Filled.ReceiptLong,
                        label = stringResource(OnboardingStrings.mockOrderInfoDescLabel),
                        value = stringResource(OnboardingStrings.mockOrderInfoDescVal),
                    )

                    // Pickup location row
                    DetailRowMini(
                        icon = Icons.Default.LocationOn,
                        label = stringResource(OnboardingStrings.mockOrderInfoPickupLabel),
                        value = stringResource(OnboardingStrings.mockOrderInfoPickupVal),
                        valueColor = MaterialTheme.colorScheme.primary,
                    )

                    // Dropoff location row
                    DetailRowMini(
                        icon = Icons.Default.Navigation,
                        label = stringResource(OnboardingStrings.mockOrderInfoDropoffLabel),
                        value = stringResource(OnboardingStrings.mockOrderInfoDropoffVal),
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        thickness = 0.5.dp,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(OnboardingStrings.mockOrderInfoFeeLabel),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.5.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Text(
                            text = stringResource(OnboardingStrings.mockOrderInfoFeeVal),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.5.sp,
                            ),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }

        // Cancel Order Button matching CancelWorkflowSection (Secondary Button)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(HatliRadius.sm),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ) {
            Box(
                modifier = Modifier.padding(vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(OnboardingStrings.mockBtnCancelOrder),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 8.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
