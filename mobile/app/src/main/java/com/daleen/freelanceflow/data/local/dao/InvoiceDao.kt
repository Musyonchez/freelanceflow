package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daleen.freelanceflow.data.local.entity.InvoiceEntity
import com.daleen.freelanceflow.data.local.entity.InvoiceStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(invoice: InvoiceEntity)

    @Update
    suspend fun update(invoice: InvoiceEntity)

    @Delete
    suspend fun delete(invoice: InvoiceEntity)

    @Query("SELECT * FROM invoices ORDER BY issueDate DESC")
    fun observeAll(): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE clientId = :clientId ORDER BY issueDate DESC")
    fun observeByClient(clientId: String): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE status = :status ORDER BY dueDate ASC")
    fun observeByStatus(status: InvoiceStatus): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE id = :id")
    fun observeById(id: String): Flow<InvoiceEntity?>

    @Query("SELECT * FROM invoices WHERE id = :id")
    suspend fun getById(id: String): InvoiceEntity?

    @Query("SELECT COUNT(*) FROM invoices")
    suspend fun count(): Int

    @Query("UPDATE invoices SET status = :status WHERE id = :id")
    suspend fun setStatus(id: String, status: InvoiceStatus)

    @Query(
        "UPDATE invoices SET status = :overdue WHERE status = :sent AND dueDate < :now"
    )
    suspend fun flagOverdue(
        now: Long,
        sent: InvoiceStatus = InvoiceStatus.SENT,
        overdue: InvoiceStatus = InvoiceStatus.OVERDUE,
    )
}
