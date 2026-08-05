package com.app.hatli.core.designsystem.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.app.hatli.core.designsystem.R

/**
 * Canonical Hatli typefaces.
 *
 * **Files:** `core/designsystem/src/main/res/font/`
 * - **Mada** — primary UI family (full weight set, EN + AR)
 * - **Tajawal** — alternate / marketing (expose via [HatliFonts.Tajawal])
 *
 * Features use [HatliFontFamily] or MaterialTheme.typography — never feature-local fonts.
 */
val HatliFontFamily: FontFamily = FontFamily(
    Font(R.font.mada_extralight, FontWeight.ExtraLight),
    Font(R.font.mada_light, FontWeight.Light),
    Font(R.font.mada_regular, FontWeight.Normal),
    Font(R.font.mada_medium, FontWeight.Medium),
    Font(R.font.mada_semibold, FontWeight.SemiBold),
    Font(R.font.mada_bold, FontWeight.Bold),
    Font(R.font.mada_extrabold, FontWeight.ExtraBold),
)

val HatliTajawalFontFamily: FontFamily = FontFamily(
    Font(R.font.tajawal_extralight, FontWeight.ExtraLight),
    Font(R.font.tajawal_light, FontWeight.Light),
    Font(R.font.tajawal_regular, FontWeight.Normal),
    Font(R.font.tajawal_medium, FontWeight.Medium),
    Font(R.font.tajawal_bold, FontWeight.Bold),
)

/**
 * Accessors for explicit `Text(..., fontFamily = …)` when not using
 * MaterialTheme.typography.
 */
object HatliFonts {
    /** Primary brand UI face (Mada). Same as [HatliFontFamily]. */
    val Default: FontFamily = HatliFontFamily

    /** Alternate face (Tajawal) — optional for display / marketing. */
    val Tajawal: FontFamily = HatliTajawalFontFamily

    val extraLight: FontWeight = FontWeight.ExtraLight
    val light: FontWeight = FontWeight.Light
    val normal: FontWeight = FontWeight.Normal
    val medium: FontWeight = FontWeight.Medium
    val semiBold: FontWeight = FontWeight.SemiBold
    val bold: FontWeight = FontWeight.Bold
    val extraBold: FontWeight = FontWeight.ExtraBold
}
