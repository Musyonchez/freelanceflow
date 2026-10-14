package com.daleen.freelanceflow.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    val name: String,
    val company: String? = null,
    val email: String? = null,
    val phone: String? = null,
    @SerialName("default_currency") val defaultCurrency: String,
    val notes: String? = null,
    val status: String,
    @SerialName("created_at") val createdAt: Long,
)
