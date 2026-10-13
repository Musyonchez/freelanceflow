package com.daleen.freelanceflow.presentation.screens.work

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
import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.domain.model.RateType
import com.daleen.freelanceflow.presentation.components.PlaceholderScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkScreen(viewModel: ProjectsViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val projects by viewModel.projects.collectAsStateWithLifecycle()
    val clients by viewModel.clients.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val clientNameById = clients.associate { it.id to it.name }

    Box(modifier = modifier.fillMaxSize()) {
        if (projects.isEmpty()) {
            PlaceholderScreen(
                title = "No projects yet",
                subtitle = "Tap + to add your first project.",
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Text(
                        text = "Projects & time",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(24.dp),
                    )
                }
                items(projects, key = { it.id }) { project ->
                    ProjectRow(
                        project = project,
                        clientName = clientNameById[project.clientId] ?: "Unknown client",
                        onClick = { viewModel.showEditForm(project) },
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.showAddForm() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add project")
        }
    }

    if (formState != null) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(onDismissRequest = { viewModel.hideForm() }, sheetState = sheetState) {
            ProjectFormSheet(
                state = formState!!,
                clients = clients,
                onFieldChange = viewModel::updateForm,
                onSave = viewModel::saveForm,
            )
        }
    }
}

@Composable
private fun ProjectRow(project: Project, clientName: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(project.name) },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(clientName)
                Text("${project.rate.format()} ${if (project.rateType == RateType.HOURLY) "/ hr" else "fixed"} · ${project.status.name.lowercase()}")
            }
        },
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
    )
}
