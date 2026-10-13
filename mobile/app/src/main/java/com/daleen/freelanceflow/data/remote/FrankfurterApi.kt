package com.daleen.freelanceflow.data.remote

import com.daleen.freelanceflow.data.remote.dto.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

/** Frankfurter (frankfurter.app): ECB-sourced rates, no API key required. */
interface FrankfurterApi {
    @GET("latest")
    suspend fun getLatestRate(
        @Query("from") base: String,
        @Query("to") target: String,
    ): ExchangeRateResponse

    companion object {
        const val BASE_URL = "https://api.frankfurter.app/"
    }
}
