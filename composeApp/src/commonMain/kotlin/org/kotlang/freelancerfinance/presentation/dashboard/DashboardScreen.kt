package org.kotlang.freelancerfinance.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import freelancerfinance.composeapp.generated.resources.ic_description
import freelancerfinance.composeapp.generated.resources.ic_outline_add
import freelancerfinance.composeapp.generated.resources.ic_person
import freelancerfinance.composeapp.generated.resources.img_company_logo_placeholder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.presentation.theme.FinanceAppTheme
import org.kotlang.freelancerfinance.presentation.theme.MoneyGreen
import java.text.NumberFormat
import java.util.Locale

data class Invoice(
    val id: String,
    val clientName: String,
    val invoiceNumber: String,
    val amount: Double,
    val day: String,
    val month: String
)

val sampleInvoices = listOf(
    Invoice("1", "Client Name Inc.", "Invoice #INV-0024", 5000.0, "15", "OCT"),
    Invoice("2", "Innovate LLC", "Invoice #INV-0023", 12250.0, "12", "OCT"),
    Invoice("3", "Synergy Corp", "Invoice #INV-0022", 8700.0, "28", "SEP"),
    Invoice("4", "Alpha Stream", "Invoice #INV-0021", 2400.0, "10", "SEP"),
)

@Composable
fun DashboardScreenRoot(
    viewModel: DashboardViewModel = koinViewModel(),
    onManageClients: () -> Unit,
    onEditProfile: () -> Unit,
    onCreateInvoice: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        state = uiState,
        onAction = { action ->
            when (action) {
                DashboardUiAction.OnManageClientsClick -> onManageClients()
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
                logoPath = state.logoPath
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-50).dp) // Overlap logic
                    .clickable { }
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                ActionButtonsRow(
                    onManageClientsClick = { onAction(DashboardUiAction.OnManageClientsClick) },
                    onEditProfileClick = { onAction(DashboardUiAction.OnEditProfileClick) }
                )
            }

            // 3. Recent Invoices Title
            Text(
                text = "Recent Invoices",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 0.dp, bottom = 12.dp)
            )

            // 4. Invoices List
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp), // Space for FAB
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                sampleInvoices.forEach { invoice ->
                    InvoiceItemCard(invoice = invoice)
                }
            }
        }
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
            },
        )
    }
}

@Composable
fun DashboardHeader(
    companyName: String,
    logoPath: String?
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
                text = "₹ 1,50,000",
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
    onEditProfileClick: () -> Unit
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
            text = "Edit Profile",
            iconResId = Res.drawable.ic_description,
            modifier = Modifier.weight(1f),
            onClick = onEditProfileClick
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
fun InvoiceItemCard(invoice: Invoice) {
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
                    text = invoice.month,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = invoice.day,
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
                text = "₹ ${NumberFormat.getNumberInstance(Locale.US).format(invoice.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MoneyGreen
            )
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