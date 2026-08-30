package com.example

import com.example.data.model.UserProfileSettings
import com.example.ui.components.HangoutVibesCatalog
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `hangout vibes catalog contains expected categories and items`() {
        assertTrue(HangoutVibesCatalog.CLASSICS.contains("☕ Coffee"))
        assertTrue(HangoutVibesCatalog.CLASSICS.contains("🌿 Walk in the park"))
        assertTrue(HangoutVibesCatalog.CLASSICS.contains("🎲 Game night"))
        assertTrue(HangoutVibesCatalog.CATEGORIES.isNotEmpty())
    }

    @Test
    fun `user profile settings stores and updates hangout vibes correctly`() {
        val defaultSettings = UserProfileSettings()
        assertNotNull(defaultSettings.hangoutVibes)
        assertTrue(defaultSettings.hangoutVibes.isNotEmpty())

        val updated = defaultSettings.copy(
            hangoutVibes = listOf("☕ Coffee", "🌿 Walk in the park", "🎲 Game night")
        )
        assertEquals(3, updated.hangoutVibes.size)
        assertTrue(updated.hangoutVibes.contains("☕ Coffee"))
    }
}
