package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliColors

@Composable
fun AnimatedSuccessCheckmark(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    circleColor: Color = HatliColors.BrandTeal,
    checkColor: Color = HatliColors.DeepTeal,
    repeatCheckmark: Boolean = true // نتحكم إذا كانت علامة الصح تكرر رسمها أم ترسم مرة واحدة
) {
    // 1. انيميشن الـ Pulse الدائم للدائرة الخارجيّة (Breath / Ripple Effect)
    val infiniteTransition = rememberInfiniteTransition(label = "CirclePulseTransition")
    val circleScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CircleScale"
    )

    // 2. انيميشن رسم علامة الصح نفسها (Stroke Path Progress من 0f لـ 1f)
    val checkProgress = remember { Animatable(0f) }

    LaunchedEffect(repeatCheckmark) {
        if (repeatCheckmark) {
            // رسم متكرر لعلامة الصح (تترسم وتستنى ثانية وتكرر)
            while (true) {
                checkProgress.snapTo(0f)
                checkProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                )
                kotlinx.coroutines.delay(1800) // التوقف بين كل رسمة والثانية
            }
        } else {
            // ترسم مرة واحدة فقط بـ Elastic/Spring Feel
            checkProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = circleScale
                scaleY = circleScale
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasWidth = size.toPx()
            val canvasHeight = size.toPx()
            val radius = canvasWidth / 2f

            // أ) رسم الخلفية الدائرية
            drawCircle(
                color = circleColor,
                radius = radius
            )

            // ب) بناء مسار (Path) علامة الصح المخصصة
            val checkPath = Path().apply {
                // بداية الرسم من اليسار (Start of check)
                moveTo(canvasWidth * 0.28f, canvasHeight * 0.52f)
                // خط النزول لمنتصف العلامة (Vertex)
                lineTo(canvasWidth * 0.45f, canvasHeight * 0.68f)
                // خط الصعود لأعلى اليمين (End of check)
                lineTo(canvasWidth * 0.72f, canvasHeight * 0.38f)
            }

            // ج) تقطيع المسار بناءً على نسبة الـ progress الكائنة من الأنيميشن
            val pathMeasure = PathMeasure()
            pathMeasure.setPath(checkPath, false)
            val pathSegment = Path()
            pathMeasure.getSegment(
                startDistance = 0f,
                stopDistance = pathMeasure.length * checkProgress.value,
                destination = pathSegment,
                startWithMoveTo = true
            )

            // د) رسم خط علامة الصح سميك ودائري الحواف
            drawPath(
                path = pathSegment,
                color = checkColor,
                style = Stroke(
                    width = canvasWidth * 0.08f, // سمك الحافة بالنسبة لقطر الدائرة
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}
