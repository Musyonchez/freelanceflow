package com.daleen.freelanceflow.domain.model

data class Client(
    val id: String,
    val name: String,
    val company: String?,
    val email: String?,
    val phone: String?,
    val defaultCurrency: String,
    val notes: String?,
    val status: ClientStatus,
    val createdAt: Long,
)
