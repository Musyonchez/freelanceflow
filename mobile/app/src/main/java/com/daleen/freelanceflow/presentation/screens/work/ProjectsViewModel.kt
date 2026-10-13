package com.daleen.freelanceflow.presentation.screens.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.ClientStatus
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.domain.model.ProjectStatus
import com.daleen.freelanceflow.domain.model.TimeEntry
import com.daleen.freelanceflow.domain.repository.ClientRepository
import com.daleen.freelanceflow.domain.repository.ProjectRepository
import com.daleen.freelanceflow.domain.repository.TimeEntryRepository
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
class ProjectsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val clientRepository: ClientRepository,
    private val timeEntryRepository: TimeEntryRepository,
) : ViewModel() {

    val projects: StateFlow<List<Project>> = projectRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val clients: StateFlow<List<Client>> = clientRepository.observeByStatus(ClientStatus.ACTIVE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val runningEntry: StateFlow<TimeEntry?> = timeEntryRepository.observeRunningEntry()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _formState = MutableStateFlow<ProjectFormState?>(null)
    val formState: StateFlow<ProjectFormState?> = _formState

    fun showAddForm() {
        val defaultClient = clients.value.firstOrNull()
        _formState.value = ProjectFormState(
            clientId = defaultClient?.id,
            currency = defaultClient?.defaultCurrency ?: "USD",
        )
    }

    fun showEditForm(project: Project) {
        val amount = project.rate.minorUnits / 100.0
        _formState.value = ProjectFormState(
            id = project.id,
            createdAt = project.createdAt,
            clientId = project.clientId,
            name = project.name,
            description = project.description.orEmpty(),
            rateType = project.rateType,
            rateAmount = if (amount == amount.toLong().toDouble()) {
                amount.toLong().toString()
            } else {
                amount.toString()
            },
            currency = project.rate.currencyCode,
            status = project.status,
        )
    }

    fun hideForm() {
        _formState.value = null
    }

    fun updateForm(transform: (ProjectFormState) -> ProjectFormState) {
        _formState.update { it?.let(transform) }
    }

    fun saveForm() {
        val form = _formState.value ?: return
        val clientId = form.clientId ?: return
        val amount = form.rateAmount.toDoubleOrNull() ?: return
        viewModelScope.launch {
            projectRepository.upsert(
                Project(
                    id = form.id ?: UUID.randomUUID().toString(),
                    clientId = clientId,
                    name = form.name.trim(),
                    description = form.description.trim().ifBlank { null },
                    rateType = form.rateType,
                    rate = Money((amount * 100).roundToLong(), form.currency.trim().uppercase()),
                    status = form.status,
                    createdAt = form.createdAt ?: System.currentTimeMillis(),
                ),
            )
            _formState.value = null
        }
    }

    fun setStatus(project: Project, status: ProjectStatus) {
        viewModelScope.launch {
            projectRepository.setStatus(project.id, status)
            _formState.value = null
        }
    }

    fun startTimer(projectId: String) {
        viewModelScope.launch { timeEntryRepository.start(projectId) }
    }

    fun stopTimer() {
        val entry = runningEntry.value ?: return
        viewModelScope.launch { timeEntryRepository.stop(entry) }
    }
}
