package com.daleen.freelanceflow.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("invoice_id") val invoiceId: String,
    @SerialName("amount_minor") val amountMinor: Long,
    val currency: String,
    @SerialName("paid_date") val paidDate: Long,
    val method: String? = null,
)
