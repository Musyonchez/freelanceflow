package com.daleen.freelanceflow.presentation.screens.money

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daleen.freelanceflow.data.receipts.ReceiptStore
import com.daleen.freelanceflow.domain.model.Expense
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.domain.model.ProjectStatus
import com.daleen.freelanceflow.domain.repository.ExpenseRepository
import com.daleen.freelanceflow.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlin.math.roundToLong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val receiptStore: ReceiptStore,
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

    fun cameraUriForCurrentForm(): Uri? =
        _formState.value?.let { receiptStore.newCameraUri(it.id) }

    fun onPhotoCaptured(success: Boolean) {
        val form = _formState.value ?: return
        if (!success) return
        _formState.update { it?.copy(receiptFile = receiptStore.localFile(form.id)) }
    }

    fun onPhotoPicked(uri: Uri) {
        val form = _formState.value ?: return
        viewModelScope.launch {
            val file = receiptStore.importFromGallery(form.id, uri)
            _formState.update { it?.copy(receiptFile = file) }
        }
    }

    fun removePhoto() {
        val form = _formState.value ?: return
        receiptStore.deleteLocalCopy(form.id)
        _formState.update { it?.copy(receiptFile = null) }
    }

    fun saveForm() {
        val form = _formState.value ?: return
        val amount = form.amount.toDoubleOrNull() ?: return
        val project = activeProjects.value.find { it.id == form.projectId }
        viewModelScope.launch {
            expenseRepository.upsert(
                Expense(
                    id = form.id,
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
        viewModelScope.launch {
            expenseRepository.delete(expense)
            receiptStore.deleteLocalCopy(expense.id)
            expense.receiptPhotoUrl?.let { storagePath ->
                runCatching { receiptStore.deleteRemote(storagePath) }
            }
        }
    }

    fun localReceiptFile(expense: Expense): File? =
        receiptStore.localFile(expense.id).takeIf { it.exists() }

    suspend fun downloadReceipt(expense: Expense): File? {
        val storagePath = expense.receiptPhotoUrl ?: return null
        return receiptStore.ensureLocalCopy(storagePath, expense.id)
    }
}
