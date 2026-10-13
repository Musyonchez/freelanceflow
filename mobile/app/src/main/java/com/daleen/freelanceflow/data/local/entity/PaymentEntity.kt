package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(
            entity = InvoiceEntity::class,
            parentColumns = ["id"],
            childColumns = ["invoiceId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("invoiceId")],
)
data class PaymentEntity(
    @PrimaryKey val id: String,
    val invoiceId: String,
    val amountMinor: Long,
    val currency: String,
    val paidDate: Long,
    val method: String?,
)
