package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daleen.freelanceflow.data.local.entity.ProjectEntity
import com.daleen.freelanceflow.data.local.entity.ProjectStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(project: ProjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(projects: List<ProjectEntity>)

    @Update
    suspend fun update(project: ProjectEntity)

    @Delete
    suspend fun delete(project: ProjectEntity)

    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects")
    suspend fun getAllOnce(): List<ProjectEntity>

    @Query("SELECT * FROM projects WHERE clientId = :clientId ORDER BY createdAt DESC")
    fun observeByClient(clientId: String): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE status = :status ORDER BY createdAt DESC")
    fun observeByStatus(status: ProjectStatus): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun observeById(id: String): Flow<ProjectEntity?>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getById(id: String): ProjectEntity?

    @Query("UPDATE projects SET status = :status WHERE id = :id")
    suspend fun setStatus(id: String, status: ProjectStatus)
}
