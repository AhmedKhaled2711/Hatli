package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliTextField
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.CharcoalMuted
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.feature.request.R
import com.app.hatli.feature.request.presentation.request.CreateRequestUiEvent
import com.app.hatli.feature.request.presentation.request.CreateRequestUiState

private val LocalLandmarks = listOf(
    "الميدان القديم",
    "شارع البحر الرئيسي",
    "أمام المستشفى العام",
    "موقف السيارات الرئيسي",
    "مبنى المحافظة",
)

@Composable
fun DetailsStep(
    state: CreateRequestUiState,
    onEvent: (CreateRequestUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        when (state.category) {
            RequestCategory.Shopping -> {
                HatliTextField(
                    value = state.shopName,
                    onValueChange = { onEvent(CreateRequestUiEvent.ShopNameChanged(it)) },
                    label = "اسم المتجر أو الصيدلية (مثال: صيدلية الأمل / خير زمان)",
                    modifier = Modifier.fillMaxWidth(),
                )

                // Multiline Items Description Input with Item Counter
                Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xxs)) {
                    HatliTextField(
                        value = state.itemsText,
                        onValueChange = { onEvent(CreateRequestUiEvent.ItemsChanged(it)) },
                        label = "قائمة الطلبات والمشتريات التفصيلية",
                        placeholder = "اكتب تفاصيل طلبك بالكامل (مثال: 2 كجم سكر، 1 لبن كامل الدسم، علبة بندول إكسترا)...",
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        Text(
                            text = "${state.itemsText.length} حرفاً مدخلاً",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CharcoalMuted,
                                fontSize = 11.sp,
                            ),
                        )
                    }
                }

                HatliTextField(
                    value = state.notes,
                    onValueChange = { onEvent(CreateRequestUiEvent.NotesChanged(it)) },
                    label = "ملاحظات إضافية للكابتن (اختياري)",
                    modifier = Modifier.fillMaxWidth(),
                )

                HatliTextField(
                    value = state.budgetText,
                    onValueChange = { onEvent(CreateRequestUiEvent.BudgetChanged(it)) },
                    label = "الميزانية التقديرية للمشتريات (ج.م)",
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            RequestCategory.Pickup -> {
                // Local Landmark Selector Chips for Pickup Point
                Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
                    Text(
                        text = "معالم محلية شائعة لنقطة الاستلام",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark,
                            fontSize = 13.sp,
                        ),
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
                        items(LocalLandmarks) { landmark ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .clickable { onEvent(CreateRequestUiEvent.PickupAddressChanged(landmark)) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                            ) {
                                Text(
                                    text = landmark,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = BrandCoral,
                                        fontSize = 12.sp,
                                    ),
                                )
                            }
                        }
                    }
                }

                HatliTextField(
                    value = state.pickupAddress,
                    onValueChange = { onEvent(CreateRequestUiEvent.PickupAddressChanged(it)) },
                    label = "مكان استلام الطرد (العنوان أو المعلم القريب)",
                    modifier = Modifier.fillMaxWidth(),
                )

                // Saved Address Preview Card for Dropoff
                SavedAddressCard(
                    addressTitle = "عنوان التوصيل (منزلك)",
                    addressDetail = if (state.dropoffAddress.isNotBlank()) state.dropoffAddress else "المحلة الكبرى - حي الأشجار - عمارة 14",
                )

                HatliTextField(
                    value = state.pickupItemDescription,
                    onValueChange = { onEvent(CreateRequestUiEvent.PickupItemChanged(it)) },
                    label = "وصف الطرد أو الأغراض المنقولة",
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            RequestCategory.Bills -> {
                HatliTextField(
                    value = state.billType,
                    onValueChange = { onEvent(CreateRequestUiEvent.BillTypeChanged(it)) },
                    label = "نوع الفاتورة / الخدمة (كهرباء · مياه · غاز · شحن رصيد)",
                    modifier = Modifier.fillMaxWidth(),
                )

                HatliTextField(
                    value = state.accountRef,
                    onValueChange = { onEvent(CreateRequestUiEvent.AccountRefChanged(it)) },
                    label = "رقم الاشتراك / الحساب / العداد",
                    modifier = Modifier.fillMaxWidth(),
                )

                HatliTextField(
                    value = state.billAmountText,
                    onValueChange = { onEvent(CreateRequestUiEvent.BillAmountChanged(it)) },
                    label = "قيمة الفاتورة التقديرية (ج.م)",
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            RequestCategory.Custom -> {
                HatliTextField(
                    value = state.customTitle,
                    onValueChange = { onEvent(CreateRequestUiEvent.CustomTitleChanged(it)) },
                    label = "عنوان المشوار / المهمة",
                    modifier = Modifier.fillMaxWidth(),
                )

                HatliTextField(
                    value = state.customBody,
                    onValueChange = { onEvent(CreateRequestUiEvent.CustomBodyChanged(it)) },
                    label = "شرح وتفاصيل المشوار بالكامل",
                    placeholder = "اشرح للكابتن بالتفصيل ما المطلوب القيام به في مدينتك...",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth(),
                )

                HatliTextField(
                    value = state.budgetText,
                    onValueChange = { onEvent(CreateRequestUiEvent.BudgetChanged(it)) },
                    label = "الميزانية المتوقعة (ج.م)",
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            null -> Unit
        }
    }
}

@Composable
private fun SavedAddressCard(
    addressTitle: String,
    addressDetail: String,
) {
    HatliCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(HatliSpacing.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BrandCoral.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = BrandCoral,
                        modifier = Modifier.size(20.dp),
                    )
                }

                Column {
                    Text(
                        text = addressTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = CharcoalDark,
                            fontSize = 13.sp,
                        ),
                    )
                    Text(
                        text = addressDetail,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CharcoalMuted,
                            fontSize = 12.sp,
                        ),
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "تعديل",
                tint = CharcoalMuted,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

