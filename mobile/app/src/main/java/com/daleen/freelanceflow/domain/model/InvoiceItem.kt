package com.daleen.freelanceflow.domain.model

data class InvoiceItem(
    val id: String,
    val invoiceId: String,
    val description: String,
    val quantity: Double,
    val unitPrice: Money,
    val lineTotal: Money,
    val sourceTimeEntryId: String?,
)
