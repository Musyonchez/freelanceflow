package com.daleen.freelanceflow.presentation.screens.clients

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.daleen.freelanceflow.domain.model.Client
import com.daleen.freelanceflow.presentation.components.PlaceholderScreen
import com.daleen.freelanceflow.ui.theme.ActionSheetShape
import com.daleen.freelanceflow.ui.theme.Amber
import com.daleen.freelanceflow.ui.theme.AvatarOnAmber
import com.daleen.freelanceflow.ui.theme.AvatarOnDark
import com.daleen.freelanceflow.ui.theme.AvatarOnPurple
import com.daleen.freelanceflow.ui.theme.AvatarShape
import com.daleen.freelanceflow.ui.theme.Brand
import com.daleen.freelanceflow.ui.theme.FabShape
import com.daleen.freelanceflow.ui.theme.Line
import com.daleen.freelanceflow.ui.theme.Mint
import com.daleen.freelanceflow.ui.theme.Muted
import com.daleen.freelanceflow.ui.theme.Purple
import com.daleen.freelanceflow.ui.theme.ShadowFabColor
import com.daleen.freelanceflow.ui.theme.ShadowFabElevation
import com.daleen.freelanceflow.ui.theme.ShadowPanelColor
import com.daleen.freelanceflow.ui.theme.ShadowPanelElevation
import com.daleen.freelanceflow.ui.theme.Shapes
import com.daleen.freelanceflow.ui.theme.Surface

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
                            clients.forEachIndexed { index, client ->
                                if (index > 0) HorizontalDivider(thickness = 1.dp, color = Line)
                                ClientRow(client = client, onClick = { viewModel.showEditForm(client) })
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
            Icon(Icons.Filled.Add, contentDescription = "Add client")
        }
    }

    if (formState != null) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideForm() },
            sheetState = sheetState,
            shape = ActionSheetShape,
            containerColor = Surface,
        ) {
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

private data class AvatarTone(val background: Color, val foreground: Color)

private val AvatarTones = listOf(
    AvatarTone(AvatarOnDark, Color.White),
    AvatarTone(Purple, AvatarOnPurple),
    AvatarTone(Amber, AvatarOnAmber),
    AvatarTone(Mint, Brand),
)

private fun avatarToneFor(id: String): AvatarTone =
    AvatarTones[(id.hashCode().mod(AvatarTones.size))]

private fun initialsFor(name: String): String =
    name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.take(2)
        .joinToString("") { it.first().uppercase() }

@Composable
private fun ClientRow(client: Client, onClick: () -> Unit) {
    val tone = avatarToneFor(client.id)
    ListItem(
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(tone.background, AvatarShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    initialsFor(client.name),
                    style = MaterialTheme.typography.labelLarge,
                    color = tone.foreground,
                )
            }
        },
        headlineContent = { Text(client.name) },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                client.company?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = Muted) }
                Text("Bills in ${client.defaultCurrency}", style = MaterialTheme.typography.bodySmall, color = Muted)
            }
        },
        trailingContent = {
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = Muted)
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
    )
}
