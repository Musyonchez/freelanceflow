package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "projects",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [Index("clientId")],
)
data class ProjectEntity(
    @PrimaryKey val id: String,
    val clientId: String,
    val name: String,
    val description: String?,
    val rateType: RateType,
    // Monetary amounts are stored as integer minor units (e.g. cents) to avoid
    // floating-point rounding errors in invoice totals; currency code travels alongside.
    val rateValueMinor: Long,
    val currency: String,
    val status: ProjectStatus = ProjectStatus.ACTIVE,
    val createdAt: Long,
)
