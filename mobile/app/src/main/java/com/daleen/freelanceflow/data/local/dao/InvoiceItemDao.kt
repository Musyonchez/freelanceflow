package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daleen.freelanceflow.data.local.entity.InvoiceItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<InvoiceItemEntity>)

    @Delete
    suspend fun delete(item: InvoiceItemEntity)

    @Query("SELECT * FROM invoice_items WHERE invoiceId = :invoiceId")
    fun observeByInvoice(invoiceId: String): Flow<List<InvoiceItemEntity>>

    @Query("SELECT * FROM invoice_items")
    suspend fun getAllOnce(): List<InvoiceItemEntity>
}
