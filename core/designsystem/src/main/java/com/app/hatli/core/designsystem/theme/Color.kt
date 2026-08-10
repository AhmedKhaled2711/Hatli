package com.app.hatli.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Hatli production color system.
 *
 * Brand direction: mint teal primary `#30E6C1` + charcoal neutrals `#1E1E1E` / `#262626` / `#FFFFFF`.
 * Built for light + dark screens (auth, home, lists, status chips) with WCAG-minded contrast.
 *
 * Semantic tokens map 1:1 to product UI. Material 3 [ColorScheme] is derived in [Theme.kt];
 * use [HatliTheme.extendedColors] for success / warning / info / border / disabled.
 */
object HatliColors {

    // --- Brand primary (Mint Teal) — canonical `#30E6C1` ---
    /** Deepest teal — onPrimaryContainer, dark success text. */
    val Teal900 = Color(0xFF063D36)
    /** Pressed / dark primary container. */
    val Teal800 = Color(0xFF0A5C50)
    /** Strong teal for dark-mode emphasis & success. */
    val Teal700 = Color(0xFF129A84)
    /** Brand primary (filled buttons, key icons, active nav). */
    val Teal500 = Color(0xFF30E6C1)
    /** Alternate brand swatch from design sheet (`#30E5C0`). */
    val Teal400 = Color(0xFF30E5C0)
    /** Soft teal for dark-mode primary / icons. */
    val Teal300 = Color(0xFF6FEFD8)
    /** Light primary container / selected chip wash. */
    val Teal100 = Color(0xFFD2FBF3)
    /** Subtle teal tint backgrounds. */
    val Teal50 = Color(0xFFECFDF9)

    // --- Neutrals (brand charcoal + white) ---
    val White = Color(0xFFFFFFFF)
    val Gray50 = Color(0xFFF7F7F7)
    val Gray100 = Color(0xFFEEEEEE)
    val Gray200 = Color(0xFFE0E0E0)
    val Gray300 = Color(0xFFBDBDBD)
    val Gray400 = Color(0xFF9E9E9E)
    val Gray500 = Color(0xFF757575)
    val Gray600 = Color(0xFF525252)
    /** Elevated dark surface / secondary chrome — `#262626`. */
    val Gray700 = Color(0xFF262626)
    /** Scaffold dark background / onPrimary — `#1E1E1E`. */
    val Gray800 = Color(0xFF1E1E1E)
    val Gray900 = Color(0xFF121212)
    val DeepTeal = Color(0xFF1A443B)

    // --- Semantic: product palette ---

    /** Primary brand action color. */
    val Primary = Teal500
    /**
     * Text/icons on primary fills.
     * Dark charcoal (not white) — bright teal fails contrast with white.
     */
    val OnPrimary = DeepTeal
    /** Secondary chrome / dark CTAs / emphasis surfaces. */
    val Secondary = Gray700
    val OnSecondary = White
    /** Card / sheet surface (light). */
    val Surface = White
    /** App scaffold background (light). */
    val Background = Gray50

    /** Destructive / validation errors. */
    val Error = Color(0xFFB3261E)
    val OnError = White
    val ErrorContainer = Color(0xFFF9DEDC)
    val OnErrorContainer = Color(0xFF410E0B)

    /** Caution: delays, pending review. */
    val Warning = Color(0xFFC47F0A)
    val WarningContainer = Color(0xFFFFF4D6)
    val OnWarning = Color(0xFF2A1C00)

    /** Positive completion / delivered / verified (teal family). */
    val Success = Teal700
    val SuccessContainer = Teal100
    val OnSuccess = White

    /** Informational / neutral guidance. */
    val Info = Color(0xFF2F6FED)
    val InfoContainer = Color(0xFFDCE8FF)
    val OnInfo = White

    /** High-emphasis body text. */
    val TextPrimary = Gray800
    /** Supporting / meta text. */
    val TextSecondary = Gray500
    /** Dividers, input outlines, card strokes. */
    val Border = Gray200
    /** Disabled text / icons. */
    val Disabled = Gray400
    /** Disabled control fill. */
    val DisabledContainer = Gray100

    // --- Dark-mode surfaces (brand charcoal) ---
    val DarkBackground = Gray800
    val DarkSurface = Gray700
    val DarkSurfaceVariant = Color(0xFF2E2E2E)
    val DarkBorder = Color(0xFF3A3A3A)
}
