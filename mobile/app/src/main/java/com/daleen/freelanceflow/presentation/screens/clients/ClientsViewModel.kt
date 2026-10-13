package com.daleen.freelanceflow.presentation.screens.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.ClientStatus
import com.daleen.freelanceflow.domain.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ClientsViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
) : ViewModel() {

    val clients: StateFlow<List<Client>> = clientRepository.observeByStatus(ClientStatus.ACTIVE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _formState = MutableStateFlow<ClientFormState?>(null)
    val formState: StateFlow<ClientFormState?> = _formState

    fun showAddForm() {
        _formState.value = ClientFormState()
    }

    fun showEditForm(client: Client) {
        _formState.value = ClientFormState(
            id = client.id,
            createdAt = client.createdAt,
            name = client.name,
            company = client.company.orEmpty(),
            email = client.email.orEmpty(),
            phone = client.phone.orEmpty(),
            defaultCurrency = client.defaultCurrency,
            notes = client.notes.orEmpty(),
        )
    }

    fun hideForm() {
        _formState.value = null
    }

    fun updateForm(transform: (ClientFormState) -> ClientFormState) {
        _formState.update { it?.let(transform) }
    }

    fun saveForm() {
        val form = _formState.value ?: return
        if (!form.isValid) return
        viewModelScope.launch {
            clientRepository.upsert(
                Client(
                    id = form.id ?: UUID.randomUUID().toString(),
                    name = form.name.trim(),
                    company = form.company.trim().ifBlank { null },
                    email = form.email.trim().ifBlank { null },
                    phone = form.phone.trim().ifBlank { null },
                    defaultCurrency = form.defaultCurrency.trim().uppercase(),
                    notes = form.notes.trim().ifBlank { null },
                    status = ClientStatus.ACTIVE,
                    createdAt = form.createdAt ?: System.currentTimeMillis(),
                ),
            )
            _formState.value = null
        }
    }

    fun archiveClient(client: Client) {
        viewModelScope.launch {
            clientRepository.archive(client.id)
            _formState.value = null
        }
    }
}
