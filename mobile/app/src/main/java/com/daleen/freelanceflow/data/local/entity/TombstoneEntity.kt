package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity

/**
 * Records a hard local delete so the next sync can propagate it to Supabase
 * before pulling, instead of silently resurrecting the row from the server's
 * still-present copy. Only tables with a real delete() (not just an archive
 * status flip) need one: clients, projects, time_entries, expenses.
 */
@Entity(tableName = "tombstones", primaryKeys = ["tableName", "recordId"])
data class TombstoneEntity(
    val tableName: String,
    val recordId: String,
    val deletedAt: Long,
)
