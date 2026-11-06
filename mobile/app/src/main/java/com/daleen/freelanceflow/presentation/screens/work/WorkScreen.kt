package com.daleen.freelanceflow.presentation.screens.work

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daleen.freelanceflow.domain.model.Project
import com.daleen.freelanceflow.domain.model.RateType
import com.daleen.freelanceflow.domain.model.TimeEntry
import com.daleen.freelanceflow.presentation.components.PlaceholderScreen
import com.daleen.freelanceflow.ui.theme.Brand
import com.daleen.freelanceflow.ui.theme.BrandDark
import com.daleen.freelanceflow.ui.theme.FabShape
import com.daleen.freelanceflow.ui.theme.LiveDotRunning
import com.daleen.freelanceflow.ui.theme.Line
import com.daleen.freelanceflow.ui.theme.Mint
import com.daleen.freelanceflow.ui.theme.Muted
import com.daleen.freelanceflow.ui.theme.ShadowFabColor
import com.daleen.freelanceflow.ui.theme.ShadowFabElevation
import com.daleen.freelanceflow.ui.theme.ShadowPanelColor
import com.daleen.freelanceflow.ui.theme.ShadowPanelElevation
import com.daleen.freelanceflow.ui.theme.Shapes
import com.daleen.freelanceflow.ui.theme.Surface
import com.daleen.freelanceflow.ui.theme.TimerControlShape
import com.daleen.freelanceflow.ui.theme.TimerCardShape
import com.daleen.freelanceflow.ui.theme.TimerDark
import com.daleen.freelanceflow.ui.theme.TimerSubtextWeak
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
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .shadow(ShadowPanelElevation, Shapes.large, ambientColor = ShadowPanelColor, spotColor = ShadowPanelColor),
                        shape = Shapes.large,
                        colors = CardDefaults.cardColors(containerColor = Surface),
                    ) {
                        Column {
                            projects.forEachIndexed { index, project ->
                                if (index > 0) {
                                    HorizontalDivider(thickness = 1.dp, color = Line)
                                }
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
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.showAddForm() },
            shape = FabShape,
            containerColor = Brand,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .shadow(ShadowFabElevation, FabShape, ambientColor = ShadowFabColor, spotColor = ShadowFabColor),
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

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = TimerCardShape,
        colors = CardDefaults.cardColors(containerColor = TimerDark),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .shadow(4.dp, CircleShape, ambientColor = LiveDotRunning.copy(alpha = .12f), spotColor = LiveDotRunning.copy(alpha = .12f))
                            .background(LiveDotRunning, CircleShape),
                    )
                    Text(
                        "Timer running",
                        style = MaterialTheme.typography.labelMedium,
                        color = TimerSubtextWeak,
                        modifier = Modifier.padding(start = 6.dp),
                    )
                }
                Text(projectName, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(
                    formatElapsed(elapsedMillis),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                )
            }
            IconButton(
                onClick = onStop,
                modifier = Modifier
                    .size(38.dp)
                    .background(Mint, TimerControlShape),
                colors = IconButtonDefaults.iconButtonColors(contentColor = BrandDark),
            ) {
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
                Text(clientName, style = MaterialTheme.typography.bodySmall, color = Muted)
                Text(
                    "${project.rate.format()} ${if (project.rateType == RateType.HOURLY) "/ hr" else "fixed"} · ${project.status.name.lowercase()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Muted,
                )
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
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
    )
}
