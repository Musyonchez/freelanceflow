package com.daleen.freelanceflow.domain.repository

import com.daleen.freelanceflow.domain.model.TaxProfile
import kotlinx.coroutines.flow.Flow

interface TaxProfileRepository {
    fun observeProfile(): Flow<TaxProfile?>
    suspend fun upsert(profile: TaxProfile)
}
