package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.daleen.freelanceflow.data.local.entity.ProjectEntity
import com.daleen.freelanceflow.data.local.entity.ProjectStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    // @Upsert, not @Insert(REPLACE): REPLACE deletes-then-reinserts the conflicting row,
    // which trips the RESTRICT foreign key from time_entries.projectId on every edit of a
    // project that already has a time entry. @Upsert compiles to a real UPDATE on conflict.
    @Upsert
    suspend fun upsert(project: ProjectEntity)

    @Upsert
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
