package com.daleen.freelanceflow.data.repository

import com.daleen.freelanceflow.data.local.dao.ClientDao
import com.daleen.freelanceflow.data.local.dao.TombstoneDao
import com.daleen.freelanceflow.data.local.entity.ClientEntity
import com.daleen.freelanceflow.data.local.entity.TombstoneEntity
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.ClientStatus
import com.daleen.freelanceflow.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.daleen.freelanceflow.data.local.entity.ClientStatus as EntityClientStatus

class ClientRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao,
    private val tombstoneDao: TombstoneDao,
) : ClientRepository {

    override fun observeByStatus(status: ClientStatus): Flow<List<Client>> =
        clientDao.observeByStatus(status.toEntity()).map { list -> list.map { it.toDomain() } }

    override fun observeAll(): Flow<List<Client>> =
        clientDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: String): Flow<Client?> =
        clientDao.observeById(id).map { it?.toDomain() }

    override suspend fun getById(id: String): Client? = clientDao.getById(id)?.toDomain()

    override suspend fun upsert(client: Client) = clientDao.upsert(client.toEntity())

    override suspend fun archive(id: String) = clientDao.setStatus(id, EntityClientStatus.ARCHIVED)

    override suspend fun restore(id: String) = clientDao.setStatus(id, EntityClientStatus.ACTIVE)

    override suspend fun delete(client: Client) {
        clientDao.delete(client.toEntity())
        tombstoneDao.insert(TombstoneEntity("clients", client.id, System.currentTimeMillis()))
    }
}

private fun ClientStatus.toEntity(): EntityClientStatus = EntityClientStatus.valueOf(name)

private fun EntityClientStatus.toDomain(): ClientStatus = ClientStatus.valueOf(name)

private fun ClientEntity.toDomain() = Client(
    id = id,
    name = name,
    company = company,
    email = email,
    phone = phone,
    defaultCurrency = defaultCurrency,
    notes = notes,
    status = status.toDomain(),
    createdAt = createdAt,
)

private fun Client.toEntity() = ClientEntity(
    id = id,
    name = name,
    company = company,
    email = email,
    phone = phone,
    defaultCurrency = defaultCurrency,
    notes = notes,
    status = status.toEntity(),
    createdAt = createdAt,
)
