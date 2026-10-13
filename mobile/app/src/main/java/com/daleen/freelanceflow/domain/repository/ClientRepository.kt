package com.daleen.freelanceflow.domain.repository

import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.ClientStatus
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun observeByStatus(status: ClientStatus = ClientStatus.ACTIVE): Flow<List<Client>>
    fun observeAll(): Flow<List<Client>>
    fun observeById(id: String): Flow<Client?>
    suspend fun getById(id: String): Client?
    suspend fun upsert(client: Client)
    suspend fun archive(id: String)
    suspend fun restore(id: String)
    suspend fun delete(client: Client)
}
