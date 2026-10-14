package com.daleen.freelanceflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.daleen.freelanceflow.data.local.dao.ClientDao
import com.daleen.freelanceflow.data.local.dao.ExchangeRateDao
import com.daleen.freelanceflow.data.local.dao.ExpenseDao
import com.daleen.freelanceflow.data.local.dao.InvoiceDao
import com.daleen.freelanceflow.data.local.dao.InvoiceItemDao
import com.daleen.freelanceflow.data.local.dao.PaymentDao
import com.daleen.freelanceflow.data.local.dao.ProjectDao
import com.daleen.freelanceflow.data.local.dao.TaxProfileDao
import com.daleen.freelanceflow.data.local.dao.TimeEntryDao
import com.daleen.freelanceflow.data.local.dao.TombstoneDao
import com.daleen.freelanceflow.data.local.entity.ClientEntity
import com.daleen.freelanceflow.data.local.entity.ExchangeRateCacheEntity
import com.daleen.freelanceflow.data.local.entity.ExpenseEntity
import com.daleen.freelanceflow.data.local.entity.InvoiceEntity
import com.daleen.freelanceflow.data.local.entity.InvoiceItemEntity
import com.daleen.freelanceflow.data.local.entity.PaymentEntity
import com.daleen.freelanceflow.data.local.entity.ProjectEntity
import com.daleen.freelanceflow.data.local.entity.TaxProfileEntity
import com.daleen.freelanceflow.data.local.entity.TimeEntryEntity
import com.daleen.freelanceflow.data.local.entity.TombstoneEntity

@Database(
    entities = [
        ClientEntity::class,
        ProjectEntity::class,
        TimeEntryEntity::class,
        InvoiceEntity::class,
        InvoiceItemEntity::class,
        PaymentEntity::class,
        ExpenseEntity::class,
        TaxProfileEntity::class,
        ExchangeRateCacheEntity::class,
        TombstoneEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class FreelanceFlowDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun projectDao(): ProjectDao
    abstract fun timeEntryDao(): TimeEntryDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun invoiceItemDao(): InvoiceItemDao
    abstract fun paymentDao(): PaymentDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun taxProfileDao(): TaxProfileDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun tombstoneDao(): TombstoneDao

    companion object {
        const val DATABASE_NAME = "freelanceflow.db"
    }
}
