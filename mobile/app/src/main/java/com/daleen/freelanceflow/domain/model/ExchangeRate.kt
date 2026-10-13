package com.daleen.freelanceflow.domain.model

/** Result of resolving a currency pair's rate: either a usable rate (live or cached) or none at all. */
sealed interface ExchangeRateOutcome {
    data class Available(val rate: Double, val fetchedAt: Long, val isCached: Boolean) : ExchangeRateOutcome
    data object Unavailable : ExchangeRateOutcome
}
