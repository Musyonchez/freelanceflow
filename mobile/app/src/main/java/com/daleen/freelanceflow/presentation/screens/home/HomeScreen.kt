package com.daleen.freelanceflow.presentation.screens.home

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daleen.freelanceflow.ui.theme.AvatarShape
import com.daleen.freelanceflow.ui.theme.Blue
import com.daleen.freelanceflow.ui.theme.Brand
import com.daleen.freelanceflow.ui.theme.Line
import com.daleen.freelanceflow.ui.theme.Mint
import com.daleen.freelanceflow.ui.theme.Muted
import com.daleen.freelanceflow.ui.theme.OnBlueIcon
import com.daleen.freelanceflow.ui.theme.OnPeachIcon
import com.daleen.freelanceflow.ui.theme.Peach
import com.daleen.freelanceflow.ui.theme.PrimaryButtonShape
import com.daleen.freelanceflow.ui.theme.Shapes
import com.daleen.freelanceflow.ui.theme.Surface

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    reportViewModel: ReportViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val summary by viewModel.summary.collectAsStateWithLifecycle()
    val reportPeriod by reportViewModel.period.collectAsStateWithLifecycle()
    val isExporting by reportViewModel.isExporting.collectAsStateWithLifecycle()
    var showCurrencyDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(reportViewModel) {
        reportViewModel.shareEvent.collect { uri ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share report"))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text("Good evening", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Reporting in ${summary.baseCurrency}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            IconButton(onClick = { showCurrencyDialog = true }) {
                Icon(Icons.Filled.Edit, contentDescription = "Change base currency")
            }
        }

        SummaryCard(
            title = "Outstanding",
            amount = summary.outstandingTotal.format(),
            icon = Icons.Outlined.AccountBalanceWallet,
            badgeColor = Blue,
            iconColor = OnBlueIcon,
        )
        SummaryCard(
            title = "Paid",
            amount = summary.paidTotal.format(),
            icon = Icons.Outlined.Check,
            badgeColor = Mint,
            iconColor = Brand,
        )
        SummaryCard(
            title = "Expenses",
            amount = summary.expenseTotal.format(),
            icon = Icons.Outlined.Receipt,
            badgeColor = Peach,
            iconColor = OnPeachIcon,
        )

        if (summary.outstandingByCurrency.size > 1) {
            Text("Outstanding by currency", style = MaterialTheme.typography.titleMedium)
            summary.outstandingByCurrency.forEach { (currency, amount) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(currency)
                    Text(amount.format())
                }
            }
        }

        Text("Export report", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ReportPeriod.entries.forEach { candidate ->
                FilterChip(
                    selected = reportPeriod == candidate,
                    onClick = { reportViewModel.setPeriod(candidate) },
                    label = { Text(candidate.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Mint,
                        selectedLabelColor = Brand,
                    ),
                )
            }
        }
        Button(
            onClick = reportViewModel::exportCsv,
            enabled = !isExporting,
            shape = PrimaryButtonShape,
            colors = ButtonDefaults.buttonColors(containerColor = Brand, contentColor = Color.White),
        ) {
            if (isExporting) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
            } else {
                Text("Export CSV", style = MaterialTheme.typography.labelLarge)
            }
        }
    }

    if (showCurrencyDialog) {
        BaseCurrencyDialog(
            initialValue = summary.baseCurrency,
            onDismiss = { showCurrencyDialog = false },
            onSave = {
                viewModel.setBaseCurrency(it)
                showCurrencyDialog = false
            },
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: String,
    icon: ImageVector,
    badgeColor: Color,
    iconColor: Color,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Line),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(badgeColor, AvatarShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = iconColor)
            }
            Column {
                Text(title, style = MaterialTheme.typography.labelMedium, color = Muted)
                Text(amount, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
private fun BaseCurrencyDialog(initialValue: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Base currency") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it.take(3) },
                label = { Text("ISO code, e.g. USD") },
                singleLine = true,
            )
        },
        confirmButton = { TextButton(onClick = { onSave(text) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
