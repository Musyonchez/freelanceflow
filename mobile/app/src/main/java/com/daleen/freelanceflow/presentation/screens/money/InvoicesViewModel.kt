package com.daleen.freelanceflow.presentation.screens.money

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.Invoice
import com.daleen.freelanceflow.domain.model.InvoiceCreationResult
import com.daleen.freelanceflow.domain.model.InvoiceLineItemDraft
import com.daleen.freelanceflow.domain.model.InvoiceStatus
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.domain.model.ProjectStatus
import com.daleen.freelanceflow.domain.repository.BusinessProfileRepository
import com.daleen.freelanceflow.domain.repository.ClientRepository
import com.daleen.freelanceflow.domain.repository.InvoiceRepository
import com.daleen.freelanceflow.domain.repository.ProjectRepository
import com.daleen.freelanceflow.domain.repository.TimeEntryRepository
import com.daleen.freelanceflow.pdf.InvoicePdfGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToLong

private const val ONE_DAY_MILLIS = 86_400_000L

@HiltViewModel
class InvoicesViewModel @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val projectRepository: ProjectRepository,
    private val clientRepository: ClientRepository,
    private val timeEntryRepository: TimeEntryRepository,
    private val businessProfileRepository: BusinessProfileRepository,
    private val pdfGenerator: InvoicePdfGenerator,
) : ViewModel() {

    val invoices: StateFlow<List<Invoice>> = invoiceRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val businessName: StateFlow<String> = businessProfileRepository.businessName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    private val _shareEvent = MutableSharedFlow<Uri>(extraBufferCapacity = 1)
    val shareEvent: SharedFlow<Uri> = _shareEvent

    val clients: StateFlow<List<Client>> = clientRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val activeProjects: StateFlow<List<Project>> = projectRepository.observeByStatus(ProjectStatus.ACTIVE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _builderState = MutableStateFlow<InvoiceBuilderState?>(null)
    val builderState: StateFlow<InvoiceBuilderState?> = _builderState

    fun showBuilder() {
        _builderState.value = InvoiceBuilderState()
    }

    fun hideBuilder() {
        _builderState.value = null
    }

    fun selectProject(projectId: String) {
        val project = activeProjects.value.find { it.id == projectId } ?: return
        viewModelScope.launch {
            val unbilled = timeEntryRepository.getUnbilledByProject(projectId)
            val drafts = unbilled.map { entry ->
                InvoiceLineItemDraft(
                    description = entry.description?.takeIf { it.isNotBlank() } ?: project.name,
                    quantity = entry.durationMinutes / 60.0,
                    unitPrice = project.rate,
                    sourceTimeEntryId = entry.id,
                )
            }
            _builderState.update {
                it?.copy(projectId = projectId, unbilledItems = drafts, manualItems = emptyList())
            }
        }
    }

    fun updateManualDescription(value: String) {
        _builderState.update { it?.copy(manualDescription = value) }
    }

    fun updateManualQuantity(value: String) {
        _builderState.update { it?.copy(manualQuantity = value) }
    }

    fun updateManualUnitPrice(value: String) {
        _builderState.update { it?.copy(manualUnitPrice = value) }
    }

    fun updateNotes(value: String) {
        _builderState.update { it?.copy(notes = value) }
    }

    fun updateDueInDays(value: String) {
        _builderState.update { it?.copy(dueInDays = value) }
    }

    fun addManualItem() {
        val state = _builderState.value ?: return
        val project = activeProjects.value.find { it.id == state.projectId } ?: return
        val quantity = state.manualQuantity.toDoubleOrNull() ?: return
        val price = state.manualUnitPrice.toDoubleOrNull() ?: return
        val draft = InvoiceLineItemDraft(
            description = state.manualDescription.trim(),
            quantity = quantity,
            unitPrice = Money((price * 100).roundToLong(), project.rate.currencyCode),
        )
        _builderState.update {
            it?.copy(
                manualItems = it.manualItems + draft,
                manualDescription = "",
                manualQuantity = "",
                manualUnitPrice = "",
            )
        }
    }

    fun removeManualItem(draft: InvoiceLineItemDraft) {
        _builderState.update { it?.copy(manualItems = it.manualItems - draft) }
    }

    fun submit() {
        val state = _builderState.value ?: return
        val projectId = state.projectId ?: return
        val project = activeProjects.value.find { it.id == projectId } ?: return
        val client = clients.value.find { it.id == project.clientId } ?: return
        if (state.allItems.isEmpty()) {
            _builderState.update { it?.copy(error = "Add at least one line item.") }
            return
        }
        val dueDays = state.dueInDays.toIntOrNull() ?: 14

        viewModelScope.launch {
            _builderState.update { it?.copy(isSubmitting = true, error = null) }
            val dueDate = System.currentTimeMillis() + dueDays * ONE_DAY_MILLIS
            when (
                invoiceRepository.createInvoice(
                    clientId = client.id,
                    billingCurrency = client.defaultCurrency,
                    baseCurrency = project.rate.currencyCode,
                    items = state.allItems,
                    dueDate = dueDate,
                    notes = state.notes.trim().ifBlank { null },
                )
            ) {
                is InvoiceCreationResult.Success -> _builderState.value = null
                InvoiceCreationResult.NoItems ->
                    _builderState.update { it?.copy(isSubmitting = false, error = "Add at least one line item.") }
                InvoiceCreationResult.RateUnavailable ->
                    _builderState.update {
                        it?.copy(
                            isSubmitting = false,
                            error = "Couldn't resolve an exchange rate. Check your connection and try again.",
                        )
                    }
            }
        }
    }

    fun markSent(invoice: Invoice) {
        viewModelScope.launch { invoiceRepository.setStatus(invoice.id, InvoiceStatus.SENT) }
    }

    fun markPaid(invoice: Invoice) {
        viewModelScope.launch {
            invoiceRepository.recordPayment(invoice.id, invoice.total, System.currentTimeMillis(), null)
        }
    }

    fun setBusinessName(name: String) {
        viewModelScope.launch { businessProfileRepository.setBusinessName(name) }
    }

    fun shareInvoice(invoice: Invoice) {
        val client = clients.value.find { it.id == invoice.clientId } ?: return
        viewModelScope.launch {
            val items = invoiceRepository.observeItems(invoice.id).first()
            val name = businessProfileRepository.businessName.first()
            val file = pdfGenerator.generate(invoice, items, client, name)
            _shareEvent.emit(pdfGenerator.shareUriFor(file))
        }
    }
}
