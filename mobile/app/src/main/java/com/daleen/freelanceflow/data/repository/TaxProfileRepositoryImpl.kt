package com.daleen.freelanceflow.data.repository

import com.daleen.freelanceflow.data.local.dao.TaxProfileDao
import com.daleen.freelanceflow.data.local.entity.TaxProfileEntity
import com.daleen.freelanceflow.domain.model.TaxProfile
import com.daleen.freelanceflow.domain.repository.TaxProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaxProfileRepositoryImpl @Inject constructor(
    private val taxProfileDao: TaxProfileDao,
) : TaxProfileRepository {

    override fun observeProfile(): Flow<TaxProfile?> =
        taxProfileDao.observeActive().map { it?.toDomain() }

    override suspend fun upsert(profile: TaxProfile) = taxProfileDao.upsert(profile.toEntity())
}

private fun TaxProfileEntity.toDomain() = TaxProfile(
    id = id,
    label = label,
    deductionRatePercent = deductionRatePercent,
    notes = notes,
)

private fun TaxProfile.toEntity() = TaxProfileEntity(
    id = id,
    label = label,
    deductionRatePercent = deductionRatePercent,
    notes = notes,
)
