package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "invoice_items",
    foreignKeys = [
        ForeignKey(
            entity = InvoiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["invoiceId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = TimeEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["sourceTimeEntryId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("invoiceId"), Index("sourceTimeEntryId")],
)
data class InvoiceItemEntity(
    @PrimaryKey val id: String,
    val invoiceId: String,
    val description: String,
    val quantity: Double,
    val unitPriceMinor: Long,
    val lineTotalMinor: Long,
    val sourceTimeEntryId: String?,
)
