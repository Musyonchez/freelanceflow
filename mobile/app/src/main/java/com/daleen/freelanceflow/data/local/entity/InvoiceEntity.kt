package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "invoices",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [Index("clientId"), Index(value = ["invoiceNumber"], unique = true)],
)
data class InvoiceEntity(
    @PrimaryKey val id: String,
    val clientId: String,
    val invoiceNumber: String,
    val issueDate: Long,
    val dueDate: Long,
    val billingCurrency: String,
    val baseCurrency: String,
    // Snapshotted at creation so past invoices stay accurate as live rates change.
    val exchangeRate: Double,
    val exchangeRateTimestamp: Long,
    val subtotalMinor: Long,
    val totalMinor: Long,
    val status: InvoiceStatus = InvoiceStatus.DRAFT,
    val notes: String?,
)
