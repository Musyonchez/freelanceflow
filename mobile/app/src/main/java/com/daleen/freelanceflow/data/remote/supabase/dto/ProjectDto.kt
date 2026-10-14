package com.daleen.freelanceflow.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("client_id") val clientId: String,
    val name: String,
    val description: String? = null,
    @SerialName("rate_type") val rateType: String,
    @SerialName("rate_value_minor") val rateValueMinor: Long,
    val currency: String,
    val status: String,
    @SerialName("created_at") val createdAt: Long,
)
