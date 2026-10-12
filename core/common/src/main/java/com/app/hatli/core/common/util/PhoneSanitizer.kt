package com.app.hatli.core.common.util

/**
 * Sanitizes Egyptian phone numbers into strict E.164 format (+201XXXXXXXXX).
 *
 * Supported user inputs:
 * - "01007394856"      -> "+201007394856"
 * - "1007394856"       -> "+201007394856"
 * - "+201007394856"    -> "+201007394856"
 * - "+20 100 739 4856" -> "+201007394856"
 * - "00201007394856"   -> "+201007394856"
 * - "010 0739 4856"    -> "+201007394856"
 */
object PhoneSanitizer {

    private val EGYPTIAN_E164_REGEX = Regex("^\\+201[0125]\\d{8}$")

    fun sanitizeEgyptianPhoneNumber(input: String): String {
        if (input.isBlank()) return ""

        // 1. Remove all non-digit characters except leading '+'
        var cleaned = input.trim().replace(Regex("[^0-9+]"), "")

        // 2. Remove leading '+' or '00'
        if (cleaned.startsWith("+")) {
            cleaned = cleaned.substring(1)
        } else if (cleaned.startsWith("00")) {
            cleaned = cleaned.substring(2)
        }

        // 3. Strip country code '20' if present at start
        if (cleaned.startsWith("20")) {
            cleaned = cleaned.substring(2)
        }

        // 4. Strip any leading zeros (e.g. '01012345678' -> '1012345678')
        while (cleaned.startsWith("0")) {
            cleaned = cleaned.substring(1)
        }

        // 5. Prepend official Egyptian country code (+20)
        return "+20$cleaned"
    }

    fun isValidEgyptianPhoneNumber(phoneNumber: String): Boolean {
        val sanitized = sanitizeEgyptianPhoneNumber(phoneNumber)
        return sanitized.matches(EGYPTIAN_E164_REGEX)
    }
}
