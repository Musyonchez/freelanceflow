package com.daleen.freelanceflow.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InvoiceItemDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("invoice_id") val invoiceId: String,
    val description: String,
    val quantity: Double,
    @SerialName("unit_price_minor") val unitPriceMinor: Long,
    @SerialName("line_total_minor") val lineTotalMinor: Long,
    @SerialName("source_time_entry_id") val sourceTimeEntryId: String? = null,
)
