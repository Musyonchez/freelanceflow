package com.daleen.freelanceflow.data.repository

import com.daleen.freelanceflow.data.local.dao.ProjectDao
import com.daleen.freelanceflow.data.local.dao.TombstoneDao
import com.daleen.freelanceflow.data.local.entity.ProjectEntity
import com.daleen.freelanceflow.data.local.entity.TombstoneEntity
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.domain.model.ProjectStatus
import com.daleen.freelanceflow.domain.model.RateType
import com.daleen.freelanceflow.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.daleen.freelanceflow.data.local.entity.ProjectStatus as EntityProjectStatus
import com.daleen.freelanceflow.data.local.entity.RateType as EntityRateType

class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
    private val tombstoneDao: TombstoneDao,
) : ProjectRepository {

    override fun observeAll(): Flow<List<Project>> =
        projectDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeByClient(clientId: String): Flow<List<Project>> =
        projectDao.observeByClient(clientId).map { list -> list.map { it.toDomain() } }

    override fun observeByStatus(status: ProjectStatus): Flow<List<Project>> =
        projectDao.observeByStatus(status.toEntity()).map { list -> list.map { it.toDomain() } }

    override fun observeById(id: String): Flow<Project?> =
        projectDao.observeById(id).map { it?.toDomain() }

    override suspend fun getById(id: String): Project? = projectDao.getById(id)?.toDomain()

    override suspend fun upsert(project: Project) = projectDao.upsert(project.toEntity())

    override suspend fun setStatus(id: String, status: ProjectStatus) =
        projectDao.setStatus(id, status.toEntity())

    override suspend fun delete(project: Project) {
        projectDao.delete(project.toEntity())
        tombstoneDao.insert(TombstoneEntity("projects", project.id, System.currentTimeMillis()))
    }
}

private fun ProjectStatus.toEntity(): EntityProjectStatus = EntityProjectStatus.valueOf(name)
private fun EntityProjectStatus.toDomain(): ProjectStatus = ProjectStatus.valueOf(name)
private fun RateType.toEntity(): EntityRateType = EntityRateType.valueOf(name)
private fun EntityRateType.toDomain(): RateType = RateType.valueOf(name)

private fun ProjectEntity.toDomain() = Project(
    id = id,
    clientId = clientId,
    name = name,
    description = description,
    rateType = rateType.toDomain(),
    rate = Money(rateValueMinor, currency),
    status = status.toDomain(),
    createdAt = createdAt,
)

private fun Project.toEntity() = ProjectEntity(
    id = id,
    clientId = clientId,
    name = name,
    description = description,
    rateType = rateType.toEntity(),
    rateValueMinor = rate.minorUnits,
    currency = rate.currencyCode,
    status = status.toEntity(),
    createdAt = createdAt,
)
