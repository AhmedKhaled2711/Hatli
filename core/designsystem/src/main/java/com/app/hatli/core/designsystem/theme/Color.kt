package com.app.hatli.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Hatli production color system.
 *
 * Brand direction: Warm Coral primary (#FF6B35) + Mint Teal (#30E6C1) + Off-White canvas (#F8F9FA).
 */
object HatliColors {

    // -------------------------------------------------------------------------
    // Brand Tokens
    // -------------------------------------------------------------------------

    val BrandCoral = Color(0xFFFF6B35)
    val CoralContainer = Color(0xFFFFEFEA)
    val BrandTeal = Color(0xFF30E6C1)
    val TealContainer = Color(0xFFEAFDF9)

    val HeaderLight = BrandTeal
    val HeaderDark = Color(0xFF121418)

    val StatsCardLight = Color(0xFF07362D)
    val OnStatsCardDark = Color(0xFF181A20)
    val OnStatsCardLight = BrandTeal

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
    val OffWhiteCanvas = Color(0xFFF8F9FA)
    val CharcoalDark = Color(0xFF1E2022)
    val CharcoalMuted = Color(0xFF686E74)

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
    // Semantic Tokens - Light
    // -------------------------------------------------------------------------

    val Primary = BrandTeal
    val OnPrimary = DeepTeal

    val Secondary = Gray700
    val OnSecondary = White

    val Surface = White
    val Background = OffWhiteCanvas

    // -------------------------------------------------------------------------
    // Semantic Tokens - Dark
    // -------------------------------------------------------------------------

    val DarkSurface = Color(0xFF1C1F24)
    val DarkSurfaceVariant = Color(0xFF24282E)
    val DarkBorder = Color(0xFF363A40)

    // -------------------------------------------------------------------------
    // Error & Warnings
    // -------------------------------------------------------------------------

    val Error = Color(0xFFB3261E)
    val OnError = White
    val ErrorContainer = Color(0xFFF9DEDC)
    val OnErrorContainer = Color(0xFF410E0B)

    val Warning = Color(0xFFC47F0A)
    val WarningContainer = Color(0xFFFFF4D6)
    val AmberPending = Color(0xFFFFB800)

    // -------------------------------------------------------------------------
    // Success & Info
    // -------------------------------------------------------------------------

    val Success = Teal700
    val SuccessContainer = Teal100
    val OnSuccess = White

    val Info = Color(0xFF2F6FED)
    val InfoContainer = Color(0xFFDCE8FF)
    val OnInfo = White

    // -------------------------------------------------------------------------
    // Typography Tones
    // -------------------------------------------------------------------------

    val TextPrimary = CharcoalDark
    val TextSecondary = CharcoalMuted

    // -------------------------------------------------------------------------
    // Borders / Disabled
    // -------------------------------------------------------------------------

    val Border = Gray200
    val Disabled = Gray400
    val DisabledContainer = Gray100
}
