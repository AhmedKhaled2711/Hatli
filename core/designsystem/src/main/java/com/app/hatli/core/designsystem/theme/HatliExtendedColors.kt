package com.app.hatli.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class HatliExtendedColors(
    val headerBackground: Color,
    val onHeaderBackground: Color,
    val statsCardBackground: Color,
    val onStatsCardBackground: Color,
    val onWhiteAndBlack: Color,
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val info: Color,
    val infoContainer: Color,
    val border: Color,
    val disabled: Color,
    val disabledContainer: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val coralPrimary: Color = HatliColors.BrandCoral,
    val coralContainer: Color = HatliColors.CoralContainer,
    val amberPending: Color = HatliColors.AmberPending,
    val mintAccent: Color = HatliColors.BrandTeal,
    val offWhiteCanvas: Color = HatliColors.OffWhiteCanvas,
)

val LightHatliExtendedColors = HatliExtendedColors(
    headerBackground = HatliColors.HeaderLight,
    onHeaderBackground = HatliColors.DeepTeal,
    statsCardBackground = HatliColors.StatsCardLight,
    onStatsCardBackground = HatliColors.OnStatsCardLight,
    onWhiteAndBlack = HatliColors.White,
    success = HatliColors.Success,
    successContainer = HatliColors.SuccessContainer,
    warning = HatliColors.Warning,
    warningContainer = HatliColors.WarningContainer,
    info = HatliColors.Info,
    infoContainer = HatliColors.InfoContainer,
    border = HatliColors.Border,
    disabled = HatliColors.Disabled,
    disabledContainer = HatliColors.DisabledContainer,
    surfaceVariant = HatliColors.Gray100,
    onSurfaceVariant = HatliColors.CharcoalMuted,
    coralPrimary = HatliColors.BrandCoral,
    coralContainer = HatliColors.CoralContainer,
    amberPending = HatliColors.AmberPending,
    mintAccent = HatliColors.BrandTeal,
    offWhiteCanvas = HatliColors.OffWhiteCanvas,
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
    surfaceVariant = HatliColors.DarkSurfaceVariant,
    onSurfaceVariant = HatliColors.Gray400,
    coralPrimary = HatliColors.BrandCoral,
    coralContainer = Color(0xFF4A1F12),
    amberPending = HatliColors.AmberPending,
    mintAccent = HatliColors.BrandTeal,
    offWhiteCanvas = HatliColors.DarkBackground,
)

val LocalHatliExtendedColors = staticCompositionLocalOf {
    LightHatliExtendedColors
}