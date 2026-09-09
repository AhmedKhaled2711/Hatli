package com.app.hatli.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Hatli production color system.
 *
 * Brand direction: mint teal primary `#35F9D1` + charcoal neutrals.
 *
 * Material 3 [ColorScheme] is derived from these semantic tokens.
 * Use [HatliTheme.extendedColors] for success / warning / info / border / disabled.
 */
object HatliColors {

    // -------------------------------------------------------------------------
    // Brand
    // -------------------------------------------------------------------------

    /** Figma Light Header */
    val HeaderLight = Color(0xFF30E6C1)

    /** Figma Dark Header */
    val HeaderDark = Color(0xFF121418)

    /** Home Stats Card background in Light mode. */
    val StatsCardLight = Color(0xFF07362D)

    /** Hatli brand teal — Figma Light Mode */
    val BrandTeal = Color(0xFF30E6C1)

    val OnStatsCardDark = Color(0xFF181A20)
    val OnStatsCardLight = BrandTeal

    /** Dark mode app background — Figma */
    val DarkBackground = Color(0xFF121418)

    val Teal900 = Color(0xFF063D36)
    val Teal800 = Color(0xFF075C50)
    val Teal700 = Color(0xFF0F9F89)
    val Teal500 = BrandTeal
    val Teal400 = Color(0xFF2FE0BD)
    val Teal300 = Color(0xFF72F4DC)
    val Teal100 = Color(0xFFD4FBF3)
    val Teal50 = Color(0xFFEDFFFB)

    // -------------------------------------------------------------------------
    // Neutrals
    // -------------------------------------------------------------------------

    val White = Color(0xFFFFFFFF)

    val Gray50 = Color(0xFFF7F7F7)
    val Gray100 = Color(0xFFEEEEEE)
    val Gray200 = Color(0xFFE0E0E0)
    val Gray300 = Color(0xFFBDBDBD)
    val Gray400 = Color(0xFF9E9E9E)
    val Gray500 = Color(0xFF757575)
    val Gray600 = Color(0xFF525252)
    val Gray700 = Color(0xFF262626)
    val Gray800 = Color(0xFF1E1E1E)
    val Gray900 = Color(0xFF121212)

    val DeepTeal = Color(0xFF063D36)

    // -------------------------------------------------------------------------
    // Semantic - Light
    // -------------------------------------------------------------------------

    val Primary = BrandTeal
    val OnPrimary = DeepTeal

    val Secondary = Gray700
    val OnSecondary = White

    val Surface = White
    val Background = Gray50

    // -------------------------------------------------------------------------
    // Semantic - Dark
    // -------------------------------------------------------------------------

    val DarkSurface = Color(0xFF1C1F24)
    val DarkSurfaceVariant = Color(0xFF24282E)
    val DarkBorder = Color(0xFF363A40)

    // -------------------------------------------------------------------------
    // Error
    // -------------------------------------------------------------------------

    val Error = Color(0xFFB3261E)
    val OnError = White
    val ErrorContainer = Color(0xFFF9DEDC)
    val OnErrorContainer = Color(0xFF410E0B)

    // -------------------------------------------------------------------------
    // Warning
    // -------------------------------------------------------------------------

    val Warning = Color(0xFFC47F0A)
    val WarningContainer = Color(0xFFFFF4D6)
    val OnWarning = Color(0xFF2A1C00)

    // -------------------------------------------------------------------------
    // Success
    // -------------------------------------------------------------------------

    val Success = Teal700
    val SuccessContainer = Teal100
    val OnSuccess = White

    // -------------------------------------------------------------------------
    // Info
    // -------------------------------------------------------------------------

    val Info = Color(0xFF2F6FED)
    val InfoContainer = Color(0xFFDCE8FF)
    val OnInfo = White

    // -------------------------------------------------------------------------
    // Text
    // -------------------------------------------------------------------------

    val TextPrimary = Gray800
    val TextSecondary = Gray500

    // -------------------------------------------------------------------------
    // Borders / Disabled
    // -------------------------------------------------------------------------

    val Border = Gray200
    val Disabled = Gray400
    val DisabledContainer = Gray100
}
