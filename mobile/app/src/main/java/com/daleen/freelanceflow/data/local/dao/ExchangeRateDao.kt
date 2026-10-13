package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daleen.freelanceflow.data.local.entity.ExchangeRateCacheEntity

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(rate: ExchangeRateCacheEntity)

    @Query(
        "SELECT * FROM exchange_rate_cache WHERE baseCode = :baseCode AND targetCode = :targetCode"
    )
    suspend fun getCached(baseCode: String, targetCode: String): ExchangeRateCacheEntity?
}
