package com.daleen.freelanceflow.data.repository

import android.util.Log
import com.daleen.freelanceflow.data.local.dao.ClientDao
import com.daleen.freelanceflow.data.local.dao.ExpenseDao
import com.daleen.freelanceflow.data.local.dao.InvoiceDao
import com.daleen.freelanceflow.data.local.dao.InvoiceItemDao
import com.daleen.freelanceflow.data.local.dao.PaymentDao
import com.daleen.freelanceflow.data.local.dao.ProjectDao
import com.daleen.freelanceflow.data.local.dao.TaxProfileDao
import com.daleen.freelanceflow.data.local.dao.TimeEntryDao
import com.daleen.freelanceflow.data.local.dao.TombstoneDao
import com.daleen.freelanceflow.data.local.entity.ClientEntity
import com.daleen.freelanceflow.data.local.entity.ClientStatus
import com.daleen.freelanceflow.data.local.entity.ExpenseEntity
import com.daleen.freelanceflow.data.local.entity.InvoiceEntity
import com.daleen.freelanceflow.data.local.entity.InvoiceItemEntity
import com.daleen.freelanceflow.data.local.entity.InvoiceStatus
import com.daleen.freelanceflow.data.local.entity.PaymentEntity
import com.daleen.freelanceflow.data.local.entity.ProjectEntity
import com.daleen.freelanceflow.data.local.entity.ProjectStatus
import com.daleen.freelanceflow.data.local.entity.RateType
import com.daleen.freelanceflow.data.local.entity.TaxProfileEntity
import com.daleen.freelanceflow.data.local.entity.TimeEntryEntity
import com.daleen.freelanceflow.data.remote.supabase.dto.ClientDto
import com.daleen.freelanceflow.data.remote.supabase.dto.ExpenseDto
import com.daleen.freelanceflow.data.remote.supabase.dto.InvoiceDto
import com.daleen.freelanceflow.data.remote.supabase.dto.InvoiceItemDto
import com.daleen.freelanceflow.data.remote.supabase.dto.PaymentDto
import com.daleen.freelanceflow.data.remote.supabase.dto.ProfileDto
import com.daleen.freelanceflow.data.remote.supabase.dto.ProjectDto
import com.daleen.freelanceflow.data.remote.supabase.dto.TaxProfileDto
import com.daleen.freelanceflow.data.remote.supabase.dto.TimeEntryDto
import com.daleen.freelanceflow.domain.repository.BusinessProfileRepository
import com.daleen.freelanceflow.domain.repository.SyncRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import kotlinx.coroutines.flow.first

private const val TAG = "SyncRepository"

class SyncRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val clientDao: ClientDao,
    private val projectDao: ProjectDao,
    private val timeEntryDao: TimeEntryDao,
    private val invoiceDao: InvoiceDao,
    private val invoiceItemDao: InvoiceItemDao,
    private val paymentDao: PaymentDao,
    private val expenseDao: ExpenseDao,
    private val taxProfileDao: TaxProfileDao,
    private val businessProfileRepository: BusinessProfileRepository,
    private val tombstoneDao: TombstoneDao,
) : SyncRepository {

    override suspend fun syncAll(): Result<Unit> = runCatching {
        val userId = supabaseClient.auth.currentUserOrNull()?.id ?: return@runCatching

        syncClients(userId)
        syncProjects(userId)
        syncTimeEntries(userId)
        syncInvoices(userId)
        syncInvoiceItems(userId)
        syncPayments(userId)
        syncExpenses(userId)
        syncTaxProfiles(userId)
        syncProfile(userId)
    }

    private suspend fun syncClients(userId: String) = safely("clients") {
        val local = clientDao.getAllOnce()
        if (local.isNotEmpty()) {
            supabaseClient.postgrest.from("clients").upsert(local.map { it.toDto(userId) })
        }
        pushDeletions("clients")
        val remote = supabaseClient.postgrest.from("clients")
            .select { filter { ClientDto::userId eq userId } }
            .decodeList<ClientDto>()
        clientDao.upsertAll(remote.map { it.toEntity() })
    }

    private suspend fun syncProjects(userId: String) = safely("projects") {
        val local = projectDao.getAllOnce()
        if (local.isNotEmpty()) {
            supabaseClient.postgrest.from("projects").upsert(local.map { it.toDto(userId) })
        }
        pushDeletions("projects")
        val remote = supabaseClient.postgrest.from("projects")
            .select { filter { ProjectDto::userId eq userId } }
            .decodeList<ProjectDto>()
        projectDao.upsertAll(remote.map { it.toEntity() })
    }

    private suspend fun syncTimeEntries(userId: String) = safely("time_entries") {
        val local = timeEntryDao.getAllOnce()
        if (local.isNotEmpty()) {
            supabaseClient.postgrest.from("time_entries").upsert(local.map { it.toDto(userId) })
        }
        pushDeletions("time_entries")
        val remote = supabaseClient.postgrest.from("time_entries")
            .select { filter { TimeEntryDto::userId eq userId } }
            .decodeList<TimeEntryDto>()
        timeEntryDao.upsertAll(remote.map { it.toEntity() })
    }

    private suspend fun syncInvoices(userId: String) = safely("invoices") {
        val local = invoiceDao.getAllOnce()
        if (local.isNotEmpty()) {
            supabaseClient.postgrest.from("invoices").upsert(local.map { it.toDto(userId) })
        }
        val remote = supabaseClient.postgrest.from("invoices")
            .select { filter { InvoiceDto::userId eq userId } }
            .decodeList<InvoiceDto>()
        invoiceDao.upsertAll(remote.map { it.toEntity() })
    }

    private suspend fun syncInvoiceItems(userId: String) = safely("invoice_items") {
        val local = invoiceItemDao.getAllOnce()
        if (local.isNotEmpty()) {
            supabaseClient.postgrest.from("invoice_items").upsert(local.map { it.toDto(userId) })
        }
        val remote = supabaseClient.postgrest.from("invoice_items")
            .select { filter { InvoiceItemDto::userId eq userId } }
            .decodeList<InvoiceItemDto>()
        invoiceItemDao.upsertAll(remote.map { it.toEntity() })
    }

    private suspend fun syncPayments(userId: String) = safely("payments") {
        val local = paymentDao.getAllOnce()
        if (local.isNotEmpty()) {
            supabaseClient.postgrest.from("payments").upsert(local.map { it.toDto(userId) })
        }
        val remote = supabaseClient.postgrest.from("payments")
            .select { filter { PaymentDto::userId eq userId } }
            .decodeList<PaymentDto>()
        paymentDao.upsertAll(remote.map { it.toEntity() })
    }

    private suspend fun syncExpenses(userId: String) = safely("expenses") {
        val local = expenseDao.getAllOnce()
        if (local.isNotEmpty()) {
            supabaseClient.postgrest.from("expenses").upsert(local.map { it.toDto(userId) })
        }
        pushDeletions("expenses")
        val remote = supabaseClient.postgrest.from("expenses")
            .select { filter { ExpenseDto::userId eq userId } }
            .decodeList<ExpenseDto>()
        expenseDao.upsertAll(remote.map { it.toEntity() })
    }

    private suspend fun syncTaxProfiles(userId: String) = safely("tax_profiles") {
        val local = taxProfileDao.getAllOnce()
        if (local.isNotEmpty()) {
            supabaseClient.postgrest.from("tax_profiles").upsert(local.map { it.toDto(userId) })
        }
        val remote = supabaseClient.postgrest.from("tax_profiles")
            .select { filter { TaxProfileDto::userId eq userId } }
            .decodeList<TaxProfileDto>()
        taxProfileDao.upsertAll(remote.map { it.toEntity() })
    }

    private suspend fun syncProfile(userId: String) = safely("profiles") {
        val businessName = businessProfileRepository.businessName.first()
        val baseCurrency = businessProfileRepository.baseCurrency.first()
        supabaseClient.postgrest.from("profiles").upsert(
            ProfileDto(userId = userId, businessName = businessName, baseCurrency = baseCurrency),
        )
        val remote = supabaseClient.postgrest.from("profiles")
            .select { filter { ProfileDto::userId eq userId } }
            .decodeList<ProfileDto>()
            .firstOrNull()
        if (remote != null) {
            if (remote.businessName != businessName) businessProfileRepository.setBusinessName(remote.businessName)
            if (remote.baseCurrency != baseCurrency) businessProfileRepository.setBaseCurrency(remote.baseCurrency)
        }
    }

    /**
     * Deletes rows tombstoned locally (hard-deleted since the last sync) from
     * [table] on the server, then clears those tombstones. Must run before
     * the pull step for [table], otherwise the pull would re-fetch the
     * still-present remote row and resurrect it locally.
     */
    private suspend fun pushDeletions(table: String) {
        val tombstones = tombstoneDao.getAllOnce(table)
        if (tombstones.isEmpty()) return
        val ids = tombstones.map { it.recordId }
        supabaseClient.postgrest.from(table).delete { filter { isIn("id", ids) } }
        tombstoneDao.clear(table, ids)
    }

    private inline fun safely(table: String, block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            Log.w(TAG, "Sync failed for table '$table', will retry next run", e)
        }
    }
}

private fun ClientEntity.toDto(userId: String) = ClientDto(
    id = id,
    userId = userId,
    name = name,
    company = company,
    email = email,
    phone = phone,
    defaultCurrency = defaultCurrency,
    notes = notes,
    status = status.name,
    createdAt = createdAt,
)

private fun ClientDto.toEntity() = ClientEntity(
    id = id,
    name = name,
    company = company,
    email = email,
    phone = phone,
    defaultCurrency = defaultCurrency,
    notes = notes,
    status = ClientStatus.valueOf(status),
    createdAt = createdAt,
)

private fun ProjectEntity.toDto(userId: String) = ProjectDto(
    id = id,
    userId = userId,
    clientId = clientId,
    name = name,
    description = description,
    rateType = rateType.name,
    rateValueMinor = rateValueMinor,
    currency = currency,
    status = status.name,
    createdAt = createdAt,
)

private fun ProjectDto.toEntity() = ProjectEntity(
    id = id,
    clientId = clientId,
    name = name,
    description = description,
    rateType = RateType.valueOf(rateType),
    rateValueMinor = rateValueMinor,
    currency = currency,
    status = ProjectStatus.valueOf(status),
    createdAt = createdAt,
)

private fun TimeEntryEntity.toDto(userId: String) = TimeEntryDto(
    id = id,
    userId = userId,
    projectId = projectId,
    startTime = startTime,
    endTime = endTime,
    durationMinutes = durationMinutes,
    description = description,
    billed = billed,
)

private fun TimeEntryDto.toEntity() = TimeEntryEntity(
    id = id,
    projectId = projectId,
    startTime = startTime,
    endTime = endTime,
    durationMinutes = durationMinutes,
    description = description,
    billed = billed,
)

private fun InvoiceEntity.toDto(userId: String) = InvoiceDto(
    id = id,
    userId = userId,
    clientId = clientId,
    invoiceNumber = invoiceNumber,
    issueDate = issueDate,
    dueDate = dueDate,
    billingCurrency = billingCurrency,
    baseCurrency = baseCurrency,
    exchangeRate = exchangeRate,
    exchangeRateTimestamp = exchangeRateTimestamp,
    subtotalMinor = subtotalMinor,
    totalMinor = totalMinor,
    status = status.name,
    notes = notes,
)

private fun InvoiceDto.toEntity() = InvoiceEntity(
    id = id,
    clientId = clientId,
    invoiceNumber = invoiceNumber,
    issueDate = issueDate,
    dueDate = dueDate,
    billingCurrency = billingCurrency,
    baseCurrency = baseCurrency,
    exchangeRate = exchangeRate,
    exchangeRateTimestamp = exchangeRateTimestamp,
    subtotalMinor = subtotalMinor,
    totalMinor = totalMinor,
    status = InvoiceStatus.valueOf(status),
    notes = notes,
)

private fun InvoiceItemEntity.toDto(userId: String) = InvoiceItemDto(
    id = id,
    userId = userId,
    invoiceId = invoiceId,
    description = description,
    quantity = quantity,
    unitPriceMinor = unitPriceMinor,
    lineTotalMinor = lineTotalMinor,
    sourceTimeEntryId = sourceTimeEntryId,
)

private fun InvoiceItemDto.toEntity() = InvoiceItemEntity(
    id = id,
    invoiceId = invoiceId,
    description = description,
    quantity = quantity,
    unitPriceMinor = unitPriceMinor,
    lineTotalMinor = lineTotalMinor,
    sourceTimeEntryId = sourceTimeEntryId,
)

private fun PaymentEntity.toDto(userId: String) = PaymentDto(
    id = id,
    userId = userId,
    invoiceId = invoiceId,
    amountMinor = amountMinor,
    currency = currency,
    paidDate = paidDate,
    method = method,
)

private fun PaymentDto.toEntity() = PaymentEntity(
    id = id,
    invoiceId = invoiceId,
    amountMinor = amountMinor,
    currency = currency,
    paidDate = paidDate,
    method = method,
)

private fun ExpenseEntity.toDto(userId: String) = ExpenseDto(
    id = id,
    userId = userId,
    clientId = clientId,
    projectId = projectId,
    amountMinor = amountMinor,
    currency = currency,
    category = category,
    date = date,
    receiptPhotoUrl = receiptPhotoUrl,
    deductible = deductible,
    deductiblePercent = deductiblePercent,
)

private fun ExpenseDto.toEntity() = ExpenseEntity(
    id = id,
    clientId = clientId,
    projectId = projectId,
    amountMinor = amountMinor,
    currency = currency,
    category = category,
    date = date,
    receiptPhotoUrl = receiptPhotoUrl,
    deductible = deductible,
    deductiblePercent = deductiblePercent,
)

private fun TaxProfileEntity.toDto(userId: String) = TaxProfileDto(
    id = id,
    userId = userId,
    label = label,
    deductionRatePercent = deductionRatePercent,
    notes = notes,
)

private fun TaxProfileDto.toEntity() = TaxProfileEntity(
    id = id,
    label = label,
    deductionRatePercent = deductionRatePercent,
    notes = notes,
)
