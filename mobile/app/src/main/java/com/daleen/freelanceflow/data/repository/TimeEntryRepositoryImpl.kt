package com.daleen.freelanceflow.data.repository

import com.daleen.freelanceflow.data.local.dao.TimeEntryDao
import com.daleen.freelanceflow.data.local.dao.TombstoneDao
import com.daleen.freelanceflow.data.local.entity.TimeEntryEntity
import com.daleen.freelanceflow.data.local.entity.TombstoneEntity
import com.daleen.freelanceflow.domain.model.TimeEntry
import com.daleen.freelanceflow.domain.repository.TimeEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToInt

class TimeEntryRepositoryImpl @Inject constructor(
    private val timeEntryDao: TimeEntryDao,
    private val tombstoneDao: TombstoneDao,
) : TimeEntryRepository {

    override fun observeByProject(projectId: String): Flow<List<TimeEntry>> =
        timeEntryDao.observeByProject(projectId).map { list -> list.map { it.toDomain() } }

    override fun observeRunningEntry(): Flow<TimeEntry?> =
        timeEntryDao.observeRunningEntry().map { it?.toDomain() }

    override suspend fun getUnbilledByProject(projectId: String): List<TimeEntry> =
        timeEntryDao.getUnbilledByProject(projectId).map { it.toDomain() }

    override suspend fun start(projectId: String) {
        timeEntryDao.getRunningEntry()?.let { running -> stopEntity(running) }
        timeEntryDao.upsert(
            TimeEntryEntity(
                id = UUID.randomUUID().toString(),
                projectId = projectId,
                startTime = System.currentTimeMillis(),
                endTime = null,
                durationMinutes = 0,
                description = null,
                billed = false,
            ),
        )
    }

    override suspend fun stop(entry: TimeEntry) = stopEntity(entry.toEntity())

    override suspend fun addManualEntry(entry: TimeEntry) = timeEntryDao.upsert(entry.toEntity())

    override suspend fun delete(entry: TimeEntry) {
        timeEntryDao.delete(entry.toEntity())
        tombstoneDao.insert(TombstoneEntity("time_entries", entry.id, System.currentTimeMillis()))
    }

    private suspend fun stopEntity(entity: TimeEntryEntity) {
        val now = System.currentTimeMillis()
        val start = entity.startTime ?: now
        timeEntryDao.update(
            entity.copy(
                endTime = now,
                durationMinutes = ((now - start) / 60_000.0).roundToInt(),
            ),
        )
    }
}

private fun TimeEntryEntity.toDomain() = TimeEntry(
    id = id,
    projectId = projectId,
    startTime = startTime,
    endTime = endTime,
    durationMinutes = durationMinutes,
    description = description,
    billed = billed,
)

private fun TimeEntry.toEntity() = TimeEntryEntity(
    id = id,
    projectId = projectId,
    startTime = startTime,
    endTime = endTime,
    durationMinutes = durationMinutes,
    description = description,
    billed = billed,
)
