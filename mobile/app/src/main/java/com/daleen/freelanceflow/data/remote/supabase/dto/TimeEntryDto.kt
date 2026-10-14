package com.daleen.freelanceflow.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeEntryDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("project_id") val projectId: String,
    @SerialName("start_time") val startTime: Long? = null,
    @SerialName("end_time") val endTime: Long? = null,
    @SerialName("duration_minutes") val durationMinutes: Int,
    val description: String? = null,
    val billed: Boolean,
)
