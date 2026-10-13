package com.daleen.freelanceflow.presentation.screens.money

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.domain.model.InvoiceLineItemDraft
import com.daleen.freelanceflow.domain.model.Money
import com.daleen.freelanceflow.domain.model.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceBuilderSheet(
    state: InvoiceBuilderState,
    projects: List<Project>,
    clients: List<Client>,
    onSelectProject: (String) -> Unit,
    onManualDescriptionChange: (String) -> Unit,
    onManualQuantityChange: (String) -> Unit,
    onManualUnitPriceChange: (String) -> Unit,
    onAddManualItem: () -> Unit,
    onRemoveManualItem: (InvoiceLineItemDraft) -> Unit,
    onNotesChange: (String) -> Unit,
    onDueInDaysChange: (String) -> Unit,
    onSubmit: () -> Unit,
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
        Text("New invoice", style = MaterialTheme.typography.titleLarge)

        ExposedDropdownMenuBox(
            expanded = projectMenuExpanded,
            onExpandedChange = { projectMenuExpanded = it },
        ) {
            OutlinedTextField(
                value = selectedProject?.let { "${it.name} — ${clientNameById[it.clientId]}" }
                    ?: "Select a project",
                onValueChange = {},
                readOnly = true,
                label = { Text("Project *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = projectMenuExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(
                expanded = projectMenuExpanded,
                onDismissRequest = { projectMenuExpanded = false },
            ) {
                projects.forEach { project ->
                    DropdownMenuItem(
                        text = { Text("${project.name} — ${clientNameById[project.clientId]}") },
                        onClick = {
                            onSelectProject(project.id)
                            projectMenuExpanded = false
                        },
                    )
                }
            }
        }

        if (selectedProject != null) {
            if (state.unbilledItems.isNotEmpty()) {
                Text("Unbilled time", style = MaterialTheme.typography.labelLarge)
                state.unbilledItems.forEach { item -> LineItemRow(item, onRemove = null) }
            }

            if (state.manualItems.isNotEmpty()) {
                Text("Manual items", style = MaterialTheme.typography.labelLarge)
                state.manualItems.forEach { item ->
                    LineItemRow(item, onRemove = { onRemoveManualItem(item) })
                }
            }

            HorizontalDivider()
            Text("Add a line item", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = state.manualDescription,
                onValueChange = onManualDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.manualQuantity,
                    onValueChange = onManualQuantityChange,
                    label = { Text("Qty") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.manualUnitPrice,
                    onValueChange = onManualUnitPriceChange,
                    label = { Text("Unit price (${selectedProject.rate.currencyCode})") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
            }
            OutlinedButton(onClick = onAddManualItem, enabled = state.canAddManualItem) {
                Text("Add item")
            }

            HorizontalDivider()
            val subtotal = state.allItems.fold(Money.zero(selectedProject.rate.currencyCode)) { acc, item ->
                acc + item.lineTotal
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Subtotal", fontWeight = FontWeight.Bold)
                Text(subtotal.format(), fontWeight = FontWeight.Bold)
            }

            OutlinedTextField(
                value = state.dueInDays,
                onValueChange = onDueInDaysChange,
                label = { Text("Due in (days)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = state.notes,
                onValueChange = onNotesChange,
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = onSubmit, enabled = state.canSubmit) {
                Text(if (state.isSubmitting) "Creating…" else "Create invoice")
            }
        }
    }
}

@Composable
private fun LineItemRow(item: InvoiceLineItemDraft, onRemove: (() -> Unit)?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.description)
            Text(
                "${item.quantity} × ${item.unitPrice.format()}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Text(item.lineTotal.format())
        if (onRemove != null) {
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Close, contentDescription = "Remove item")
            }
        }
    }
}
