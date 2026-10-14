package com.app.hatli.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Hatli production color system.
 *
 * Official Brand Identity (from ic_hatli_logo.xml):
 * - Deep Petroleum Teal (#102F34): Logo structural anchor & primary brand tone.
 * - Vibrant Coral Gradient (#F16B5A -> #F05C4C / #FF6B35): Dynamic courier wings & high-energy CTAs.
 * - Soft Ice Sage Mist (#EAF2F1 -> #F5F8F7): Logo canvas & ambient surface background.
 * - Semantic Emerald (#0F9F55 / Container #E8F8F0): Verified runner status & success states.
 * - Deep Charcoal (#1E2022) / Night Canvas (#121418): High-contrast typography & dark mode surfaces.
 */
object HatliColors {

    // -------------------------------------------------------------------------
    // Official Logo & Vector Identity Tokens (from ic_hatli_logo.xml)
    // -------------------------------------------------------------------------

    val LogoDeepTeal = Color(0xFF102F34)      // Top crown & bottom foundation of 'H'
    val LogoCoralStart = Color(0xFFF16B5A)    // Courier speed-wing gradient start
    val LogoCoralEnd = Color(0xFFF05C4C)      // Courier speed-wing gradient end
    val LogoCanvasStart = Color(0xFFEAF2F1)   // Ice sage background gradient start
    val LogoCanvasEnd = Color(0xFFF5F8F7)     // Ice sage background gradient end

    // -------------------------------------------------------------------------
    // Definitive Primary Brand Tokens
    // -------------------------------------------------------------------------

    val BrandCoral = Color(0xFFFF6B35)
    val CoralContainer = Color(0xFFFFEFEA)

    // Backwards-compatible Brand Teal aliases mapped to Warm Coral & Success Green
    val BrandTeal = Color(0xFF0F9F55)
    val TealContainer = Color(0xFFE8F8F0)

    val HeaderLight = BrandCoral
    val HeaderDark = Color(0xFF121418)

    val StatsCardLight = Color(0xFFFF6B35)
    val OnStatsCardDark = Color(0xFF181A20)
    val OnStatsCardLight = Color(0xFFFFFFFF)

    val DarkBackground = Color(0xFF121418)

    // Legacy Teal scale aliases for backwards compatibility
    val Teal900 = Color(0xFF1E2022)
    val Teal800 = Color(0xFF075C50)
    val Teal700 = Color(0xFF0F9F55)
    val Teal500 = BrandCoral
    val Teal400 = Color(0xFF2FE0BD)
    val Teal300 = BrandCoral
    val Teal100 = Color(0xFFFFEFEA)
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

    val DeepTeal = Color(0xFF0F9F55)

    // -------------------------------------------------------------------------
    // Semantic Tokens - Light
    // -------------------------------------------------------------------------

    val Primary = BrandCoral
    val OnPrimary = White

    val Secondary = CharcoalDark
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
    // Semantic Success (Exclusive Green Domain) & Info
    // -------------------------------------------------------------------------

    val Success = Color(0xFF0F9F55)
    val SuccessContainer = Color(0xFFE8F8F0)
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

// Top-level aliases for direct imports across features
val BrandCoral = HatliColors.BrandCoral
val CoralContainer = HatliColors.CoralContainer
val BrandTeal = HatliColors.Success
val TealContainer = HatliColors.SuccessContainer
val OffWhiteCanvas = HatliColors.OffWhiteCanvas
val CharcoalDark = HatliColors.CharcoalDark
val CharcoalMuted = HatliColors.CharcoalMuted
val AmberPending = HatliColors.AmberPending
val SuccessGreen = HatliColors.Success
val SuccessContainerGreen = HatliColors.SuccessContainer
val LogoDeepTeal = HatliColors.LogoDeepTeal
val LogoCoralStart = HatliColors.LogoCoralStart
val LogoCoralEnd = HatliColors.LogoCoralEnd
val LogoCanvasStart = HatliColors.LogoCanvasStart
val LogoCanvasEnd = HatliColors.LogoCanvasEnd
