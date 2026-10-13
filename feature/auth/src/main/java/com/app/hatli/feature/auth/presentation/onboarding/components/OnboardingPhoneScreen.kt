package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingPageType
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingStrings

/**
 * Authentic, production-grade phone preview screens mirroring Hatli's actual client-side UI:
 * - Screen 1: Create Supermarket Request (CreateRequestScreen Step 2 - DetailsStep)
 * - Screen 2: Review Request & Captain Delivery Fee (CreateRequestScreen Step 3 - FeeReviewStep)
 * - Screen 3: Track Pending Request (RequestDetailScreen - Searching for a Captain)
 */
@Composable
fun OnboardingPhoneScreen(
    page: OnboardingPageType,
    modifier: Modifier = Modifier,
) {
    when (page) {
        OnboardingPageType.OrderNearby -> CreateSupermarketRequestPreviewScreen(modifier)
        OnboardingPageType.CreateRequest -> FeeReviewRequestPreviewScreen(modifier)
        OnboardingPageType.NearbyRunners -> TrackPendingRequestPreviewScreen(modifier)
    }
}

// =============================================================================
// SCREEN 1: Create Supermarket Request (Step 2 - DetailsStep)
// =============================================================================

@Composable
private fun CreateSupermarketRequestPreviewScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 7.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Mini Header Top Bar matching CreateRequestScreen
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(20.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(11.dp),
                        )
                    }
                }

                Text(
                    text = stringResource(OnboardingStrings.mockCreateTitle),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Surface(
                shape = RoundedCornerShape(HatliRadius.pill),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Text(
                    text = stringResource(OnboardingStrings.mockStep2Of3),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 7.5.sp,
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                )
            }
        }

        // Mini 3-Step Wizard Indicator matching CreateRequestScreen
        WizardStepTrackMini(currentStep = 2)

        // Scrollable Form Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // Headline
            Text(
                text = stringResource(OnboardingStrings.mockCategoryShopping),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp,
                ),
                color = MaterialTheme.colorScheme.onBackground,
            )

            // Active Category Header Pill with Edit Action
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.sm),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(HatliColors.BrandCoral.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = HatliColors.BrandCoral,
                                modifier = Modifier.size(12.dp),
                            )
                        }

                        Column {
                            Text(
                                text = stringResource(OnboardingStrings.mockCategoryShopping),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 8.5.sp,
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = stringResource(OnboardingStrings.mockCategoryShoppingSubtitle),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 6.5.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(9.dp),
                        )
                        Text(
                            text = stringResource(OnboardingStrings.mockChangeCategory),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 7.sp,
                            ),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            // Field 1: Store Name (Optional)
            FormTextFieldMini(
                label = stringResource(OnboardingStrings.mockShopNameLabel),
                value = stringResource(OnboardingStrings.mockShopNameValue),
            )

            // Field 2: Items Description (Multiline) with character count
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                FormTextFieldMini(
                    label = stringResource(OnboardingStrings.mockItemsLabel),
                    value = stringResource(OnboardingStrings.mockItemsValue),
                    isMultiline = true,
                )
                Text(
                    text = stringResource(OnboardingStrings.mockItemsCharCount),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 6.5.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }

            // Field 3: Additional Notes (Optional)
            FormTextFieldMini(
                label = stringResource(OnboardingStrings.mockNotesLabel),
                value = stringResource(OnboardingStrings.mockNotesValue),
            )

            // Field 4: Estimated Budget (Optional)
            FormTextFieldMini(
                label = stringResource(OnboardingStrings.mockBudgetLabel),
                value = stringResource(OnboardingStrings.mockBudgetValue),
            )
        }

        // Action Buttons Footer matching CreateRequestScreen
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.sm),
                color = HatliColors.BrandCoral,
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(OnboardingStrings.mockBtnContinue),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp,
                        ),
                        color = Color.White,
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.sm),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 3.5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(OnboardingStrings.mockBtnBack),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 7.5.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

// =============================================================================
// SCREEN 2: Review Request & Delivery Fee (Step 3 - FeeReviewStep)
// =============================================================================

@Composable
private fun FeeReviewRequestPreviewScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 7.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Mini Header Top Bar matching CreateRequestScreen
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(20.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(11.dp),
                        )
                    }
                }

                Text(
                    text = stringResource(OnboardingStrings.mockCreateTitle),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Surface(
                shape = RoundedCornerShape(HatliRadius.pill),
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Text(
                    text = stringResource(OnboardingStrings.mockStep3Of3),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 7.5.sp,
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                )
            }
        }

        // Mini 3-Step Wizard Indicator matching CreateRequestScreen (Step 3 active)
        WizardStepTrackMini(currentStep = 3)

        // Scrollable Review Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // Headline
            Text(
                text = stringResource(OnboardingStrings.mockReviewHeadline),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.5.sp,
                ),
                color = MaterialTheme.colorScheme.onBackground,
            )

            // Delivery Fee Interactive Stepper Card matching FeeReviewStep
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.sm),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(
                        text = stringResource(OnboardingStrings.mockFeeStepperTitle),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = stringResource(OnboardingStrings.mockFeeStepperDesc),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 6.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )

                    // Stepper Row: (-) [ 35 EGP ] (+)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = HatliColors.CoralContainer,
                            modifier = Modifier.size(24.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = null,
                                    tint = HatliColors.BrandCoral,
                                    modifier = Modifier.size(12.dp),
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(HatliRadius.sm),
                            color = HatliColors.BrandCoral.copy(alpha = 0.10f),
                            modifier = Modifier.padding(horizontal = 8.dp),
                        ) {
                            Text(
                                text = stringResource(OnboardingStrings.mockFeeStepperAmount),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                ),
                                color = HatliColors.BrandCoral,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = HatliColors.BrandCoral,
                            modifier = Modifier.size(24.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp),
                                )
                            }
                        }
                    }
                }
            }

            // Cost & Details Breakdown Summary Card matching FeeReviewStep
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.sm),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = null,
                            tint = HatliColors.BrandTeal,
                            modifier = Modifier.size(12.dp),
                        )
                        Text(
                            text = stringResource(OnboardingStrings.mockOrderCostSummary),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.5.sp,
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        thickness = 0.5.dp,
                    )

                    SummaryRowMini(
                        label = stringResource(OnboardingStrings.mockReviewServiceTypeLabel),
                        value = stringResource(OnboardingStrings.mockReviewServiceTypeVal),
                    )

                    SummaryRowMini(
                        label = stringResource(OnboardingStrings.mockShopNameLabel),
                        value = stringResource(OnboardingStrings.mockShopNameValue),
                    )

                    SummaryRowMini(
                        label = stringResource(OnboardingStrings.mockItemsLabel),
                        value = "سكر، لبن، أرز، جبنة",
                    )

                    SummaryRowMini(
                        label = stringResource(OnboardingStrings.mockReviewDeliveryFeeLabel),
                        value = stringResource(OnboardingStrings.mockReviewDeliveryFeeVal),
                        valueColor = HatliColors.BrandCoral,
                        isBoldValue = true,
                    )

                    SummaryRowMini(
                        label = stringResource(OnboardingStrings.mockReviewItemsCostLabel),
                        value = stringResource(OnboardingStrings.mockReviewItemsCostNote),
                        isMuted = true,
                    )

                    SummaryRowMini(
                        label = stringResource(OnboardingStrings.mockReviewServiceChargeLabel),
                        value = stringResource(OnboardingStrings.mockReviewServiceChargeFree),
                        valueColor = HatliColors.BrandTeal,
                        isBoldValue = true,
                    )
                }
            }
        }

        // Action Buttons Footer matching CreateRequestScreen
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.sm),
                color = HatliColors.BrandCoral,
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(OnboardingStrings.mockBtnConfirm),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.5.sp,
                        ),
                        color = Color.White,
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(HatliRadius.sm),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 3.5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(OnboardingStrings.mockBtnBack),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 7.5.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

// =============================================================================
// SCREEN 3: Track Pending Request (RequestDetailScreen - Searching for Captain)
// =============================================================================

@Composable
private fun TrackPendingRequestPreviewScreen(modifier: Modifier = Modifier) {
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

// =============================================================================
// Reusable Lightweight Preview Subcomponents (Faithful to Production UI)
// =============================================================================

@Composable
private fun WizardStepTrackMini(currentStep: Int) {
    val steps = listOf(
        1 to stringResource(OnboardingStrings.mockStepService),
        2 to stringResource(OnboardingStrings.mockStepDetails),
        3 to stringResource(OnboardingStrings.mockStepReview),
    )

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            steps.forEach { (stepNumber, label) ->
                val isCompleted = stepNumber < currentStep
                val isCurrent = stepNumber == currentStep

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> MaterialTheme.colorScheme.primary
                                    isCurrent -> MaterialTheme.colorScheme.primaryContainer
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                },
                            )
                            .border(
                                width = if (isCurrent) 1.dp else 0.dp,
                                color = if (isCurrent) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(8.dp),
                            )
                        } else {
                            Text(
                                text = "$stepNumber",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 7.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        ),
                        color = if (isCurrent) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        LinearProgressIndicator(
            progress = {
                when (currentStep) {
                    1 -> 0.33f
                    2 -> 0.66f
                    else -> 1.0f
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer,
        )
    }
}

@Composable
private fun FormTextFieldMini(
    label: String,
    value: String,
    isMultiline: Boolean = false,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(HatliRadius.sm),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 6.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = if (isMultiline) 9.5.sp else 8.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun SummaryRowMini(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    isBoldValue: Boolean = false,
    isMuted: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f, fill = false),
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = if (isMuted) 6.sp else 7.5.sp,
                fontWeight = if (isBoldValue) FontWeight.Bold else FontWeight.Normal,
            ),
            color = if (isMuted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) else valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun DetailRowMini(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 1.dp)
                .size(10.dp),
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 6.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 9.sp,
                ),
                color = valueColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
