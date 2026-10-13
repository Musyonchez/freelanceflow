package com.daleen.freelanceflow.presentation.screens.money

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daleen.freelanceflow.domain.model.Expense
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.domain.model.ProjectStatus
import com.daleen.freelanceflow.domain.repository.ExpenseRepository
import com.daleen.freelanceflow.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToLong

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    projectRepository: ProjectRepository,
) : ViewModel() {

    val expenses: StateFlow<List<Expense>> = expenseRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val activeProjects: StateFlow<List<Project>> = projectRepository.observeByStatus(ProjectStatus.ACTIVE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _formState = MutableStateFlow<ExpenseFormState?>(null)
    val formState: StateFlow<ExpenseFormState?> = _formState

    fun showAddForm() {
        _formState.value = ExpenseFormState()
    }

    fun hideForm() {
        _formState.value = null
    }

    fun updateForm(transform: (ExpenseFormState) -> ExpenseFormState) {
        _formState.update { it?.let(transform) }
    }

    fun saveForm() {
        val form = _formState.value ?: return
        val amount = form.amount.toDoubleOrNull() ?: return
        val project = activeProjects.value.find { it.id == form.projectId }
        viewModelScope.launch {
            expenseRepository.upsert(
                Expense(
                    id = UUID.randomUUID().toString(),
                    clientId = project?.clientId,
                    projectId = project?.id,
                    amount = Money((amount * 100).roundToLong(), form.currency.trim().uppercase()),
                    category = form.category.trim(),
                    date = System.currentTimeMillis(),
                    receiptPhotoUrl = null,
                    deductible = form.deductible,
                    deductiblePercent = 100.0,
                ),
            )
            _formState.value = null
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { expenseRepository.delete(expense) }
    }
}
