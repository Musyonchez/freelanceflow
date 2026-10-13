package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daleen.freelanceflow.data.local.entity.ClientEntity
import com.daleen.freelanceflow.data.local.entity.ClientStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(client: ClientEntity)

    @Update
    suspend fun update(client: ClientEntity)

    @Delete
    suspend fun delete(client: ClientEntity)

    @Query("SELECT * FROM clients WHERE status = :status ORDER BY name ASC")
    fun observeByStatus(status: ClientStatus = ClientStatus.ACTIVE): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun observeAll(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE id = :id")
    fun observeById(id: String): Flow<ClientEntity?>

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getById(id: String): ClientEntity?

    @Query("UPDATE clients SET status = :status WHERE id = :id")
    suspend fun setStatus(id: String, status: ClientStatus)
}
