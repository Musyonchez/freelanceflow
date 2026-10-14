package com.daleen.freelanceflow.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaxProfileDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    val label: String,
    @SerialName("deduction_rate_percent") val deductionRatePercent: Double,
    val notes: String? = null,
)
