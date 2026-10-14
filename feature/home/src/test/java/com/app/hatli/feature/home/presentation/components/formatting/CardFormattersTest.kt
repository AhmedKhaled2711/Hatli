package com.app.hatli.feature.home.presentation.components.formatting

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CardFormattersTest {

    @Test
    fun `formatRoute in Arabic points leftwards arrow towards dropoff destination`() {
        val pickup = "حي الأشجار"
        val dropoff = "الميدان القديم"

        val result = formatRoute(
            pickupLabel = pickup,
            dropoffLabel = dropoff,
            isAr = true,
        )

        assertThat(result).isNotNull()
        assertThat(result).contains("←")
        assertThat(result).isEqualTo("📍 $pickup \u200F←\u200F 🏁 $dropoff")
    }

    @Test
    fun `formatRoute in English points rightwards arrow towards dropoff destination`() {
        val pickup = "Store A"
        val dropoff = "Building B"

        val result = formatRoute(
            pickupLabel = pickup,
            dropoffLabel = dropoff,
            isAr = false,
        )

        assertThat(result).isNotNull()
        assertThat(result).contains("➔")
        assertThat(result).isEqualTo("📍 $pickup ➔ 🏁 $dropoff")
    }

    @Test
    fun `formatRoute with pickup only returns pickup`() {
        val result = formatRoute(
            pickupLabel = "Supermarket",
            dropoffLabel = null,
            isAr = true,
        )

        assertThat(result).isEqualTo("📍 Supermarket")
    }

    @Test
    fun `formatRoute with dropoff only returns dropoff`() {
        val result = formatRoute(
            pickupLabel = null,
            dropoffLabel = "Home Address",
            isAr = true,
        )

        assertThat(result).isEqualTo("🏁 Home Address")
    }

    @Test
    fun `formatRoute with description fallback when labels are empty`() {
        val result = formatRoute(
            pickupLabel = "",
            dropoffLabel = "",
            description = "Custom errand order",
            isAr = true,
        )

        assertThat(result).isEqualTo("Custom errand order")
    }

    @Test
    fun `formatRoute returns null when all fields are empty`() {
        val result = formatRoute(
            pickupLabel = null,
            dropoffLabel = null,
            description = null,
            isAr = true,
        )

        assertThat(result).isNull()
    }
}
