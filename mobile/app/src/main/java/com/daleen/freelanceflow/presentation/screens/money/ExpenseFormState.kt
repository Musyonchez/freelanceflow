package com.daleen.freelanceflow.presentation.screens.money

data class ExpenseFormState(
    val id: String? = null,
    val amount: String = "",
    val currency: String = "USD",
    val category: String = "",
    val projectId: String? = null,
    val deductible: Boolean = false,
) {
    val isValid: Boolean
        get() = amount.toDoubleOrNull() != null && currency.isNotBlank() && category.isNotBlank()
}
