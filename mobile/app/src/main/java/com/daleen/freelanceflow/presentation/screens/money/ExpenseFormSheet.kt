package com.daleen.freelanceflow.presentation.screens.money

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseFormSheet(
    state: ExpenseFormState,
    projects: List<Project>,
    clients: List<Client>,
    onFieldChange: ((ExpenseFormState) -> ExpenseFormState) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var projectMenuExpanded by remember { mutableStateOf(false) }
    val clientNameById = clients.associate { it.id to it.name }
    val selectedProject = projects.find { it.id == state.projectId }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("New expense", style = MaterialTheme.typography.titleLarge)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = state.amount,
                onValueChange = { value -> onFieldChange { it.copy(amount = value) } },
                label = { Text("Amount *") },
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

        OutlinedTextField(
            value = state.category,
            onValueChange = { value -> onFieldChange { it.copy(category = value) } },
            label = { Text("Category * (e.g. software, travel)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        ExposedDropdownMenuBox(
            expanded = projectMenuExpanded,
            onExpandedChange = { projectMenuExpanded = it },
        ) {
            OutlinedTextField(
                value = selectedProject?.let { "${it.name} — ${clientNameById[it.clientId]}" } ?: "None",
                onValueChange = {},
                readOnly = true,
                label = { Text("Project (optional)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = projectMenuExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(
                expanded = projectMenuExpanded,
                onDismissRequest = { projectMenuExpanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text("None") },
                    onClick = {
                        onFieldChange { it.copy(projectId = null) }
                        projectMenuExpanded = false
                    },
                )
                projects.forEach { project ->
                    DropdownMenuItem(
                        text = { Text("${project.name} — ${clientNameById[project.clientId]}") },
                        onClick = {
                            onFieldChange { it.copy(projectId = project.id) }
                            projectMenuExpanded = false
                        },
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Tax-deductible")
            Checkbox(
                checked = state.deductible,
                onCheckedChange = { value -> onFieldChange { it.copy(deductible = value) } },
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = onSave, enabled = state.isValid) {
                Text("Add expense")
            }
        }
    }
}
