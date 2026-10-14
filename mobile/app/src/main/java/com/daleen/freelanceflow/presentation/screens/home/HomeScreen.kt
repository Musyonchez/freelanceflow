package com.daleen.freelanceflow.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val summary by viewModel.summary.collectAsStateWithLifecycle()
    var showCurrencyDialog by remember { mutableStateOf(false) }

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

        SummaryCard(title = "Outstanding", amount = summary.outstandingTotal.format())
        SummaryCard(title = "Paid", amount = summary.paidTotal.format())
        SummaryCard(title = "Expenses", amount = summary.expenseTotal.format())

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
private fun SummaryCard(title: String, amount: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge)
            Text(amount, style = MaterialTheme.typography.headlineMedium)
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
