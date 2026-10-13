package com.daleen.freelanceflow.domain.model

/** A line item proposed for a new invoice, before it has an invoice to belong to. */
data class InvoiceLineItemDraft(
    val description: String,
    val quantity: Double,
    val unitPrice: Money,
    val sourceTimeEntryId: String? = null,
) {
    val lineTotal: Money
        get() = Money((quantity * unitPrice.minorUnits).toLong(), unitPrice.currencyCode)
}

sealed interface InvoiceCreationResult {
    data class Success(val invoice: Invoice) : InvoiceCreationResult
    data object RateUnavailable : InvoiceCreationResult
    data object NoItems : InvoiceCreationResult
}
