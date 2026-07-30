package com.app.hatli.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Semantic colors outside the Material 3 ColorScheme (success / warning / info / border / disabled).
 * Always read via [HatliTheme.extendedColors] so light and dark stay in sync.
 */
@Immutable
data class HatliExtendedColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val info: Color,
    val onInfo: Color,
    val infoContainer: Color,
    val onInfoContainer: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val disabled: Color,
    val disabledContainer: Color,
)

val LightHatliExtendedColors = HatliExtendedColors(
    success = HatliColors.Success,
    onSuccess = HatliColors.OnSuccess,
    successContainer = HatliColors.SuccessContainer,
    onSuccessContainer = HatliColors.Forest900,
    warning = HatliColors.Warning,
    onWarning = HatliColors.OnWarning,
    warningContainer = HatliColors.WarningContainer,
    onWarningContainer = HatliColors.OnWarning,
    info = HatliColors.Info,
    onInfo = HatliColors.OnInfo,
    infoContainer = HatliColors.InfoContainer,
    onInfoContainer = HatliColors.Info,
    border = HatliColors.Border,
    textPrimary = HatliColors.TextPrimary,
    textSecondary = HatliColors.TextSecondary,
    disabled = HatliColors.Disabled,
    disabledContainer = HatliColors.DisabledContainer,
)

val DarkHatliExtendedColors = HatliExtendedColors(
    success = Color(0xFF7BC4A4),
    onSuccess = HatliColors.Forest900,
    successContainer = Color(0xFF0F3D30),
    onSuccessContainer = HatliColors.Forest100,
    warning = Color(0xFFF0C45A),
    onWarning = Color(0xFF2A1C00),
    warningContainer = Color(0xFF3D2E08),
    onWarningContainer = HatliColors.Amber100,
    info = Color(0xFF8BB4E0),
    onInfo = HatliColors.Slate900,
    infoContainer = Color(0xFF1A334D),
    onInfoContainer = HatliColors.InfoContainer,
    border = HatliColors.DarkBorder,
    textPrimary = HatliColors.Slate50,
    textSecondary = HatliColors.Slate400,
    disabled = Color(0xFF6B7A74),
    disabledContainer = Color(0xFF24302C),
)

val LocalHatliExtendedColors = staticCompositionLocalOf { LightHatliExtendedColors }
