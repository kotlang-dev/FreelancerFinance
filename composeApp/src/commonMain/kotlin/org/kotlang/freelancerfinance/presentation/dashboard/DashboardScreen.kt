package org.kotlang.freelancerfinance.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_card
import freelancerfinance.composeapp.generated.resources.ic_description
import freelancerfinance.composeapp.generated.resources.ic_outline_add
import freelancerfinance.composeapp.generated.resources.ic_person
import freelancerfinance.composeapp.generated.resources.img_company_logo_placeholder
import freelancerfinance.composeapp.generated.resources.img_no_invoices_placeholder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.InvoiceSummary
import org.kotlang.freelancerfinance.presentation.design_system.layout.StandardEmptyStateView
import org.kotlang.freelancerfinance.presentation.theme.FinanceAppTheme
import org.kotlang.freelancerfinance.presentation.theme.MoneyGreen
import org.kotlang.freelancerfinance.presentation.util.toIndianCurrency
import org.kotlang.freelancerfinance.presentation.util.toInvoiceDateUi

@Composable
fun DashboardScreenRoot(
    viewModel: DashboardViewModel = koinViewModel(),
    onManageClients: () -> Unit,
    onManageServices: () -> Unit,
    onEditProfile: () -> Unit,
    onCreateInvoice: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        state = uiState,
        onAction = { action ->
            when (action) {
                DashboardUiAction.OnManageClientsClick -> onManageClients()
                DashboardUiAction.OnManageServicesClick -> onManageServices()
                DashboardUiAction.OnEditProfileClick -> onEditProfile()
                DashboardUiAction.OnCreateInvoiceClick -> onCreateInvoice()
            }
        }
    )
}

@Composable
private fun DashboardScreen(
    state: DashboardUiState,
    onAction: (DashboardUiAction) -> Unit
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .widthIn(max = 700.dp) // Desktop constraint
        ) {
            DashboardHeader(
                companyName = state.companyName,
                logoPath = state.logoPath,
                totalInvoicedValue = state.totalInvoicedValue,
                onEditProfileClick = { onAction(DashboardUiAction.OnEditProfileClick) }
            )

            Card(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .offset(y = (-50).dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                ActionButtonsRow(
                    onManageClientsClick = { onAction(DashboardUiAction.OnManageClientsClick) },
                    onManageServicesClick = { onAction(DashboardUiAction.OnManageServicesClick) }
                )
            }

            when {
                state.isLoading -> {
                    Box(Modifier.size(400.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.showEmptyState -> {
                    StandardEmptyStateView(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        imageResId = Res.drawable.img_no_invoices_placeholder,
                        title = "No invoices yet",
                        description = "Create your first invoice to start tracking your business earnings.",
                        buttonText = "Create New Invoice",
                        onButtonClick = { onAction(DashboardUiAction.OnCreateInvoiceClick)}
                    )
                }
                else -> {
                    InvoiceListView(
                        modifier = Modifier
                            .widthIn(max = 700.dp)
                            .align(Alignment.CenterHorizontally),
                        invoices = state.recentInvoices
                    )
                }
            }
        }
        if (state.showContent) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = { onAction(DashboardUiAction.OnCreateInvoiceClick) },
                content = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_outline_add),
                        contentDescription = "Create Invoice"
                    )
                }
            )
        }
    }
}

@Composable
fun DashboardHeader(
    companyName: String,
    logoPath: String?,
    totalInvoicedValue: Double,
    onEditProfileClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(bottomStartPercent = 10, bottomEndPercent = 10))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        // Top Row: Greeting & Profile
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = logoPath,
                contentDescription = "Company Logo",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.img_company_logo_placeholder),
                fallback = painterResource(Res.drawable.img_company_logo_placeholder)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Welcome back,",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
                Text(
                    text = companyName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2
                )
            }

            IconButton(
                onClick = onEditProfileClick
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_card),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Invoiced Value",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "₹ ${totalInvoicedValue.toIndianCurrency()}",
                style = MaterialTheme.typography.displayMedium, // Large text
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun ActionButtonsRow(
    modifier: Modifier = Modifier,
    onManageClientsClick: () -> Unit,
    onManageServicesClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionCard(
            text = "Manage Clients",
            iconResId = Res.drawable.ic_person,
            modifier = Modifier.weight(1f),
            onClick = onManageClientsClick
        )
        VerticalDivider(
            modifier = Modifier.height(80.dp),
            thickness = Dp.Hairline,
            color = MaterialTheme.colorScheme.primary
        )
        ActionCard(
            text = "Manage Services",
            iconResId = Res.drawable.ic_description,
            modifier = Modifier.weight(1f),
            onClick = onManageServicesClick
        )
    }
}

@Composable
fun ActionCard(
    text: String,
    iconResId: DrawableResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun InvoiceItemCard(
    invoice: InvoiceSummary
) {
    val timestamp = invoice.date
    val uiDate = remember(timestamp) { timestamp.toInvoiceDateUi() }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        // Subtle border for definition
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date Column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(40.dp)
            ) {
                Text(
                    text = uiDate.month,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = uiDate.day,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = invoice.clientName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = invoice.invoiceNumber,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Price
            Text(
                text = "₹ ${invoice.totalAmount.toIndianCurrency()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MoneyGreen
            )
        }
    }
}

@Composable
private fun InvoiceListView(
    modifier: Modifier = Modifier,
    invoices: List<InvoiceSummary>
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Invoices",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = { /* Navigate to All */ }) {
                Text("View All")
            }
        }

        invoices.forEach { invoice ->
            InvoiceItemCard(invoice = invoice)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDashboardScreen() {
    FinanceAppTheme {
        DashboardScreen(
            state = DashboardUiState(),
            onAction = {}
        )
    }
}