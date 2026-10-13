package com.daleen.freelanceflow.domain.repository

import com.daleen.freelanceflow.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun observeAll(): Flow<List<Expense>>
    suspend fun upsert(expense: Expense)
    suspend fun delete(expense: Expense)
}
