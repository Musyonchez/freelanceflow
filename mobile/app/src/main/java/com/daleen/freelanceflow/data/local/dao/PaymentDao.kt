package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daleen.freelanceflow.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE invoiceId = :invoiceId ORDER BY paidDate DESC")
    fun observeByInvoice(invoiceId: String): Flow<List<PaymentEntity>>
}
