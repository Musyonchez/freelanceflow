package com.daleen.freelanceflow.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class MoneyTest {

    @Test
    fun `plus adds minor units for matching currencies`() {
        val total = Money(1_000, "USD") + Money(250, "USD")

        assertEquals(1_250, total.minorUnits)
        assertEquals("USD", total.currencyCode)
    }

    @Test
    fun `plus rejects mismatched currencies rather than silently combining them`() {
        assertThrows(IllegalArgumentException::class.java) {
            Money(1_000, "USD") + Money(1_000, "KES")
        }
    }

    @Test
    fun `zero creates a zero-value Money in the given currency`() {
        val zero = Money.zero("EUR")

        assertEquals(0, zero.minorUnits)
        assertEquals("EUR", zero.currencyCode)
    }

    @Test
    fun `format renders minor units as major units with two decimal places`() {
        val formatted = Money(10_000, "USD").format()

        assertTrue("expected major-unit value '100.00' in '$formatted'", formatted.contains("100.00"))
    }

    @Test
    fun `format rounds down sub-cent minor units correctly in major-unit display`() {
        // 1 minor unit is sub-cent noise once divided to major units; it must not get
        // silently rounded up to the next cent on an invoice-facing display.
        val formatted = Money(1_001, "USD").format()

        assertTrue("expected '10.01' in '$formatted'", formatted.contains("10.01"))
    }

    @Test
    fun `format falls back to a plain unambiguous rendering for an unknown currency code`() {
        val formatted = Money(500, "ZZZ").format()

        assertEquals("ZZZ 5.00", formatted)
    }
}
