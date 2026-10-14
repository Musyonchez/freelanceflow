package com.daleen.freelanceflow.domain.model

/** User-defined, not sourced from any tax authority — see FR-7.3: estimate, not a filing. */
data class TaxProfile(
    val id: String,
    val label: String,
    val deductionRatePercent: Double,
    val notes: String?,
)
