package com.daleen.freelanceflow.domain.repository

import com.daleen.freelanceflow.domain.model.ExchangeRateOutcome

interface ExchangeRateRepository {
    /**
     * Resolves the rate to convert 1 unit of [base] into [target]. Tries a live fetch first;
     * on failure (offline, rate-limited, etc.) falls back to the last cached rate for that pair,
     * however old. Returns [ExchangeRateOutcome.Unavailable] only when neither is possible.
     */
    suspend fun getRate(base: String, target: String): ExchangeRateOutcome
}
