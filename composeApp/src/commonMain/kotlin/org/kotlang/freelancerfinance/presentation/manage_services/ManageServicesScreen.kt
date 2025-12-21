package org.kotlang.freelancerfinance.presentation.manage_services

import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_outline_add
import freelancerfinance.composeapp.generated.resources.img_no_clients_placeholder
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.ServiceItem
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
import org.kotlang.freelancerfinance.presentation.design_system.layout.InitialsAvatar
import org.kotlang.freelancerfinance.presentation.design_system.layout.StandardEmptyStateView
import org.kotlang.freelancerfinance.presentation.design_system.textfields.StandardSearchBar

@Composable
fun ManageServicesScreenRoot(
    viewModel: ManageServicesViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onAddEditService: (Long?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ManageServicesScreen(
        state = uiState,
        onAction = { action ->
            when (action) {
                ManageServicesUiAction.OnGoBackClick -> onNavigateBack()
                is ManageServicesUiAction.OnAddEditServiceClick -> onAddEditService(action.serviceId)
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageServicesScreen(
    state: ManageServicesUiState,
    onAction: (ManageServicesUiAction) -> Unit
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
                onValueChange = { onAction(ManageServicesUiAction.OnSearchQueryChange(it)) },
                placeholderText = "Search services..."
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
                    imageResId = Res.drawable.img_no_clients_placeholder, //TODO change this
                    title = "No services saved",
                    description = "Save your frequently used items (like 'Hourly Consulting') to create invoices faster.",
                    buttonText = "Add First Service",
                    onButtonClick = { onAction(ManageServicesUiAction.OnAddEditServiceClick(null)) }
                )
            }

            if (state.showContent) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp), // Space for FAB
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(state.filteredServices) { service ->
                        ServiceItemCard(
                            service = service,
                            onCardClick = {
                                onAction(ManageServicesUiAction.OnAddEditServiceClick(service.id))
                            }
                        )
                    }
                }
            }
        }

        FinanceTopBar(
            title = "Manage Services",
            onNavigateBack = { onAction(ManageServicesUiAction.OnGoBackClick) }
        )

        if (state.showFab) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = { onAction(ManageServicesUiAction.OnAddEditServiceClick(null)) },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_outline_add),
                        contentDescription = "Add Service"
                    )
                }
            )
        }
    }
}

@Composable
private fun ServiceItemCard(
    service: ServiceItem,
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
            InitialsAvatar(
                name = service.name,
                initials = service.initials
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹ ${service.formattedPrice}", // e.g. "₹ 50,000"
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = " • ",
                        color = MaterialTheme.colorScheme.outline
                    )

                    Text(
                        text = "${service.taxRate}% GST",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}