package com.daleen.freelanceflow.presentation.screens.work

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.ProjectStatus
import com.daleen.freelanceflow.domain.model.RateType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectFormSheet(
    state: ProjectFormState,
    clients: List<Client>,
    onFieldChange: ((ProjectFormState) -> ProjectFormState) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var clientMenuExpanded by remember { mutableStateOf(false) }
    val selectedClient = clients.find { it.id == state.clientId }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = if (state.isEditing) "Edit project" else "New project",
            style = MaterialTheme.typography.titleLarge,
        )

        ExposedDropdownMenuBox(
            expanded = clientMenuExpanded,
            onExpandedChange = { clientMenuExpanded = it },
        ) {
            OutlinedTextField(
                value = selectedClient?.name ?: "Select a client",
                onValueChange = {},
                readOnly = true,
                label = { Text("Client *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = clientMenuExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(
                expanded = clientMenuExpanded,
                onDismissRequest = { clientMenuExpanded = false },
            ) {
                clients.forEach { client ->
                    DropdownMenuItem(
                        text = { Text(client.name) },
                        onClick = {
                            onFieldChange {
                                it.copy(clientId = client.id, currency = client.defaultCurrency)
                            }
                            clientMenuExpanded = false
                        },
                    )
                }
            }
        }

        OutlinedTextField(
            value = state.name,
            onValueChange = { value -> onFieldChange { it.copy(name = value) } },
            label = { Text("Project name *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = state.description,
            onValueChange = { value -> onFieldChange { it.copy(description = value) } },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
        )

        Text("Rate type", style = MaterialTheme.typography.labelLarge)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            RateType.entries.forEachIndexed { index, type ->
                SegmentedButton(
                    selected = state.rateType == type,
                    onClick = { onFieldChange { it.copy(rateType = type) } },
                    shape = SegmentedButtonDefaults.itemShape(index, RateType.entries.size),
                ) {
                    Text(if (type == RateType.HOURLY) "Hourly" else "Fixed")
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = state.rateAmount,
                onValueChange = { value -> onFieldChange { it.copy(rateAmount = value) } },
                label = { Text(if (state.rateType == RateType.HOURLY) "Rate / hour *" else "Fixed price *") },
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
            OutlinedTextField(
                value = state.currency,
                onValueChange = { value -> onFieldChange { it.copy(currency = value.take(3)) } },
                label = { Text("Currency *") },
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
        }

        if (state.isEditing) {
            Text("Status", style = MaterialTheme.typography.labelLarge)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                ProjectStatus.entries.forEachIndexed { index, status ->
                    SegmentedButton(
                        selected = state.status == status,
                        onClick = { onFieldChange { it.copy(status = status) } },
                        shape = SegmentedButtonDefaults.itemShape(index, ProjectStatus.entries.size),
                    ) {
                        Text(status.name.lowercase().replaceFirstChar { it.uppercase() })
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = onSave, enabled = state.isValid) {
                Text(if (state.isEditing) "Save changes" else "Add project")
            }
        }
    }
}
