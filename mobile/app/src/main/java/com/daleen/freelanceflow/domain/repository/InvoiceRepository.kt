package com.daleen.freelanceflow.domain.repository

import com.daleen.freelanceflow.domain.model.Invoice
import com.daleen.freelanceflow.domain.model.InvoiceCreationResult
import com.daleen.freelanceflow.domain.model.InvoiceItem
import com.daleen.freelanceflow.domain.model.InvoiceLineItemDraft
import com.daleen.freelanceflow.domain.model.InvoiceStatus
import com.daleen.freelanceflow.domain.model.Money
import kotlinx.coroutines.flow.Flow

interface InvoiceRepository {
    fun observeAll(): Flow<List<Invoice>>
    fun observeByClient(clientId: String): Flow<List<Invoice>>
    fun observeById(id: String): Flow<Invoice?>
    fun observeItems(invoiceId: String): Flow<List<InvoiceItem>>

    /**
     * Builds and persists a new invoice from [items] (all assumed to share one currency, the
     * project/base currency), converting the total into [billingCurrency] via a resolved
     * exchange rate. Marks any referenced time entries as billed. Fails with
     * [InvoiceCreationResult.RateUnavailable] rather than guessing a rate.
     */
    suspend fun createInvoice(
        clientId: String,
        billingCurrency: String,
        baseCurrency: String,
        items: List<InvoiceLineItemDraft>,
        dueDate: Long,
        notes: String?,
    ): InvoiceCreationResult

    suspend fun setStatus(id: String, status: InvoiceStatus)
    suspend fun recordPayment(invoiceId: String, amount: Money, paidDate: Long, method: String?)
}
