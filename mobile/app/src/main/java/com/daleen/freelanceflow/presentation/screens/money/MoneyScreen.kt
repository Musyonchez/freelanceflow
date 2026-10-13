package com.daleen.freelanceflow.presentation.screens.money

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daleen.freelanceflow.domain.model.Expense
import com.daleen.freelanceflow.domain.model.Invoice
import com.daleen.freelanceflow.domain.model.InvoiceStatus
import com.daleen.freelanceflow.presentation.components.PlaceholderScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val TABS = listOf("Invoices", "Expenses")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyScreen(
    invoicesViewModel: InvoicesViewModel = hiltViewModel(),
    expensesViewModel: ExpensesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val invoices by invoicesViewModel.invoices.collectAsStateWithLifecycle()
    val clients by invoicesViewModel.clients.collectAsStateWithLifecycle()
    val activeProjects by invoicesViewModel.activeProjects.collectAsStateWithLifecycle()
    val builderState by invoicesViewModel.builderState.collectAsStateWithLifecycle()
    val businessName by invoicesViewModel.businessName.collectAsStateWithLifecycle()
    val clientNameById = clients.associate { it.id to it.name }

    val expenses by expensesViewModel.expenses.collectAsStateWithLifecycle()
    val expenseFormState by expensesViewModel.formState.collectAsStateWithLifecycle()

    var showBusinessNameDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    LaunchedEffect(invoicesViewModel) {
        invoicesViewModel.shareEvent.collect { uri ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share invoice"))
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 8.dp, top = 24.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "Invoices & expenses", style = MaterialTheme.typography.headlineSmall)
            IconButton(onClick = { showBusinessNameDialog = true }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit business name")
            }
        }

        SecondaryTabRow(selectedTabIndex = selectedTab) {
            TABS.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) },
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (selectedTab == 0) {
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
                        items(invoices, key = { it.id }) { invoice ->
                            InvoiceRow(
                                invoice = invoice,
                                clientName = clientNameById[invoice.clientId] ?: "Unknown client",
                                onMarkSent = { invoicesViewModel.markSent(invoice) },
                                onMarkPaid = { invoicesViewModel.markPaid(invoice) },
                                onShare = { invoicesViewModel.shareInvoice(invoice) },
                            )
                        }
                    }
                }
            } else {
                if (expenses.isEmpty()) {
                    PlaceholderScreen(
                        title = "No expenses yet",
                        subtitle = "Tap + to log your first expense.",
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(expenses, key = { it.id }) { expense ->
                            ExpenseRow(
                                expense = expense,
                                onDelete = { expensesViewModel.deleteExpense(expense) },
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    if (selectedTab == 0) invoicesViewModel.showBuilder() else expensesViewModel.showAddForm()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = if (selectedTab == 0) "Create invoice" else "Add expense",
                )
            }
        }
    }

    if (builderState != null) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(onDismissRequest = { invoicesViewModel.hideBuilder() }, sheetState = sheetState) {
            InvoiceBuilderSheet(
                state = builderState!!,
                projects = activeProjects,
                clients = clients,
                onSelectProject = invoicesViewModel::selectProject,
                onManualDescriptionChange = invoicesViewModel::updateManualDescription,
                onManualQuantityChange = invoicesViewModel::updateManualQuantity,
                onManualUnitPriceChange = invoicesViewModel::updateManualUnitPrice,
                onAddManualItem = invoicesViewModel::addManualItem,
                onRemoveManualItem = invoicesViewModel::removeManualItem,
                onNotesChange = invoicesViewModel::updateNotes,
                onDueInDaysChange = invoicesViewModel::updateDueInDays,
                onSubmit = invoicesViewModel::submit,
            )
        }
    }

    if (expenseFormState != null) {
        val expenseSheetState = rememberModalBottomSheetState()
        ModalBottomSheet(onDismissRequest = { expensesViewModel.hideForm() }, sheetState = expenseSheetState) {
            ExpenseFormSheet(
                state = expenseFormState!!,
                projects = activeProjects,
                clients = clients,
                onFieldChange = expensesViewModel::updateForm,
                onSave = expensesViewModel::saveForm,
            )
        }
    }

    if (showBusinessNameDialog) {
        BusinessNameDialog(
            initialValue = businessName,
            onDismiss = { showBusinessNameDialog = false },
            onSave = {
                invoicesViewModel.setBusinessName(it)
                showBusinessNameDialog = false
            },
        )
    }
}

@Composable
private fun BusinessNameDialog(initialValue: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Business name") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Shown on invoice PDFs") },
                singleLine = true,
            )
        },
        confirmButton = { TextButton(onClick = { onSave(text.trim()) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

private val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())

@Composable
private fun InvoiceRow(
    invoice: Invoice,
    clientName: String,
    onMarkSent: () -> Unit,
    onMarkPaid: () -> Unit,
    onShare: () -> Unit,
) {
    ListItem(
        headlineContent = { Text("${invoice.invoiceNumber} · $clientName") },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Due ${dateFormat.format(Date(invoice.dueDate))} · ${invoice.total.format()}")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    when (invoice.status) {
                        InvoiceStatus.DRAFT -> AssistChip(onClick = onMarkSent, label = { Text("Mark sent") })
                        InvoiceStatus.SENT, InvoiceStatus.OVERDUE ->
                            AssistChip(onClick = onMarkPaid, label = { Text("Mark paid") })
                        InvoiceStatus.PAID -> Text("Paid", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        },
        trailingContent = {
            IconButton(onClick = onShare) {
                Icon(Icons.Filled.Share, contentDescription = "Share invoice PDF")
            }
        },
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}

@Composable
private fun ExpenseRow(expense: Expense, onDelete: () -> Unit) {
    ListItem(
        headlineContent = { Text(expense.category) },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("${dateFormat.format(Date(expense.date))} · ${expense.amount.format()}")
                if (expense.deductible) {
                    Text("Deductible", style = MaterialTheme.typography.labelMedium)
                }
            }
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete expense")
            }
        },
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}
