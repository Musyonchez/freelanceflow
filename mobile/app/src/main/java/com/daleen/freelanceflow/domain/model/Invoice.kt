package com.daleen.freelanceflow.domain.model

data class Invoice(
    val id: String,
    val clientId: String,
    val invoiceNumber: String,
    val issueDate: Long,
    val dueDate: Long,
    val billingCurrency: String,
    val baseCurrency: String,
    // Snapshotted at creation so past invoices stay accurate as live rates change.
    val exchangeRate: Double,
    val exchangeRateTimestamp: Long,
    val subtotal: Money,
    val total: Money,
    val status: InvoiceStatus,
    val notes: String?,
)
