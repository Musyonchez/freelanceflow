package com.daleen.freelanceflow.di

import android.content.Context
import androidx.room.Room
import com.daleen.freelanceflow.data.local.FreelanceFlowDatabase
import com.daleen.freelanceflow.data.local.dao.ClientDao
import com.daleen.freelanceflow.data.local.dao.ExchangeRateDao
import com.daleen.freelanceflow.data.local.dao.ExpenseDao
import com.daleen.freelanceflow.data.local.dao.InvoiceDao
import com.daleen.freelanceflow.data.local.dao.InvoiceItemDao
import com.daleen.freelanceflow.data.local.dao.PaymentDao
import com.daleen.freelanceflow.data.local.dao.ProjectDao
import com.daleen.freelanceflow.data.local.dao.TaxProfileDao
import com.daleen.freelanceflow.data.local.dao.TimeEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FreelanceFlowDatabase =
        Room.databaseBuilder(
            context,
            FreelanceFlowDatabase::class.java,
            FreelanceFlowDatabase.DATABASE_NAME,
        ).build()

    @Provides
    fun provideClientDao(db: FreelanceFlowDatabase): ClientDao = db.clientDao()

    @Provides
    fun provideProjectDao(db: FreelanceFlowDatabase): ProjectDao = db.projectDao()

    @Provides
    fun provideTimeEntryDao(db: FreelanceFlowDatabase): TimeEntryDao = db.timeEntryDao()

    @Provides
    fun provideInvoiceDao(db: FreelanceFlowDatabase): InvoiceDao = db.invoiceDao()

    @Provides
    fun provideInvoiceItemDao(db: FreelanceFlowDatabase): InvoiceItemDao = db.invoiceItemDao()

    @Provides
    fun providePaymentDao(db: FreelanceFlowDatabase): PaymentDao = db.paymentDao()

    @Provides
    fun provideExpenseDao(db: FreelanceFlowDatabase): ExpenseDao = db.expenseDao()

    @Provides
    fun provideTaxProfileDao(db: FreelanceFlowDatabase): TaxProfileDao = db.taxProfileDao()

    @Provides
    fun provideExchangeRateDao(db: FreelanceFlowDatabase): ExchangeRateDao = db.exchangeRateDao()
}
