package com.daleen.freelanceflow.domain.model

data class Project(
    val id: String,
    val clientId: String,
    val name: String,
    val description: String?,
    val rateType: RateType,
    val rate: Money,
    val status: ProjectStatus,
    val createdAt: Long,
)
