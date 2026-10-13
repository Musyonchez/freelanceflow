package com.daleen.freelanceflow.domain.model

data class TimeEntry(
    val id: String,
    val projectId: String,
    val startTime: Long?,
    // Null endTime marks a timer still running.
    val endTime: Long?,
    val durationMinutes: Int,
    val description: String?,
    val billed: Boolean,
)
