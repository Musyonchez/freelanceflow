package com.daleen.freelanceflow.presentation.screens.money

import java.io.File
import java.util.UUID

data class ExpenseFormState(
    val id: String = UUID.randomUUID().toString(),
    val amount: String = "",
    val currency: String = "USD",
    val category: String = "",
    val projectId: String? = null,
    val deductible: Boolean = false,
    val receiptFile: File? = null,
) {
    val isValid: Boolean
        get() = amount.toDoubleOrNull() != null && currency.isNotBlank() && category.isNotBlank()
}
