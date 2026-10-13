package com.daleen.freelanceflow.domain.repository

import com.daleen.freelanceflow.domain.model.TimeEntry
import kotlinx.coroutines.flow.Flow

interface TimeEntryRepository {
    fun observeByProject(projectId: String): Flow<List<TimeEntry>>
    fun observeRunningEntry(): Flow<TimeEntry?>
    suspend fun getUnbilledByProject(projectId: String): List<TimeEntry>

    /** Stops any currently running entry (only one timer runs at a time), then starts a new one. */
    suspend fun start(projectId: String)
    suspend fun stop(entry: TimeEntry)
    suspend fun addManualEntry(entry: TimeEntry)
    suspend fun delete(entry: TimeEntry)
}
