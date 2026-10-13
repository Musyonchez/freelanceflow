package com.daleen.freelanceflow.presentation.screens.money

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daleen.freelanceflow.domain.model.Invoice
import com.daleen.freelanceflow.domain.model.InvoiceStatus
import com.daleen.freelanceflow.presentation.components.PlaceholderScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyScreen(viewModel: InvoicesViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val invoices by viewModel.invoices.collectAsStateWithLifecycle()
    val clients by viewModel.clients.collectAsStateWithLifecycle()
    val activeProjects by viewModel.activeProjects.collectAsStateWithLifecycle()
    val builderState by viewModel.builderState.collectAsStateWithLifecycle()
    val clientNameById = clients.associate { it.id to it.name }

    Box(modifier = modifier.fillMaxSize()) {
        if (invoices.isEmpty()) {
            PlaceholderScreen(
                title = "No invoices yet",
                subtitle = if (activeProjects.isEmpty()) {
                    "Add a project first, then tap + to create an invoice."
                } else {
                    "Tap + to create your first invoice."
                },
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Text(
                        text = "Invoices & expenses",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(24.dp),
                    )
                }
                items(invoices, key = { it.id }) { invoice ->
                    InvoiceRow(
                        invoice = invoice,
                        clientName = clientNameById[invoice.clientId] ?: "Unknown client",
                        onMarkSent = { viewModel.markSent(invoice) },
                        onMarkPaid = { viewModel.markPaid(invoice) },
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.showBuilder() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Create invoice")
        }
    }

    if (builderState != null) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(onDismissRequest = { viewModel.hideBuilder() }, sheetState = sheetState) {
            InvoiceBuilderSheet(
                state = builderState!!,
                projects = activeProjects,
                clients = clients,
                onSelectProject = viewModel::selectProject,
                onManualDescriptionChange = viewModel::updateManualDescription,
                onManualQuantityChange = viewModel::updateManualQuantity,
                onManualUnitPriceChange = viewModel::updateManualUnitPrice,
                onAddManualItem = viewModel::addManualItem,
                onRemoveManualItem = viewModel::removeManualItem,
                onNotesChange = viewModel::updateNotes,
                onDueInDaysChange = viewModel::updateDueInDays,
                onSubmit = viewModel::submit,
            )
        }
    }
}

private val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())

@Composable
private fun InvoiceRow(
    invoice: Invoice,
    clientName: String,
    onMarkSent: () -> Unit,
    onMarkPaid: () -> Unit,
) {
    ListItem(
        headlineContent = { Text("${invoice.invoiceNumber} · $clientName") },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Due ${dateFormat.format(Date(invoice.dueDate))} · ${invoice.total.format()}")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (invoice.status) {
                        InvoiceStatus.DRAFT -> AssistChip(onClick = onMarkSent, label = { Text("Mark sent") })
                        InvoiceStatus.SENT, InvoiceStatus.OVERDUE ->
                            AssistChip(onClick = onMarkPaid, label = { Text("Mark paid") })
                        InvoiceStatus.PAID -> Text("Paid", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        },
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}
