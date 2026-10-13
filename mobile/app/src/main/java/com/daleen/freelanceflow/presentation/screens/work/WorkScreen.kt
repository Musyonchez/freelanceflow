package com.daleen.freelanceflow.presentation.screens.work

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.domain.model.RateType
import com.daleen.freelanceflow.domain.model.TimeEntry
import com.daleen.freelanceflow.presentation.components.PlaceholderScreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkScreen(viewModel: ProjectsViewModel = hiltViewModel(), modifier: Modifier = Modifier) {
    val projects by viewModel.projects.collectAsStateWithLifecycle()
    val clients by viewModel.clients.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val runningEntry by viewModel.runningEntry.collectAsStateWithLifecycle()
    val clientNameById = clients.associate { it.id to it.name }
    val projectNameById = projects.associate { it.id to it.name }

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
                runningEntry?.let { entry ->
                    item {
                        RunningTimerCard(
                            entry = entry,
                            projectName = projectNameById[entry.projectId] ?: "Unknown project",
                            onStop = viewModel::stopTimer,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        )
                    }
                }
                items(projects, key = { it.id }) { project ->
                    ProjectRow(
                        project = project,
                        clientName = clientNameById[project.clientId] ?: "Unknown client",
                        isTimerRunningHere = runningEntry?.projectId == project.id,
                        onClick = { viewModel.showEditForm(project) },
                        onToggleTimer = {
                            if (runningEntry?.projectId == project.id) {
                                viewModel.stopTimer()
                            } else {
                                viewModel.startTimer(project.id)
                            }
                        },
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
private fun RunningTimerCard(
    entry: TimeEntry,
    projectName: String,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var elapsedMillis by remember(entry.id) {
        mutableLongStateOf(System.currentTimeMillis() - (entry.startTime ?: System.currentTimeMillis()))
    }
    LaunchedEffect(entry.id) {
        while (true) {
            elapsedMillis = System.currentTimeMillis() - (entry.startTime ?: System.currentTimeMillis())
            delay(1_000)
        }
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text("Timer running", style = MaterialTheme.typography.labelMedium)
                Text(projectName, style = MaterialTheme.typography.titleMedium)
                Text(formatElapsed(elapsedMillis), style = MaterialTheme.typography.headlineSmall)
            }
            IconButton(onClick = onStop) {
                Icon(Icons.Filled.Stop, contentDescription = "Stop timer")
            }
        }
    }
}

private fun formatElapsed(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d:%02d".format(hours, minutes, seconds)
}

@Composable
private fun ProjectRow(
    project: Project,
    clientName: String,
    isTimerRunningHere: Boolean,
    onClick: () -> Unit,
    onToggleTimer: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(project.name) },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(clientName)
                Text("${project.rate.format()} ${if (project.rateType == RateType.HOURLY) "/ hr" else "fixed"} · ${project.status.name.lowercase()}")
            }
        },
        trailingContent = {
            IconButton(onClick = onToggleTimer) {
                Icon(
                    if (isTimerRunningHere) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                    contentDescription = if (isTimerRunningHere) "Stop timer" else "Start timer",
                )
            }
        },
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
    )
}
