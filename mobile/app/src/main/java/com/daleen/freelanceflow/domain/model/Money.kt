package com.daleen.freelanceflow.domain.model

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/** Stored as integer minor units (e.g. cents) to avoid floating-point rounding in totals. */
data class Money(val minorUnits: Long, val currencyCode: String) {
    fun format(): String =
        try {
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            formatter.currency = Currency.getInstance(currencyCode)
            formatter.format(minorUnits / 100.0)
        } catch (e: IllegalArgumentException) {
            // Unknown/placeholder currency code: fall back to a plain, unambiguous rendering.
            val major = minorUnits / 100.0
            "%s %.2f".format(currencyCode, major)
        }

    operator fun plus(other: Money): Money {
        require(currencyCode == other.currencyCode) {
            "Cannot add Money of different currencies: $currencyCode vs ${other.currencyCode}"
        }
        return Money(minorUnits + other.minorUnits, currencyCode)
    }

    companion object {
        fun zero(currencyCode: String) = Money(0, currencyCode)
    }
}
