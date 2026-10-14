package com.app.hatli.feature.auth.presentation.onboarding.components.preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingStrings

/**
 * Screen 2 Mockup: Review Request & Captain Delivery Fee.
 * Faithfully mirrors CreateRequestScreen (Step 3 - FeeReviewStep).
 */
@Composable
internal fun FeeReviewRequestPreview(modifier: Modifier = Modifier) {
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
