package com.daleen.freelanceflow.data.remote.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InvoiceDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("invoice_number") val invoiceNumber: String,
    @SerialName("issue_date") val issueDate: Long,
    @SerialName("due_date") val dueDate: Long,
    @SerialName("billing_currency") val billingCurrency: String,
    @SerialName("base_currency") val baseCurrency: String,
    @SerialName("exchange_rate") val exchangeRate: Double,
    @SerialName("exchange_rate_timestamp") val exchangeRateTimestamp: Long,
    @SerialName("subtotal_minor") val subtotalMinor: Long,
    @SerialName("total_minor") val totalMinor: Long,
    val status: String,
    val notes: String? = null,
)
