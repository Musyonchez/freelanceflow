package com.daleen.freelanceflow.domain.repository

import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.domain.model.ProjectStatus
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun observeAll(): Flow<List<Project>>
    fun observeByClient(clientId: String): Flow<List<Project>>
    fun observeByStatus(status: ProjectStatus): Flow<List<Project>>
    fun observeById(id: String): Flow<Project?>
    suspend fun getById(id: String): Project?
    suspend fun upsert(project: Project)
    suspend fun setStatus(id: String, status: ProjectStatus)
    suspend fun delete(project: Project)
}
