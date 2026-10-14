package com.daleen.freelanceflow.data.repository

import com.daleen.freelanceflow.data.local.dao.ExpenseDao
import com.daleen.freelanceflow.data.local.dao.TombstoneDao
import com.daleen.freelanceflow.data.local.entity.ExpenseEntity
import com.daleen.freelanceflow.data.local.entity.TombstoneEntity
import com.daleen.freelanceflow.domain.model.Expense
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val tombstoneDao: TombstoneDao,
) : ExpenseRepository {

    override fun observeAll(): Flow<List<Expense>> =
        expenseDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(expense: Expense) = expenseDao.upsert(expense.toEntity())

    override suspend fun delete(expense: Expense) {
        expenseDao.delete(expense.toEntity())
        tombstoneDao.insert(TombstoneEntity("expenses", expense.id, System.currentTimeMillis()))
    }
}

private fun ExpenseEntity.toDomain() = Expense(
    id = id,
    clientId = clientId,
    projectId = projectId,
    amount = Money(amountMinor, currency),
    category = category,
    date = date,
    receiptPhotoUrl = receiptPhotoUrl,
    deductible = deductible,
    deductiblePercent = deductiblePercent,
)

private fun Expense.toEntity() = ExpenseEntity(
    id = id,
    clientId = clientId,
    projectId = projectId,
    amountMinor = amount.minorUnits,
    currency = amount.currencyCode,
    category = category,
    date = date,
    receiptPhotoUrl = receiptPhotoUrl,
    deductible = deductible,
    deductiblePercent = deductiblePercent,
)
