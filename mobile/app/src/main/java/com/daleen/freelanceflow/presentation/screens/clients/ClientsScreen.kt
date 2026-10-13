package com.daleen.freelanceflow.presentation.screens.clients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.presentation.components.PlaceholderScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(viewModel: ClientsViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val clients by viewModel.clients.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        if (clients.isEmpty()) {
            PlaceholderScreen(
                title = "No clients yet",
                subtitle = "Tap + to add your first client.",
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Text(
                        text = "Clients",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(24.dp),
                    )
                }
                items(clients, key = { it.id }) { client ->
                    ClientRow(client = client, onClick = { viewModel.showEditForm(client) })
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.showAddForm() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add client")
        }
    }

    if (formState != null) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(onDismissRequest = { viewModel.hideForm() }, sheetState = sheetState) {
            ClientFormSheet(
                state = formState!!,
                onFieldChange = viewModel::updateForm,
                onSave = viewModel::saveForm,
                onArchive = {
                    formState?.id?.let { id ->
                        clients.find { it.id == id }?.let(viewModel::archiveClient)
                    }
                },
            )
        }
    }
}

@Composable
private fun ClientRow(client: Client, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(client.name) },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                client.company?.let { Text(it) }
                Text("Bills in ${client.defaultCurrency}")
            }
        },
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
    )
}
