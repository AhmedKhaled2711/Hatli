package com.app.hatli.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Hatli production color system.
 *
 * Brand direction: deep forest (trust, reliability) + restrained amber (energy, urgency).
 * Avoids generic purple/indigo gradients and warm cream/terracotta clichés.
 *
 * Semantic tokens below map 1:1 to product UI (buttons, status chips, borders, disabled).
 * Material 3 ColorScheme is derived in [Theme.kt]; use [HatliColors] for non-M3 semantics
 * (success / warning / info / border / disabled).
 */
object HatliColors {
    // --- Brand primary (Forest) ---
    val Forest900 = Color(0xFF0B2E24)
    val Forest800 = Color(0xFF0F3D30)
    val Forest700 = Color(0xFF145C48)
    val Forest500 = Color(0xFF1F7A61)
    val Forest300 = Color(0xFF6BA894)
    val Forest100 = Color(0xFFD8EBE4)

    // --- Brand secondary (Amber) ---
    val Amber700 = Color(0xFF9A6A04)
    val Amber500 = Color(0xFFC9920A)
    val Amber200 = Color(0xFFF5D78E)
    val Amber100 = Color(0xFFFBF0D2)

    // --- Neutrals (Slate-green tint for brand cohesion) ---
    val Slate50 = Color(0xFFF4F6F5)
    val SoftWhite = Color(0xFFFBFCFB)
    val Slate100 = Color(0xFFE6EBE9)
    val Slate200 = Color(0xFFD5DED9)
    val Slate400 = Color(0xFF8A9A93)
    val Slate500 = Color(0xFF5A6B64)
    val Slate700 = Color(0xFF2A3531)
    val Slate800 = Color(0xFF1C2421)
    val Slate900 = Color(0xFF111816)

    // --- Semantic: required product palette ---
    /** Primary brand action color (filled buttons, key icons). */
    val Primary = Forest700
    /** Secondary accent (highlights, badges, secondary CTAs). */
    val Secondary = Amber500
    /** Card / sheet surface (light). Prefer SoftWhite over flat white for softness. */
    val Surface = SoftWhite
    /** App scaffold background. */
    val Background = Slate50
    /** Destructive / validation errors. */
    val Error = Color(0xFFB3261E)
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer = Color(0xFFF9DEDC)
    val OnErrorContainer = Color(0xFF410E0B)
    /** Caution: delays, pending review, soft blocks. */
    val Warning = Color(0xFF9A6A04)
    val WarningContainer = Color(0xFFFBF0D2)
    val OnWarning = Color(0xFF2A1C00)
    /** Positive completion / delivered / verified. */
    val Success = Color(0xFF1B6B4A)
    val SuccessContainer = Color(0xFFD8EBE4)
    val OnSuccess = Color(0xFFFFFFFF)
    /** Informational / neutral guidance (not brand). */
    val Info = Color(0xFF2F5D8C)
    val InfoContainer = Color(0xFFDCE8F5)
    val OnInfo = Color(0xFFFFFFFF)
    /** High-emphasis body text. */
    val TextPrimary = Slate900
    /** Supporting / meta text. */
    val TextSecondary = Slate500
    /** Dividers, input outlines, card strokes. */
    val Border = Slate200
    /** Disabled text / icons. */
    val Disabled = Slate400
    /** Disabled control fill. */
    val DisabledContainer = Color(0xFFE8EEEC)

    // --- Dark-mode surface elevations ---
    val DarkBackground = Slate900
    val DarkSurface = Color(0xFF161E1B)
    val DarkSurfaceVariant = Slate800
    val DarkBorder = Color(0xFF2F3B36)
}
