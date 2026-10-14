package com.daleen.freelanceflow.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("client_id") val clientId: String? = null,
    @SerialName("project_id") val projectId: String? = null,
    @SerialName("amount_minor") val amountMinor: Long,
    val currency: String,
    val category: String,
    val date: Long,
    @SerialName("receipt_photo_url") val receiptPhotoUrl: String? = null,
    val deductible: Boolean,
    @SerialName("deductible_percent") val deductiblePercent: Double,
)
