package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("clientId"), Index("projectId")],
)
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val clientId: String?,
    val projectId: String?,
    val amountMinor: Long,
    val currency: String,
    val category: String,
    val date: Long,
    val receiptPhotoUrl: String?,
    val deductible: Boolean = false,
    val deductiblePercent: Double = 100.0,
)
