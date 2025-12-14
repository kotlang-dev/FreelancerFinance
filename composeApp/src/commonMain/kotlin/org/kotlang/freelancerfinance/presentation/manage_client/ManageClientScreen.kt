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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
import org.kotlang.freelancerfinance.presentation.design_system.layout.StandardEmptyStateView
import org.kotlang.freelancerfinance.presentation.design_system.textfields.StandardSearchBar
import org.kotlang.freelancerfinance.presentation.theme.MoneyGreen

@Composable
fun ManageClientScreenRoot(
    viewModel: ManageClientViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onAddEditClient: (Long?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ManageClientScreen(
        state = uiState,
        onAction = { action ->
            when (action) {
                ManageClientUiAction.OnGoBackClick -> onNavigateBack()
                is ManageClientUiAction.OnAddEditClientClick -> onAddEditClient(action.clientId)
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
                .widthIn(max = 700.dp)
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
        ) {

            StandardSearchBar(
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
                    onButtonClick = { onAction(ManageClientUiAction.OnAddEditClientClick(null)) }
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
                        ClientItemCard(
                            client = client,
                            onCardClick = {
                                onAction(ManageClientUiAction.OnAddEditClientClick(client.id))
                            }
                        )
                    }
                }
            }
        }

        FinanceTopBar(
            title = "Manage Clients",
            onNavigateBack = { onAction(ManageClientUiAction.OnGoBackClick) }
        )

        if (state.showFab) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = { onAction(ManageClientUiAction.OnAddEditClientClick(null)) },
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
private fun ClientItemCard(
    client: ClientListItemUi,
    onCardClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier.fillMaxWidth().clickable { onCardClick() }
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
    val (text, color) = when (status) {
        ClientStatus.GSTIN -> "GSTIN" to MoneyGreen
        ClientStatus.UNREGISTERED -> "Unregistered" to Color.Gray
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        color = color.copy(alpha = 0.08f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
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