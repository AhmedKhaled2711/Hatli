package com.app.hatli.feature.request.presentation.detail.components

import androidx.compose.runtime.Composable

@Composable
fun animateFloatAsState(
    targetValue: Float,
    animationSpec: androidx.compose.animation.core.AnimationSpec<Float>,
    label: String
) = androidx.compose.animation.core.animateFloatAsState(
    targetValue = targetValue,
    animationSpec = animationSpec,
    label = label
)
