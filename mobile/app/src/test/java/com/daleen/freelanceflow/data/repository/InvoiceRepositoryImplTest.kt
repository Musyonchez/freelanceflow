package com.daleen.freelanceflow.data.repository

import com.daleen.freelanceflow.data.local.dao.InvoiceDao
import com.daleen.freelanceflow.data.local.dao.InvoiceItemDao
import com.daleen.freelanceflow.data.local.dao.PaymentDao
import com.daleen.freelanceflow.data.local.dao.TimeEntryDao
import com.daleen.freelanceflow.data.local.entity.InvoiceEntity
import com.daleen.freelanceflow.data.local.entity.InvoiceItemEntity
import com.daleen.freelanceflow.data.local.entity.InvoiceStatus as EntityInvoiceStatus
import com.daleen.freelanceflow.data.local.entity.PaymentEntity
import com.daleen.freelanceflow.domain.model.ExchangeRateOutcome
import com.daleen.freelanceflow.domain.model.InvoiceCreationResult
import com.daleen.freelanceflow.domain.model.InvoiceLineItemDraft
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.repository.ExchangeRateRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import java.util.Calendar
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class InvoiceRepositoryImplTest {

    private val invoiceDao = mockk<InvoiceDao>()
    private val invoiceItemDao = mockk<InvoiceItemDao>()
    private val timeEntryDao = mockk<TimeEntryDao>()
    private val paymentDao = mockk<PaymentDao>()
    private val exchangeRateRepository = mockk<ExchangeRateRepository>()

    private lateinit var repository: InvoiceRepositoryImpl

    @Before
    fun setUp() {
        repository = InvoiceRepositoryImpl(invoiceDao, invoiceItemDao, timeEntryDao, paymentDao, exchangeRateRepository)
        coEvery { invoiceDao.upsert(any()) } just Runs
        coEvery { invoiceItemDao.upsertAll(any()) } just Runs
        coEvery { timeEntryDao.markBilled(any()) } just Runs
        coEvery { invoiceDao.count() } returns 0
    }

    @Test
    fun `createInvoice fails fast with NoItems when there is nothing to bill`() = runTest {
        val result = repository.createInvoice(
            clientId = "client-1",
            billingCurrency = "USD",
            baseCurrency = "USD",
            items = emptyList(),
            dueDate = 0L,
            notes = null,
        )

        assertEquals(InvoiceCreationResult.NoItems, result)
    }

    @Test
    fun `createInvoice fails with RateUnavailable rather than fabricating a conversion`() = runTest {
        coEvery { exchangeRateRepository.getRate("USD", "KES") } returns ExchangeRateOutcome.Unavailable

        val result = repository.createInvoice(
            clientId = "client-1",
            billingCurrency = "KES",
            baseCurrency = "USD",
            items = listOf(oneHourAt(Money(5_000, "USD"))),
            dueDate = 0L,
            notes = null,
        )

        assertEquals(InvoiceCreationResult.RateUnavailable, result)
    }

    @Test
    fun `createInvoice converts the base-currency subtotal into the billing currency at the resolved rate`() = runTest {
        // 1 USD = 130 KES: subtotal of $125.00 should become KES 16,250.00, not the reverse.
        coEvery { exchangeRateRepository.getRate("USD", "KES") } returns
            ExchangeRateOutcome.Available(rate = 130.0, fetchedAt = 1_000L, isCached = false)

        val items = listOf(
            InvoiceLineItemDraft(description = "Design", quantity = 2.0, unitPrice = Money(5_000, "USD")),
            InvoiceLineItemDraft(description = "Copywriting", quantity = 1.0, unitPrice = Money(2_500, "USD")),
        )

        val result = repository.createInvoice(
            clientId = "client-1",
            billingCurrency = "KES",
            baseCurrency = "USD",
            items = items,
            dueDate = 0L,
            notes = null,
        ) as InvoiceCreationResult.Success

        assertEquals(12_500L, result.invoice.subtotal.minorUnits)
        assertEquals("USD", result.invoice.subtotal.currencyCode)
        assertEquals(1_625_000L, result.invoice.total.minorUnits)
        assertEquals("KES", result.invoice.total.currencyCode)
    }

    @Test
    fun `createInvoice numbers invoices sequentially by existing count for the current year`() = runTest {
        coEvery { exchangeRateRepository.getRate("USD", "USD") } returns
            ExchangeRateOutcome.Available(rate = 1.0, fetchedAt = 0L, isCached = false)
        coEvery { invoiceDao.count() } returns 4

        val result = repository.createInvoice(
            clientId = "client-1",
            billingCurrency = "USD",
            baseCurrency = "USD",
            items = listOf(oneHourAt(Money(1_000, "USD"))),
            dueDate = 0L,
            notes = null,
        ) as InvoiceCreationResult.Success

        val year = Calendar.getInstance().get(Calendar.YEAR)
        assertEquals("INV-$year-005", result.invoice.invoiceNumber)
    }

    @Test
    fun `createInvoice marks only the time entries actually billed on this invoice`() = runTest {
        coEvery { exchangeRateRepository.getRate("USD", "USD") } returns
            ExchangeRateOutcome.Available(rate = 1.0, fetchedAt = 0L, isCached = false)

        val items = listOf(
            InvoiceLineItemDraft(
                description = "Logged time",
                quantity = 1.0,
                unitPrice = Money(1_000, "USD"),
                sourceTimeEntryId = "te-1",
            ),
            InvoiceLineItemDraft(description = "Manual line", quantity = 1.0, unitPrice = Money(500, "USD")),
        )

        repository.createInvoice(
            clientId = "client-1",
            billingCurrency = "USD",
            baseCurrency = "USD",
            items = items,
            dueDate = 0L,
            notes = null,
        )

        coVerify(exactly = 1) { timeEntryDao.markBilled(listOf("te-1")) }
    }

    @Test
    fun `createInvoice never touches time entries when no item came from one`() = runTest {
        coEvery { exchangeRateRepository.getRate("USD", "USD") } returns
            ExchangeRateOutcome.Available(rate = 1.0, fetchedAt = 0L, isCached = false)

        repository.createInvoice(
            clientId = "client-1",
            billingCurrency = "USD",
            baseCurrency = "USD",
            items = listOf(oneHourAt(Money(1_000, "USD"))),
            dueDate = 0L,
            notes = null,
        )

        coVerify(exactly = 0) { timeEntryDao.markBilled(any()) }
    }

    @Test
    fun `recordPayment persists the payment and marks the invoice paid`() = runTest {
        val paymentSlot = slot<PaymentEntity>()
        coEvery { paymentDao.upsert(capture(paymentSlot)) } just Runs
        coEvery { invoiceDao.setStatus("invoice-1", EntityInvoiceStatus.PAID) } just Runs

        repository.recordPayment(
            invoiceId = "invoice-1",
            amount = Money(5_000, "USD"),
            paidDate = 12_345L,
            method = "Bank transfer",
        )

        assertEquals("invoice-1", paymentSlot.captured.invoiceId)
        assertEquals(5_000L, paymentSlot.captured.amountMinor)
        assertEquals("USD", paymentSlot.captured.currency)
        assertEquals(12_345L, paymentSlot.captured.paidDate)
        assertEquals("Bank transfer", paymentSlot.captured.method)
        coVerify(exactly = 1) { invoiceDao.setStatus("invoice-1", EntityInvoiceStatus.PAID) }
    }

    private fun oneHourAt(rate: Money) =
        InvoiceLineItemDraft(description = "Logged time", quantity = 1.0, unitPrice = rate)
}
