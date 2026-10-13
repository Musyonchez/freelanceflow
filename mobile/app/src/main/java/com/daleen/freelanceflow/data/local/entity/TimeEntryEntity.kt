package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "time_entries",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [Index("projectId")],
)
data class TimeEntryEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val startTime: Long?,
    // Null endTime marks a timer still running.
    val endTime: Long?,
    val durationMinutes: Int,
    val description: String?,
    val billed: Boolean = false,
)
