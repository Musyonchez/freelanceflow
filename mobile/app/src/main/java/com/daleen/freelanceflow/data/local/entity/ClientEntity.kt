package com.daleen.freelanceflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val company: String?,
    val email: String?,
    val phone: String?,
    val defaultCurrency: String,
    val notes: String?,
    val status: ClientStatus = ClientStatus.ACTIVE,
    val createdAt: Long,
)
