package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daleen.freelanceflow.data.local.entity.TimeEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: TimeEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entries: List<TimeEntryEntity>)

    @Update
    suspend fun update(entry: TimeEntryEntity)

    @Delete
    suspend fun delete(entry: TimeEntryEntity)

    @Query("SELECT * FROM time_entries WHERE projectId = :projectId ORDER BY startTime DESC")
    fun observeByProject(projectId: String): Flow<List<TimeEntryEntity>>

    @Query("SELECT * FROM time_entries")
    suspend fun getAllOnce(): List<TimeEntryEntity>

    @Query(
        "SELECT * FROM time_entries WHERE projectId = :projectId AND billed = 0 " +
            "AND endTime IS NOT NULL ORDER BY startTime ASC",
    )
    suspend fun getUnbilledByProject(projectId: String): List<TimeEntryEntity>

    @Query("SELECT * FROM time_entries WHERE endTime IS NULL LIMIT 1")
    fun observeRunningEntry(): Flow<TimeEntryEntity?>

    @Query("SELECT * FROM time_entries WHERE endTime IS NULL LIMIT 1")
    suspend fun getRunningEntry(): TimeEntryEntity?

    @Query("UPDATE time_entries SET billed = 1 WHERE id IN (:ids)")
    suspend fun markBilled(ids: List<String>)
}
