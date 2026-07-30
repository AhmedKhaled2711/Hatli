package com.app.hatli.core.model

/**
 * Delivery / residential address collected during profile setup.
 *
 * [latitude] / [longitude] are reserved for a future Current Location / Maps integration.
 * Phase-now: manual entry only; coordinates stay null.
 */
data class UserAddress(
    val city: String,
    val area: String,
    val street: String,
    val building: String,
    val floor: String? = null,
    val apartment: String? = null,
    val notes: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
) {
    val isEmpty: Boolean
        get() = city.isBlank() && area.isBlank() && street.isBlank() && building.isBlank()

    fun formattedOneLine(): String = listOf(building, street, area, city)
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .joinToString(", ")
}
