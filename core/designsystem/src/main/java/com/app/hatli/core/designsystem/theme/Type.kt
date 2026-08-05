package com.app.hatli.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

private val HatliPlatformTextStyle = PlatformTextStyle(includeFontPadding = false)

private val HatliLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None,
)

private fun hatliTextStyle(
    weight: FontWeight,
    size: androidx.compose.ui.unit.TextUnit,
    lineHeight: androidx.compose.ui.unit.TextUnit,
) = TextStyle(
    fontFamily = HatliFontFamily,
    fontWeight = weight,
    fontSize = size,
    lineHeight = lineHeight,
    platformStyle = HatliPlatformTextStyle,
    lineHeightStyle = HatliLineHeightStyle,
)

/**
 * Material 3 type scale — all styles use [HatliFontFamily] (Mada).
 * Prefer `MaterialTheme.typography.*` in feature UI (never raw system fonts).
 */
val HatliTypography = Typography(
    displayLarge = hatliTextStyle(FontWeight.Bold, 40.sp, 48.sp),
    headlineLarge = hatliTextStyle(FontWeight.SemiBold, 28.sp, 36.sp),
    headlineMedium = hatliTextStyle(FontWeight.SemiBold, 24.sp, 32.sp),
    headlineSmall = hatliTextStyle(FontWeight.SemiBold, 20.sp, 28.sp),
    titleLarge = hatliTextStyle(FontWeight.SemiBold, 22.sp, 28.sp),
    titleMedium = hatliTextStyle(FontWeight.Medium, 16.sp, 24.sp),
    titleSmall = hatliTextStyle(FontWeight.Medium, 14.sp, 20.sp),
    bodyLarge = hatliTextStyle(FontWeight.Normal, 16.sp, 24.sp),
    bodyMedium = hatliTextStyle(FontWeight.Normal, 14.sp, 20.sp),
    bodySmall = hatliTextStyle(FontWeight.Normal, 12.sp, 16.sp),
    labelLarge = hatliTextStyle(FontWeight.Medium, 14.sp, 20.sp),
    labelMedium = hatliTextStyle(FontWeight.Medium, 12.sp, 16.sp),
    labelSmall = hatliTextStyle(FontWeight.Medium, 11.sp, 16.sp),
)
