package com.daleen.freelanceflow.presentation.screens.money

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Description
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.daleen.freelanceflow.domain.model.Expense
import com.daleen.freelanceflow.domain.model.Invoice
import com.daleen.freelanceflow.domain.model.InvoiceStatus
import com.daleen.freelanceflow.presentation.components.PlaceholderScreen
import com.daleen.freelanceflow.ui.theme.ActionSheetShape
import com.daleen.freelanceflow.ui.theme.Brand
import com.daleen.freelanceflow.ui.theme.FabShape
import com.daleen.freelanceflow.ui.theme.Ink
import com.daleen.freelanceflow.ui.theme.Line
import com.daleen.freelanceflow.ui.theme.Mint
import com.daleen.freelanceflow.ui.theme.Muted
import com.daleen.freelanceflow.ui.theme.ShadowFabColor
import com.daleen.freelanceflow.ui.theme.ShadowFabElevation
import com.daleen.freelanceflow.ui.theme.ShadowPanelColor
import com.daleen.freelanceflow.ui.theme.ShadowPanelElevation
import com.daleen.freelanceflow.ui.theme.Shapes
import com.daleen.freelanceflow.ui.theme.Surface
import com.daleen.freelanceflow.ui.theme.SurfaceSoft
import com.daleen.freelanceflow.ui.theme.TagAmberBg
import com.daleen.freelanceflow.ui.theme.TagAmberFg
import com.daleen.freelanceflow.ui.theme.TagBlueBg
import com.daleen.freelanceflow.ui.theme.TagBlueFg
import com.daleen.freelanceflow.ui.theme.TagGreenBg
import com.daleen.freelanceflow.ui.theme.TagGreenFg
import androidx.compose.ui.draw.shadow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val TABS = listOf("Invoices", "Expenses", "Tax")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyScreen(
    invoicesViewModel: InvoicesViewModel = hiltViewModel(),
    expensesViewModel: ExpensesViewModel = hiltViewModel(),
    taxViewModel: TaxViewModel = hiltViewModel(),
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

    val taxProfile by taxViewModel.profile.collectAsStateWithLifecycle()
    val taxPeriod by taxViewModel.period.collectAsStateWithLifecycle()
    val deductionSummary by taxViewModel.summary.collectAsStateWithLifecycle()
    val taxFormState by taxViewModel.formState.collectAsStateWithLifecycle()

    var showBusinessNameDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var previewReceiptFile by remember { mutableStateOf<File?>(null) }

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
                    selectedContentColor = Brand,
                    unselectedContentColor = Muted,
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
                        item {
                            PanelContainer {
                                invoices.forEachIndexed { index, invoice ->
                                    if (index > 0) HorizontalDivider(thickness = 1.dp, color = Line)
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
                    }
                }
            } else if (selectedTab == 1) {
                if (expenses.isEmpty()) {
                    PlaceholderScreen(
                        title = "No expenses yet",
                        subtitle = "Tap + to log your first expense.",
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            PanelContainer {
                                expenses.forEachIndexed { index, expense ->
                                    if (index > 0) HorizontalDivider(thickness = 1.dp, color = Line)
                                    ExpenseRow(
                                        expense = expense,
                                        localReceiptFile = expensesViewModel.localReceiptFile(expense),
                                        onDownloadReceipt = { expensesViewModel.downloadReceipt(expense) },
                                        onPreviewReceipt = { previewReceiptFile = it },
                                        onDelete = { expensesViewModel.deleteExpense(expense) },
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                TaxTabContent(
                    profile = taxProfile,
                    period = taxPeriod,
                    summary = deductionSummary,
                    onPeriodChange = taxViewModel::setPeriod,
                )
            }

            FloatingActionButton(
                onClick = {
                    when (selectedTab) {
                        0 -> invoicesViewModel.showBuilder()
                        1 -> expensesViewModel.showAddForm()
                        else -> taxViewModel.showProfileForm()
                    }
                },
                shape = FabShape,
                containerColor = Brand,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .shadow(ShadowFabElevation, FabShape, ambientColor = ShadowFabColor, spotColor = ShadowFabColor),
            ) {
                Icon(
                    if (selectedTab == 2) Icons.Filled.Edit else Icons.Filled.Add,
                    contentDescription = when (selectedTab) {
                        0 -> "Create invoice"
                        1 -> "Add expense"
                        else -> "Edit tax profile"
                    },
                )
            }
        }
    }

    if (builderState != null) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { invoicesViewModel.hideBuilder() },
            sheetState = sheetState,
            shape = ActionSheetShape,
            containerColor = Surface,
        ) {
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
        ModalBottomSheet(
            onDismissRequest = { expensesViewModel.hideForm() },
            sheetState = expenseSheetState,
            shape = ActionSheetShape,
            containerColor = Surface,
        ) {
            ExpenseFormSheet(
                state = expenseFormState!!,
                projects = activeProjects,
                clients = clients,
                onFieldChange = expensesViewModel::updateForm,
                onRequestCameraUri = expensesViewModel::cameraUriForCurrentForm,
                onPhotoCaptured = expensesViewModel::onPhotoCaptured,
                onPhotoPicked = expensesViewModel::onPhotoPicked,
                onRemovePhoto = expensesViewModel::removePhoto,
                onSave = expensesViewModel::saveForm,
            )
        }
    }

    if (taxFormState != null) {
        TaxProfileDialog(
            state = taxFormState!!,
            onFieldChange = taxViewModel::updateForm,
            onDismiss = taxViewModel::hideProfileForm,
            onSave = taxViewModel::saveProfile,
        )
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

    previewReceiptFile?.let { file ->
        Dialog(onDismissRequest = { previewReceiptFile = null }) {
            AsyncImage(
                model = file,
                contentDescription = "Receipt photo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth(),
            )
        }
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
        shape = Shapes.large,
        containerColor = Surface,
        titleContentColor = Ink,
        textContentColor = Ink,
    )
}

private val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())

@Composable
private fun PanelContainer(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .shadow(ShadowPanelElevation, Shapes.large, ambientColor = ShadowPanelColor, spotColor = ShadowPanelColor),
        shape = Shapes.large,
        colors = CardDefaults.cardColors(containerColor = Surface),
    ) {
        Column(content = content)
    }
}

@Composable
private fun StatusTag(text: String, background: Color, foreground: Color) {
    Box(
        modifier = Modifier
            .background(background, Shapes.extraSmall)
            .padding(horizontal = 7.dp, vertical = 3.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall, color = foreground)
    }
}

@Composable
private fun InvoiceRow(
    invoice: Invoice,
    clientName: String,
    onMarkSent: () -> Unit,
    onMarkPaid: () -> Unit,
    onShare: () -> Unit,
) {
    ListItem(
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(SurfaceSoft, Shapes.small),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.Description, contentDescription = null, tint = Brand)
            }
        },
        headlineContent = { Text("${invoice.invoiceNumber} · $clientName") },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "Due ${dateFormat.format(Date(invoice.dueDate))} · ${invoice.total.format()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Muted,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    when (invoice.status) {
                        InvoiceStatus.DRAFT -> AssistChip(onClick = onMarkSent, label = { Text("Mark sent") })
                        InvoiceStatus.SENT -> {
                            StatusTag("SENT", TagBlueBg, TagBlueFg)
                            AssistChip(onClick = onMarkPaid, label = { Text("Mark paid") })
                        }
                        InvoiceStatus.OVERDUE -> {
                            StatusTag("OVERDUE", TagAmberBg, TagAmberFg)
                            AssistChip(onClick = onMarkPaid, label = { Text("Mark paid") })
                        }
                        InvoiceStatus.PAID -> StatusTag("PAID", TagGreenBg, TagGreenFg)
                    }
                }
            }
        },
        trailingContent = {
            IconButton(onClick = onShare) {
                Icon(Icons.Filled.Share, contentDescription = "Share invoice PDF")
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}

@Composable
private fun TaxTabContent(
    profile: com.daleen.freelanceflow.domain.model.TaxProfile?,
    period: TaxPeriod,
    summary: DeductionSummary,
    onPeriodChange: (TaxPeriod) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = profile?.label ?: "No tax profile yet — tap the pencil to set one up",
            style = MaterialTheme.typography.titleMedium,
        )
        if (profile != null) {
            Text(
                text = "Deduction rate: ${profile.deductionRatePercent}%",
                style = MaterialTheme.typography.bodyMedium,
            )
            profile.notes?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TaxPeriod.entries.forEach { candidate ->
                FilterChip(
                    selected = period == candidate,
                    onClick = { onPeriodChange(candidate) },
                    label = { Text(candidate.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Mint,
                        selectedLabelColor = Brand,
                    ),
                )
            }
        }

        WorkspaceStatCard(label = "Deductible expenses", value = summary.deductibleTotal.format())
        WorkspaceStatCard(label = "Estimated deduction", value = summary.estimatedDeduction.format())

        Text(
            text = "Estimate only, based on your own tax profile — not a filed or certified tax figure.",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun WorkspaceStatCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Line),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.displaySmall)
            Text(label, style = MaterialTheme.typography.bodySmall, color = Muted)
        }
    }
}

@Composable
private fun TaxProfileDialog(
    state: TaxProfileFormState,
    onFieldChange: ((TaxProfileFormState) -> TaxProfileFormState) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tax profile") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.label,
                    onValueChange = { value -> onFieldChange { it.copy(label = value) } },
                    label = { Text("Label") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.deductionRatePercent,
                    onValueChange = { value -> onFieldChange { it.copy(deductionRatePercent = value) } },
                    label = { Text("Deduction rate %") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = { value -> onFieldChange { it.copy(notes = value) } },
                    label = { Text("Notes / disclaimer") },
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "User-defined, not sourced from any tax authority — an estimate only.",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        confirmButton = { TextButton(onClick = onSave) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        shape = Shapes.large,
        containerColor = Surface,
        titleContentColor = Ink,
        textContentColor = Ink,
    )
}

@Composable
private fun ExpenseRow(
    expense: Expense,
    localReceiptFile: File?,
    onDownloadReceipt: suspend () -> File?,
    onPreviewReceipt: (File) -> Unit,
    onDelete: () -> Unit,
) {
    var photoFile by remember(expense.id) { mutableStateOf(localReceiptFile) }
    LaunchedEffect(expense.id, expense.receiptPhotoUrl) {
        if (photoFile == null && expense.receiptPhotoUrl != null) {
            photoFile = onDownloadReceipt()
        }
    }

    ListItem(
        leadingContent = photoFile?.let { file ->
            {
                AsyncImage(
                    model = file,
                    contentDescription = "Receipt photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .clickable { onPreviewReceipt(file) },
                )
            }
        },
        headlineContent = { Text(expense.category) },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "${dateFormat.format(Date(expense.date))} · ${expense.amount.format()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Muted,
                )
                if (expense.deductible) {
                    StatusTag("DEDUCTIBLE", TagGreenBg, TagGreenFg)
                }
            }
        },
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete expense")
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.padding(horizontal = 8.dp),
    )
}
