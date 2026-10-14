package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daleen.freelanceflow.data.local.entity.TaxProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaxProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: TaxProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(profiles: List<TaxProfileEntity>)

    @Update
    suspend fun update(profile: TaxProfileEntity)

    @Query("SELECT * FROM tax_profiles LIMIT 1")
    fun observeActive(): Flow<TaxProfileEntity?>

    @Query("SELECT * FROM tax_profiles")
    suspend fun getAllOnce(): List<TaxProfileEntity>
}
