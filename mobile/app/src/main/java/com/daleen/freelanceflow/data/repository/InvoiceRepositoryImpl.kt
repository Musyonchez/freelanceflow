package com.daleen.freelanceflow.data.repository

import com.daleen.freelanceflow.data.local.dao.InvoiceDao
import com.daleen.freelanceflow.data.local.dao.InvoiceItemDao
import com.daleen.freelanceflow.data.local.dao.PaymentDao
import com.daleen.freelanceflow.data.local.dao.TimeEntryDao
import com.daleen.freelanceflow.data.local.entity.InvoiceEntity
import com.daleen.freelanceflow.data.local.entity.InvoiceItemEntity
import com.daleen.freelanceflow.data.local.entity.PaymentEntity
import com.daleen.freelanceflow.domain.model.ExchangeRateOutcome
import com.daleen.freelanceflow.domain.model.Invoice
import com.daleen.freelanceflow.domain.model.InvoiceCreationResult
import com.daleen.freelanceflow.domain.model.InvoiceItem
import com.daleen.freelanceflow.domain.model.InvoiceLineItemDraft
import com.daleen.freelanceflow.domain.model.InvoiceStatus
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.repository.ExchangeRateRepository
import com.daleen.freelanceflow.domain.repository.InvoiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToLong
import com.daleen.freelanceflow.data.local.entity.InvoiceStatus as EntityInvoiceStatus

class InvoiceRepositoryImpl @Inject constructor(
    private val invoiceDao: InvoiceDao,
    private val invoiceItemDao: InvoiceItemDao,
    private val timeEntryDao: TimeEntryDao,
    private val paymentDao: PaymentDao,
    private val exchangeRateRepository: ExchangeRateRepository,
) : InvoiceRepository {

    override fun observeAll(): Flow<List<Invoice>> =
        invoiceDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeByClient(clientId: String): Flow<List<Invoice>> =
        invoiceDao.observeByClient(clientId).map { list -> list.map { it.toDomain() } }

    override fun observeById(id: String): Flow<Invoice?> =
        invoiceDao.observeById(id).map { it?.toDomain() }

    override fun observeItems(invoiceId: String): Flow<List<InvoiceItem>> =
        combine(
            invoiceItemDao.observeByInvoice(invoiceId),
            invoiceDao.observeById(invoiceId),
        ) { items, invoice ->
            val currency = invoice?.baseCurrency.orEmpty()
            items.map { it.toDomain(currency) }
        }

    override suspend fun createInvoice(
        clientId: String,
        billingCurrency: String,
        baseCurrency: String,
        items: List<InvoiceLineItemDraft>,
        dueDate: Long,
        notes: String?,
    ): InvoiceCreationResult {
        if (items.isEmpty()) return InvoiceCreationResult.NoItems

        val subtotalMinor = items.sumOf { it.lineTotal.minorUnits }

        val rateOutcome = exchangeRateRepository.getRate(baseCurrency, billingCurrency)
        val rate = (rateOutcome as? ExchangeRateOutcome.Available)
            ?: return InvoiceCreationResult.RateUnavailable

        val totalMinor = (subtotalMinor * rate.rate).roundToLong()
        val now = System.currentTimeMillis()
        val invoiceId = UUID.randomUUID().toString()

        val entity = InvoiceEntity(
            id = invoiceId,
            clientId = clientId,
            invoiceNumber = nextInvoiceNumber(),
            issueDate = now,
            dueDate = dueDate,
            billingCurrency = billingCurrency,
            baseCurrency = baseCurrency,
            exchangeRate = rate.rate,
            exchangeRateTimestamp = rate.fetchedAt,
            subtotalMinor = subtotalMinor,
            totalMinor = totalMinor,
            status = EntityInvoiceStatus.DRAFT,
            notes = notes,
        )
        invoiceDao.upsert(entity)

        invoiceItemDao.upsertAll(
            items.map { draft ->
                InvoiceItemEntity(
                    id = UUID.randomUUID().toString(),
                    invoiceId = invoiceId,
                    description = draft.description,
                    quantity = draft.quantity,
                    unitPriceMinor = draft.unitPrice.minorUnits,
                    lineTotalMinor = draft.lineTotal.minorUnits,
                    sourceTimeEntryId = draft.sourceTimeEntryId,
                )
            },
        )

        val billedTimeEntryIds = items.mapNotNull { it.sourceTimeEntryId }
        if (billedTimeEntryIds.isNotEmpty()) {
            timeEntryDao.markBilled(billedTimeEntryIds)
        }

        return InvoiceCreationResult.Success(entity.toDomain())
    }

    override suspend fun setStatus(id: String, status: InvoiceStatus) {
        invoiceDao.setStatus(id, status.toEntity())
    }

    override suspend fun recordPayment(invoiceId: String, amount: Money, paidDate: Long, method: String?) {
        paymentDao.upsert(
            PaymentEntity(
                id = UUID.randomUUID().toString(),
                invoiceId = invoiceId,
                amountMinor = amount.minorUnits,
                currency = amount.currencyCode,
                paidDate = paidDate,
                method = method,
            ),
        )
        invoiceDao.setStatus(invoiceId, EntityInvoiceStatus.PAID)
    }

    private suspend fun nextInvoiceNumber(): String {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val sequence = invoiceDao.count() + 1
        return "INV-$year-${sequence.toString().padStart(3, '0')}"
    }
}

private fun InvoiceStatus.toEntity(): EntityInvoiceStatus = EntityInvoiceStatus.valueOf(name)
private fun EntityInvoiceStatus.toDomain(): InvoiceStatus = InvoiceStatus.valueOf(name)

private fun InvoiceEntity.toDomain() = Invoice(
    id = id,
    clientId = clientId,
    invoiceNumber = invoiceNumber,
    issueDate = issueDate,
    dueDate = dueDate,
    billingCurrency = billingCurrency,
    baseCurrency = baseCurrency,
    exchangeRate = exchangeRate,
    exchangeRateTimestamp = exchangeRateTimestamp,
    subtotal = Money(subtotalMinor, billingCurrency),
    total = Money(totalMinor, billingCurrency),
    status = status.toDomain(),
    notes = notes,
)

private fun InvoiceItemEntity.toDomain(currencyCode: String) = InvoiceItem(
    id = id,
    invoiceId = invoiceId,
    description = description,
    quantity = quantity,
    unitPrice = Money(unitPriceMinor, currencyCode),
    lineTotal = Money(lineTotalMinor, currencyCode),
    sourceTimeEntryId = sourceTimeEntryId,
)
