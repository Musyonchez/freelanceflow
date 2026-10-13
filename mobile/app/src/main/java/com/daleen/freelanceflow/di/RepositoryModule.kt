package com.daleen.freelanceflow.di

import com.daleen.freelanceflow.data.repository.ClientRepositoryImpl
import com.daleen.freelanceflow.data.repository.ProjectRepositoryImpl
import com.daleen.freelanceflow.domain.repository.ClientRepository
import com.daleen.freelanceflow.domain.repository.ProjectRepository
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
}
