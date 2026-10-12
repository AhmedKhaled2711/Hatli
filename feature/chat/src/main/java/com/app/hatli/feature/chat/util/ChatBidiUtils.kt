package com.app.hatli.feature.chat.util

import androidx.compose.ui.unit.LayoutDirection

/**
 * Resolves the layout direction (LTR or RTL) based on the first strong directional
 * character in [text], replicating the dynamic bidirectional behavior of WhatsApp.
 *
 * - If the first strong character is Arabic, Hebrew, or other RTL script, returns [LayoutDirection.Rtl].
 * - If the first strong character is Latin or another LTR script, returns [LayoutDirection.Ltr].
 * - If the text is empty or contains only neutral characters (e.g. whitespace, numbers, symbols, emojis),
 *   it falls back to [fallback].
 */
fun resolveBidiLayoutDirection(
    text: String,
    fallback: LayoutDirection = LayoutDirection.Ltr,
): LayoutDirection {
    var index = 0
    val length = text.length
    while (index < length) {
        val codePoint = text.codePointAt(index)
        when (Character.getDirectionality(codePoint)) {
            Character.DIRECTIONALITY_RIGHT_TO_LEFT,
            Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC -> return LayoutDirection.Rtl
            Character.DIRECTIONALITY_LEFT_TO_RIGHT -> return LayoutDirection.Ltr
        }
        index += Character.charCount(codePoint)
    }
    return fallback
}
