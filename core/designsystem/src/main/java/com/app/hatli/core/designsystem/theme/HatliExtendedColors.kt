package com.app.hatli.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class HatliExtendedColors(
    val headerBackground: Color,
    val onHeaderBackground: Color,
    val statsCardBackground: Color,
    val onStatsCardBackground: Color,
    val onWhiteAndBlack: Color,
    // Keep your existing extended colors here
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val info: Color,
    val infoContainer: Color,
    val border: Color,
    val disabled: Color,
    val disabledContainer: Color,
)

val LightHatliExtendedColors = HatliExtendedColors(
    headerBackground = HatliColors.HeaderLight,
    onHeaderBackground = HatliColors.DeepTeal,
    statsCardBackground = HatliColors.StatsCardLight,
    onStatsCardBackground = HatliColors.OnStatsCardLight,
    onWhiteAndBlack = HatliColors.White ,
    success = HatliColors.Success,
    successContainer = HatliColors.SuccessContainer,
    warning = HatliColors.Warning,
    warningContainer = HatliColors.WarningContainer,
    info = HatliColors.Info,
    infoContainer = HatliColors.InfoContainer,
    border = HatliColors.Border,
    disabled = HatliColors.Disabled,
    disabledContainer = HatliColors.DisabledContainer,
)

val DarkHatliExtendedColors = HatliExtendedColors(
    headerBackground = HatliColors.HeaderDark,
    onHeaderBackground = HatliColors.White,
    statsCardBackground = HatliColors.BrandTeal,
    onStatsCardBackground = HatliColors.OnStatsCardDark,
    onWhiteAndBlack = HatliColors.OnStatsCardDark,
    success = HatliColors.Success,
    successContainer = HatliColors.Teal900,
    warning = HatliColors.Warning,
    warningContainer = HatliColors.WarningContainer,
    info = HatliColors.Info,
    infoContainer = HatliColors.InfoContainer,
    border = HatliColors.DarkBorder,
    disabled = HatliColors.Gray500,
    disabledContainer = HatliColors.DarkSurfaceVariant,
)

val LocalHatliExtendedColors = staticCompositionLocalOf {
    LightHatliExtendedColors
}