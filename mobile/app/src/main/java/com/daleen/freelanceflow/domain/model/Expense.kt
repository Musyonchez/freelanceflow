package com.daleen.freelanceflow.domain.model

data class Expense(
    val id: String,
    val clientId: String?,
    val projectId: String?,
    val amount: Money,
    val category: String,
    val date: Long,
    val receiptPhotoUrl: String?,
    val deductible: Boolean,
    val deductiblePercent: Double,
)
