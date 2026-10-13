package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tax_profiles")
data class TaxProfileEntity(
    @PrimaryKey val id: String,
    val label: String,
    val deductionRatePercent: Double,
    val notes: String?,
)
