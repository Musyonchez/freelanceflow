package com.daleen.freelanceflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daleen.freelanceflow.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(expense: ExpenseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(expenses: List<ExpenseEntity>)

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun observeAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses")
    suspend fun getAllOnce(): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE projectId = :projectId ORDER BY date DESC")
    fun observeByProject(projectId: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE clientId = :clientId ORDER BY date DESC")
    fun observeByClient(clientId: String): Flow<List<ExpenseEntity>>

    @Query(
        "SELECT * FROM expenses WHERE date BETWEEN :fromInclusive AND :toInclusive ORDER BY date DESC"
    )
    fun observeByDateRange(fromInclusive: Long, toInclusive: Long): Flow<List<ExpenseEntity>>

    @Query(
        "SELECT * FROM expenses WHERE deductible = 1 AND date BETWEEN :fromInclusive AND :toInclusive"
    )
    suspend fun getDeductibleInRange(fromInclusive: Long, toInclusive: Long): List<ExpenseEntity>
}
