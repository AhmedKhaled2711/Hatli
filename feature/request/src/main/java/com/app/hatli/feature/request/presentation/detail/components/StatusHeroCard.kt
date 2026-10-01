package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.TaskAlt
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.BrandTeal
import com.app.hatli.core.designsystem.theme.CharcoalDark
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestStatus

@Composable
fun StatusHeroCard(req: Request, isAr: Boolean) {
    val isPending = req.status == RequestStatus.Pending
    val isAcceptedOrProgress = req.status in setOf(RequestStatus.Accepted, RequestStatus.InProgress, RequestStatus.Purchased)
    val isOnTheWay = req.status == RequestStatus.OnTheWay
    val isCompleted = req.status == RequestStatus.Completed || req.status == RequestStatus.Delivered
    val isCancelled = req.status == RequestStatus.Cancelled

    val containerBg by animateColorAsState(
        targetValue = when {
            isPending -> Color(0xFFFFF4D6)
            isAcceptedOrProgress || isCompleted -> Color(0xFFEAFDF9)
            isOnTheWay -> Color(0xFFFFF4F0)
            isCancelled -> Color(0xFFFDE8E8)
            else -> Color(0xFFF8F9FA)
        },
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "heroColorMorph",
    )

    val accentColor = when {
        isPending -> Color(0xFFD97706)
        isAcceptedOrProgress || isCompleted -> BrandTeal
        isOnTheWay -> BrandCoral
        isCancelled -> Color(0xFFE53E3E)
        else -> CharcoalDark
    }

    // Pulsing search animation for Pending status
    val infiniteTransition = rememberInfiniteTransition(label = "PendingPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseAlpha",
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(containerBg)
            .padding(HatliSpacing.lg),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = when {
                                isPending -> Icons.Default.HourglassTop
                                isAcceptedOrProgress -> Icons.Default.DirectionsBike
                                isOnTheWay -> Icons.Default.LocalShipping
                                else -> Icons.Default.TaskAlt
                            },
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    Text(
                        text = when {
                            isPending -> "جاري البحث عن كابتن..."
                            isAcceptedOrProgress -> "تم القبول وجاري التحضير"
                            isOnTheWay -> "الكابتن في الطريق إليك"
                            isCompleted -> "تم التوصيل بنجاح"
                            isCancelled -> "تم إلغاء الطلب"
                            else -> "حالة الطلب"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            fontSize = 15.sp,
                        ),
                        modifier = Modifier.graphicsLayer {
                            if (isPending) alpha = pulseAlpha
                        },
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = req.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = CharcoalDark,
                        fontSize = 18.sp,
                    ),
                )

                Text(
                    text = when {
                        isPending -> "سيتم إبلاغك فور قبول أحد الكباتن القريبين لطلبك."
                        isOnTheWay -> "يمكنك تتبع الخريطة والتواصل المباشر مع الكابتن."
                        isAcceptedOrProgress -> "الكابتن ${req.runnerName ?: ""} يعمل على تنفيذ طلبك."
                        isCompleted -> "شكراً لاستخدامك هاتلي! نتمنى لك يوماً سعيداً."
                        else -> "تفاصيل الطلب ومتابعة الحالة الحية."
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CharcoalDark.copy(alpha = 0.75f),
                        fontSize = 12.sp,
                    ),
                )
            }
        }
    }
}

