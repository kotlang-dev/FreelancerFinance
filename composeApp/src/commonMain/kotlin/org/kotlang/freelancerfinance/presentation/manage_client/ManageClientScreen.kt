package org.kotlang.freelancerfinance.presentation.manage_client

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_outline_add
import freelancerfinance.composeapp.generated.resources.img_no_clients_placeholder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
import org.kotlang.freelancerfinance.presentation.design_system.layout.StandardEmptyStateView
import org.kotlang.freelancerfinance.presentation.design_system.textfields.FinanceSearchBar

@Composable
fun ManageClientScreenRoot(
    viewModel: ManageClientViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ManageClientScreen(
        state = uiState,
        onAction = { action ->
            when (action) {
                ManageClientUiAction.OnGoBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageClientScreen(
    state: ManageClientUiState,
    onAction: (ManageClientUiAction) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 700.dp)
        ) {
            FinanceTopBar(
                title = "Manage Clients",
                onNavigateBack = { onAction(ManageClientUiAction.OnGoBackClick) }
            )

            FinanceSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                value = state.searchQuery,
                onValueChange = { onAction(ManageClientUiAction.OnSearchQueryChange(it)) },
                placeholderText = "Search clients..."
            )

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            if (state.showEmptyState) {
                StandardEmptyStateView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 120.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
                    imageResId = Res.drawable.img_no_clients_placeholder,
                    title = "No clients yet",
                    description = "Add your customers to manage invoices and track payments easily.",
                    buttonText = "Add First Client",
                    onButtonClick = { /* Open Add Client Dialog */ }
                )
            }

            if (state.showContent) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(state.filteredClients) { client ->
                        ClientItemCard(client)
                    }
                }
            }
        }

        if (state.showFab) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = { },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_outline_add),
                        contentDescription = "Add Client"
                    )
                }
            )
        }

    }
}

@Composable
private fun ClientItemCard(client: ClientListItemUi) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClientAvatar(
                name = client.name,
                initials = client.initials
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Info Column
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = client.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Status Chip
                    StatusChip(status = client.status)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = client.locationShort,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ClientAvatar(
    name: String,
    initials: String
) {

    // Deterministic random color based on name hash
    val colorIndex = kotlin.math.abs(name.hashCode()) % 4
    val (bgColor, textColor) = when (colorIndex) {
        0 -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        1 -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        2 -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        else -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun StatusChip(status: ClientStatus) {
    // Define colors based on status
    val (text, color) = when (status) {
        ClientStatus.GSTIN -> "GSTIN" to Color(0xFF2E7D32) // Green
        ClientStatus.UNREGISTERED -> "Unregistered" to Color.Gray
        ClientStatus.PENDING -> "Pending" to Color(0xFFF57C00) // Orange
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        color = color.copy(alpha = 0.08f) // Very light tint background
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall, // Tiny text
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
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

@Preview
@Composable
fun PreviewClientListScreen() {
    ManageClientScreen(
        state = ManageClientUiState(),
        onAction = {}
    )
}