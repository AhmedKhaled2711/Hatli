package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.CoralContainer
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestDefaults
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState
import com.app.hatli.feature.request.presentation.request.label

@Composable
fun FeeReviewStep(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    val currentFee = state.deliveryFeeText.toIntOrNull() ?: RequestDefaults.SUGGESTED_DELIVERY_FEE_EGP.toInt()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        // Delivery Fee Interactive Stepper Card
        HatliCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(HatliSpacing.md),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                Text(
                    text = "مقابل التوصيل المقترح للكابتن",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CharcoalDark,
                        fontSize = 14.sp,
                    ),
                )

                Text(
                    text = "يمكنك تعديل الأجر المقترح لتسريع قبول كباتن التوصيل في مدينتك.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CharcoalMuted,
                        fontSize = 12.sp,
                    ),
                )

                Spacer(modifier = Modifier.height(HatliSpacing.xxs))

                // Stepper Row: (-5 EGP) [ 35 ج.م ] (+5 EGP)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Decrement Button (-5 EGP)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(CoralContainer)
                            .clickable {
                                val newFee = (currentFee - 5).coerceAtLeast(15)
                                onEvent(CreateRequestUiEvent.DeliveryFeeChanged(newFee.toString()))
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "تقليل المقابل",
                            tint = BrandCoral,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    // Dynamic Fee Readout Display (Forced LTR for currency readout)
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = HatliSpacing.lg)
                                .clip(RoundedCornerShape(16.dp))
                                .background(BrandCoral.copy(alpha = 0.10f))
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "$currentFee EGP",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandCoral,
                                    fontSize = 24.sp,
                                ),
                            )
                        }
                    }

                    // Increment Button (+5 EGP)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(BrandCoral)
                            .clickable {
                                val newFee = currentFee + 5
                                onEvent(CreateRequestUiEvent.DeliveryFeeChanged(newFee.toString()))
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "زيادة المقابل",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }

        // Cost Breakdown Summary Card
        HatliCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(HatliSpacing.md),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = BrandTeal,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = "ملخص حساب وحالة الطلب",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark,
                            fontSize = 15.sp,
                        ),
                    )
                }

                ReviewSummaryRow(
                    label = "نوع الخدمة",
                    value = state.category?.label().orEmpty(),
                )

                ReviewSummaryRow(
                    label = "مقابل التوصيل للكابتن",
                    value = "$currentFee ج.م",
                )

                ReviewSummaryRow(
                    label = "تكلفة المشتريات",
                    value = "تُدفع للمندوب عند الاستلام حسب الفاتورة",
                )

                ReviewSummaryRow(
                    label = "رسوم الخدمة والتطبيق",
                    value = "مجاناً لفترة محدودة",
                )
            }
        }
    }
}

