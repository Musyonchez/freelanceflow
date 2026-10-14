package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daleen.freelanceflow.data.local.entity.TombstoneEntity

@Dao
interface TombstoneDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tombstone: TombstoneEntity)

    @Query("SELECT * FROM tombstones WHERE tableName = :tableName")
    suspend fun getAllOnce(tableName: String): List<TombstoneEntity>

    @Query("DELETE FROM tombstones WHERE tableName = :tableName AND recordId IN (:recordIds)")
    suspend fun clear(tableName: String, recordIds: List<String>)
}
