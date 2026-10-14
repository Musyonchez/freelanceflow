package com.daleen.freelanceflow.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("user_id") val userId: String,
    @SerialName("business_name") val businessName: String,
    @SerialName("base_currency") val baseCurrency: String,
)
