package com.daleen.freelanceflow.di

import com.daleen.freelanceflow.data.repository.AuthRepositoryImpl
import com.daleen.freelanceflow.data.repository.BusinessProfileRepositoryImpl
import com.daleen.freelanceflow.data.repository.ClientRepositoryImpl
import com.daleen.freelanceflow.data.repository.ExchangeRateRepositoryImpl
import com.daleen.freelanceflow.data.repository.ExpenseRepositoryImpl
import com.daleen.freelanceflow.data.repository.InvoiceRepositoryImpl
import com.daleen.freelanceflow.data.repository.ProjectRepositoryImpl
import com.daleen.freelanceflow.data.repository.SyncRepositoryImpl
import com.daleen.freelanceflow.data.repository.TaxProfileRepositoryImpl
import com.daleen.freelanceflow.data.repository.TimeEntryRepositoryImpl
import com.daleen.freelanceflow.domain.repository.AuthRepository
import com.daleen.freelanceflow.domain.repository.BusinessProfileRepository
import com.daleen.freelanceflow.domain.repository.ClientRepository
import com.daleen.freelanceflow.domain.repository.ExchangeRateRepository
import com.daleen.freelanceflow.domain.repository.ExpenseRepository
import com.daleen.freelanceflow.domain.repository.InvoiceRepository
import com.daleen.freelanceflow.domain.repository.ProjectRepository
import com.daleen.freelanceflow.domain.repository.SyncRepository
import com.daleen.freelanceflow.domain.repository.TaxProfileRepository
import com.daleen.freelanceflow.domain.repository.TimeEntryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindClientRepository(impl: ClientRepositoryImpl): ClientRepository

    @Binds
    @Singleton
    abstract fun bindProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository

    @Binds
    @Singleton
    abstract fun bindTimeEntryRepository(impl: TimeEntryRepositoryImpl): TimeEntryRepository

    @Binds
    @Singleton
    abstract fun bindExchangeRateRepository(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    @Binds
    @Singleton
    abstract fun bindInvoiceRepository(impl: InvoiceRepositoryImpl): InvoiceRepository

    @Binds
    @Singleton
    abstract fun bindBusinessProfileRepository(impl: BusinessProfileRepositoryImpl): BusinessProfileRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(impl: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindTaxProfileRepository(impl: TaxProfileRepositoryImpl): TaxProfileRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSyncRepository(impl: SyncRepositoryImpl): SyncRepository
}
