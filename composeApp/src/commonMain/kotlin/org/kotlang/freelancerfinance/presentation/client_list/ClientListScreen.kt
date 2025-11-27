package org.kotlang.freelancerfinance.presentation.client_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_arrow_back
import freelancerfinance.composeapp.generated.resources.ic_delete
import freelancerfinance.composeapp.generated.resources.ic_outline_add
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.IndianState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel = koinViewModel<ClientListViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showAddClientForm by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.clients.isEmpty() && !uiState.isLoading) {
            // Empty State
            Text(
                "No clients yet. Add one!",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                TopAppBar(
                    title = { Text(text = "Clients") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_arrow_back),
                                contentDescription = "Navigate Back"
                            )
                        }
                    }
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.clients) { client ->
                        ClientItem(
                            client = client,
                            onDelete = { viewModel.onAction(ClientListUiAction.DeleteClient(client)) }
                        )
                    }
                }
            }
        }

        // Simple "Add Client" Overlay (For MVP)
        if (showAddClientForm) {
            AddClientDialog(
                onDismiss = { showAddClientForm = false },
                onConfirm = { name, gstin, address, state, email ->
                    viewModel.onAction(
                        ClientListUiAction.AddClient(name, gstin, address, state, email)
                    )
                    showAddClientForm = false
                }
            )
        }

        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            onClick = { showAddClientForm = true }
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_outline_add),
                contentDescription = "Add Client"
            )
        }
    }
}

@Composable
fun ClientItem(client: Client, onDelete: () -> Unit) {
    Card {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(client.name)
                Text(
                    "State: ${client.state.stateName} (${client.state.code})",
                    style = MaterialTheme.typography.bodyLarge
                )
                if (client.gstin != null) {
                    Text(
                        "GSTIN: ${client.gstin}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Blue
                    )
                } else {
                    Text(
                        "Unregistered",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun AddClientDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String?, String, IndianState, String?) -> Unit
) {
    // Local state for the form inputs
    var name by remember { mutableStateOf("") }
    var gstin by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf(IndianState.MAHARASHTRA) }

    // A simple full-screen "Dialog" style overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false) {}, // Block clicks behind
        contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.padding(16.dp).fillMaxWidth(0.9f)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Add New Client")

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Client Name") }
                )

                OutlinedTextField(
                    value = gstin,
                    onValueChange = { gstin = it },
                    label = { Text("GSTIN (Optional)") }
                )

                // Simplified State Switcher (Cycle through logic)
                Button(onClick = {
                    val nextOrdinal = (selectedState.ordinal + 1) % IndianState.entries.size
                    selectedState = IndianState.entries[nextOrdinal]
                }) {
                    Text("State: ${selectedState.stateName}")
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") }
                )

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(onClick = {
                        if (name.isNotBlank()) {
                            onConfirm(name, gstin, address, selectedState, email)
                        }
                    }) { Text("Add") }
                }
            }
        }
    }
}