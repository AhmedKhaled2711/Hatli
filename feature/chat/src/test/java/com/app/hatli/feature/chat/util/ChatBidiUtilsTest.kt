package com.app.hatli.feature.chat.util

import androidx.compose.ui.unit.LayoutDirection
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatBidiUtilsTest {

    @Test
    fun `empty text falls back to default layout direction`() {
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("", LayoutDirection.Ltr))
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("", LayoutDirection.Rtl))
    }

    @Test
    fun `arabic text resolves to RTL`() {
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("مرحبا بك", LayoutDirection.Ltr))
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("أنا في طريقي إليك", LayoutDirection.Ltr))
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("تمام يا غالي", LayoutDirection.Ltr))
    }

    @Test
    fun `english text resolves to LTR`() {
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("Hello there", LayoutDirection.Rtl))
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("I am on the way", LayoutDirection.Rtl))
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("OK", LayoutDirection.Rtl))
    }

    @Test
    fun `text starting with emoji resolves by first strong character`() {
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("🛒 أنا في المحل", LayoutDirection.Ltr))
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("🛒 I am at the store", LayoutDirection.Rtl))
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("📍 وصلت تحت البيت", LayoutDirection.Ltr))
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("👍 Done", LayoutDirection.Rtl))
    }

    @Test
    fun `numbers and symbols only fall back to default direction`() {
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("12345", LayoutDirection.Ltr))
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("12345", LayoutDirection.Rtl))
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("!@#$%", LayoutDirection.Ltr))
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("!@#$%", LayoutDirection.Rtl))
    }

    @Test
    fun `text with leading numbers or symbols resolves to first strong character`() {
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("123. مرحبا", LayoutDirection.Ltr))
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("123. Hello", LayoutDirection.Rtl))
        assertEquals(LayoutDirection.Rtl, resolveBidiLayoutDirection("? كيف حالك", LayoutDirection.Ltr))
        assertEquals(LayoutDirection.Ltr, resolveBidiLayoutDirection("? How are you", LayoutDirection.Rtl))
    }
}
