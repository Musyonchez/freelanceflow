package com.daleen.freelanceflow.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daleen.freelanceflow.domain.repository.BusinessProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val BUSINESS_NAME_KEY = stringPreferencesKey("business_name")
private val BASE_CURRENCY_KEY = stringPreferencesKey("base_currency")

class BusinessProfileRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : BusinessProfileRepository {

    override val businessName: Flow<String> =
        dataStore.data.map { it[BUSINESS_NAME_KEY].orEmpty() }

    override suspend fun setBusinessName(name: String) {
        dataStore.edit { it[BUSINESS_NAME_KEY] = name }
    }

    override val baseCurrency: Flow<String> =
        dataStore.data.map { it[BASE_CURRENCY_KEY] ?: "USD" }

    override suspend fun setBaseCurrency(currencyCode: String) {
        dataStore.edit { it[BASE_CURRENCY_KEY] = currencyCode }
    }
}
