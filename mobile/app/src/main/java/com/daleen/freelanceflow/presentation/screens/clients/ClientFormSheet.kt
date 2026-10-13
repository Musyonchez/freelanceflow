package com.daleen.freelanceflow.presentation.screens.clients

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ClientFormSheet(
    state: ClientFormState,
    onFieldChange: ((ClientFormState) -> ClientFormState) -> Unit,
    onSave: () -> Unit,
    onArchive: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = if (state.isEditing) "Edit client" else "New client",
            style = MaterialTheme.typography.titleLarge,
        )

        OutlinedTextField(
            value = state.name,
            onValueChange = { value -> onFieldChange { it.copy(name = value) } },
            label = { Text("Name *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.company,
            onValueChange = { value -> onFieldChange { it.copy(company = value) } },
            label = { Text("Company") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = { value -> onFieldChange { it.copy(email = value) } },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.phone,
            onValueChange = { value -> onFieldChange { it.copy(phone = value) } },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.defaultCurrency,
            onValueChange = { value -> onFieldChange { it.copy(defaultCurrency = value.take(3)) } },
            label = { Text("Billing currency (ISO code, e.g. USD) *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.notes,
            onValueChange = { value -> onFieldChange { it.copy(notes = value) } },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            if (state.isEditing) {
                TextButton(onClick = onArchive) {
                    Text("Archive")
                }
            }
            Button(onClick = onSave, enabled = state.isValid) {
                Text(if (state.isEditing) "Save changes" else "Add client")
            }
        }
    }
}
