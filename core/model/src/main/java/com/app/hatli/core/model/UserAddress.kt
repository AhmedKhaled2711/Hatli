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

    fun formattedDeliveryAddress(): String {
        val mainParts = mutableListOf<String>()
        if (city.isNotBlank()) mainParts.add(city.trim())
        if (area.isNotBlank()) mainParts.add(area.trim())
        if (street.isNotBlank()) mainParts.add(street.trim())
        if (building.isNotBlank()) mainParts.add(building.trim())

        val subParts = mutableListOf<String>()
        floor?.trim()?.takeIf { it.isNotEmpty() }?.let {
            val hasFloorWord = it.contains("دور") || it.contains("طابق") || it.contains("floor", ignoreCase = true)
            subParts.add(if (hasFloorWord) it else "طابق $it")
        }
        apartment?.trim()?.takeIf { it.isNotEmpty() }?.let {
            val hasAptWord = it.contains("شقة") || it.contains("apt", ignoreCase = true)
            subParts.add(if (hasAptWord) it else "شقة $it")
        }
        if (subParts.isNotEmpty()) {
            mainParts.add(subParts.joinToString("، "))
        }

        notes?.trim()?.takeIf { it.isNotEmpty() }?.let {
            mainParts.add("($it)")
        }

        return if (mainParts.isNotEmpty()) mainParts.joinToString(" - ") else formattedOneLine()
    }
}
