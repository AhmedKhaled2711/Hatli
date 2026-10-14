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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingStrings

/**
 * Screen 1 Mockup: Create Supermarket Request.
 * Faithfully mirrors CreateRequestScreen (Step 2 - DetailsStep) for RequestCategory.Shopping.
 */
@Composable
internal fun CreateSupermarketRequestPreview(modifier: Modifier = Modifier) {
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

        // Mini 3-Step Wizard Indicator matching CreateRequestScreen (Step 2 active)
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
