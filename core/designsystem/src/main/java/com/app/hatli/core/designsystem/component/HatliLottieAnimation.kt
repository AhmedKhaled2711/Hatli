package com.app.hatli.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * Plays a Lottie JSON file from `src/main/assets/` (path relative to assets root).
 *
 * While loading: empty [Box] (no flash). On failure: [fallback]. On success: animation only.
 *
 * Example asset path: `lottie/splash/delivery_man.json`
 */
@Composable
fun HatliLottieAnimation(
    assetPath: String,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    fallback: @Composable () -> Unit = {},
) {
    val compositionResult = rememberLottieComposition(LottieCompositionSpec.Asset(assetPath))
    val composition by compositionResult

    when {
        composition != null -> {
            LottieAnimation(
                composition = composition,
                modifier = modifier,
                iterations = iterations,
            )
        }
        compositionResult.isFailure -> fallback()
        else -> Box(modifier = modifier) // loading — keep size, show nothing
    }
}
