package com.daleen.freelanceflow.data.repository

import com.daleen.freelanceflow.data.local.dao.ExchangeRateDao
import com.daleen.freelanceflow.data.local.entity.ExchangeRateCacheEntity
import com.daleen.freelanceflow.data.remote.FrankfurterApi
import com.daleen.freelanceflow.domain.model.ExchangeRateOutcome
import com.daleen.freelanceflow.domain.repository.ExchangeRateRepository
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: FrankfurterApi,
    private val dao: ExchangeRateDao,
) : ExchangeRateRepository {

    override suspend fun getRate(base: String, target: String): ExchangeRateOutcome {
        if (base == target) {
            return ExchangeRateOutcome.Available(rate = 1.0, fetchedAt = System.currentTimeMillis(), isCached = false)
        }

        val live = runCatching { api.getLatestRate(base, target) }.getOrNull()
        val liveRate = live?.rates?.get(target)
        if (liveRate != null) {
            val fetchedAt = System.currentTimeMillis()
            dao.upsert(ExchangeRateCacheEntity(base, target, liveRate, fetchedAt))
            return ExchangeRateOutcome.Available(rate = liveRate, fetchedAt = fetchedAt, isCached = false)
        }

        val cached = dao.getCached(base, target)
        return if (cached != null) {
            ExchangeRateOutcome.Available(rate = cached.rate, fetchedAt = cached.fetchedAt, isCached = true)
        } else {
            ExchangeRateOutcome.Unavailable
        }
    }
}
