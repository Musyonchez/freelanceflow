package com.daleen.freelanceflow.domain.repository

import kotlinx.coroutines.flow.Flow

/** The freelancer's own settings: issuer details for invoice PDFs, and reporting base currency. */
interface BusinessProfileRepository {
    val businessName: Flow<String>
    suspend fun setBusinessName(name: String)

    val baseCurrency: Flow<String>
    suspend fun setBaseCurrency(currencyCode: String)
}
