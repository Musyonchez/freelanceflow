package com.daleen.freelanceflow.presentation.screens.money

import com.daleen.freelanceflow.domain.model.InvoiceLineItemDraft

data class InvoiceBuilderState(
    val projectId: String? = null,
    val unbilledItems: List<InvoiceLineItemDraft> = emptyList(),
    val manualItems: List<InvoiceLineItemDraft> = emptyList(),
    val manualDescription: String = "",
    val manualQuantity: String = "",
    val manualUnitPrice: String = "",
    val dueInDays: String = "14",
    val notes: String = "",
    val isSubmitting: Boolean = false,
    val error: String? = null,
) {
    val allItems: List<InvoiceLineItemDraft> get() = unbilledItems + manualItems
    val canSubmit: Boolean get() = projectId != null && allItems.isNotEmpty() && !isSubmitting
    val canAddManualItem: Boolean
        get() = manualDescription.isNotBlank() &&
            manualQuantity.toDoubleOrNull() != null &&
            manualUnitPrice.toDoubleOrNull() != null
}
