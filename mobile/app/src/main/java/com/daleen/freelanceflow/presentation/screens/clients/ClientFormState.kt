package com.daleen.freelanceflow.presentation.screens.clients

data class ClientFormState(
    val id: String? = null,
    val createdAt: Long? = null,
    val name: String = "",
    val company: String = "",
    val email: String = "",
    val phone: String = "",
    val defaultCurrency: String = "USD",
    val notes: String = "",
) {
    val isEditing: Boolean get() = id != null
    val isValid: Boolean get() = name.isNotBlank() && defaultCurrency.isNotBlank()
}
