package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity

// One row per currency pair (REPLACE on insert), so caching the latest rate is a plain upsert
// rather than needing to query for the most recent of several historical rows.
@Entity(tableName = "exchange_rate_cache", primaryKeys = ["baseCode", "targetCode"])
data class ExchangeRateCacheEntity(
    val baseCode: String,
    val targetCode: String,
    val rate: Double,
    val fetchedAt: Long,
)
