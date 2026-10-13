package com.daleen.freelanceflow.domain.repository

import kotlinx.coroutines.flow.Flow

/** The freelancer's own details, shown as the issuer on generated invoice PDFs. */
interface BusinessProfileRepository {
    val businessName: Flow<String>
    suspend fun setBusinessName(name: String)
}
