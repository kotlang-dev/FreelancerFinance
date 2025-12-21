package org.kotlang.freelancerfinance.presentation.invoice

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_calendar_today
import freelancerfinance.composeapp.generated.resources.ic_calender_event
import freelancerfinance.composeapp.generated.resources.ic_close
import freelancerfinance.composeapp.generated.resources.ic_edit
import freelancerfinance.composeapp.generated.resources.ic_factory
import freelancerfinance.composeapp.generated.resources.ic_outline_add
import freelancerfinance.composeapp.generated.resources.ic_person_add
import freelancerfinance.composeapp.generated.resources.ic_save
import freelancerfinance.composeapp.generated.resources.ic_tag
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.ClientStatus
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
import org.kotlang.freelancerfinance.presentation.design_system.dialog.StandardDatePickerDialog
import org.kotlang.freelancerfinance.presentation.design_system.textfields.StandardTextField
import org.kotlang.freelancerfinance.presentation.invoice.component.ClientSelectionSheet
import org.kotlang.freelancerfinance.presentation.util.ObserveAsEvents
import org.kotlang.freelancerfinance.presentation.util.toFormattedDateString
import java.text.NumberFormat
import java.util.Locale

// --- Data Models ---
data class InvoiceItemData(
    val title: String,
    val description: String,
    val quantity: Int, // e.g., Hours
    val rate: Double // e.g., Hourly Rate
) {
    val total: Double get() = quantity * rate
}

// Sample Data
val sampleInvoiceItems = listOf(
    InvoiceItemData("UI Design Consultation", "Initial discovery phase and wireframing.", 10, 50.0),
    InvoiceItemData("Prototype Development", "Interactive Figma prototype.", 1, 350.0)
)

@Composable
fun CreateInvoiceScreenRoot(
    viewModel: CreateInvoiceViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    onNavigateToAddClient: () -> Unit,
    onNavigateToEditClient: (Long) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is CreateInvoiceEvent.NavigateBack -> {
                onNavigateBack()
            }

            is CreateInvoiceEvent.NavigateToAddClient -> {
                onNavigateToAddClient()
            }

            is CreateInvoiceEvent.NavigateToEditClient -> {
                onNavigateToEditClient(event.clientId)
            }

            is CreateInvoiceEvent.ShowSnackbar -> {
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }
        }
    }

    CreateInvoiceScreen(
        state = uiState,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateInvoiceScreen(
    state: CreateInvoiceUiState,
    onAction: (CreateInvoiceUiAction) -> Unit
) {
    val scrollState = rememberScrollState()

    // Calculation Logic
    val subtotal = sampleInvoiceItems.sumOf { it.total }
    val taxRate = 0.18 // 18%
    val taxAmount = subtotal * taxRate
    val grandTotal = subtotal + taxAmount

    if (state.showClientSelectionSheet) {
        ClientSelectionSheet(
            clients = state.availableClients,
            onDismissRequest = { onAction(CreateInvoiceUiAction.OnDismissClientSheet) },
            onClientSelected = { onAction(CreateInvoiceUiAction.OnClientSelected(it)) },
            onAddNewClient = { onAction(CreateInvoiceUiAction.OnAddNewClientClick) },
            onEditClientClick = { onAction(CreateInvoiceUiAction.OnEditClientClick(it)) }
        )
    }

    if (state.activeDatePicker != DatePickerType.None) {
        StandardDatePickerDialog(
            onDismissRequest = { onAction(CreateInvoiceUiAction.OnDateSelected(null)) },
            onConfirmButtonClick = { onAction(CreateInvoiceUiAction.OnDateSelected(it)) }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 700.dp) // Desktop constraint
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(top = 70.dp, bottom = 24.dp)
        ) {
            StandardTextField(
                inputLabel = "INVOICE NO.",
                value = "INV-1024",
                onValueChange = {},
                readOnly = true,
                leadingIconResId = Res.drawable.ic_tag
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DateSelectorField(
                    label = "Issue Date",
                    value = state.issueDate.toFormattedDateString(),
                    iconResId = Res.drawable.ic_calendar_today,
                    onClick = {
                        onAction(CreateInvoiceUiAction.OnDateFieldClick(DatePickerType.IssueDate))
                    },
                    modifier = Modifier.weight(1f)
                )

                DateSelectorField(
                    label = "Due Date",
                    value = state.dueDate.toFormattedDateString(),
                    iconResId = Res.drawable.ic_calender_event,
                    onClick = {
                        onAction(CreateInvoiceUiAction.OnDateFieldClick(DatePickerType.DueDate))
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Bill To (Client Selection)
            Text(
                text = "Bill To",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            ClientSelectionBox(
                selectedClient = state.selectedClient,
                onAddClientClick = { onAction(CreateInvoiceUiAction.OnAddClientClick) },
                onChangeClientClick = {
                    //    onAction(CreateInvoiceUiAction.OnChangeClientClick)
                },
                onEditClientClick = {
                    //  onAction(CreateInvoiceUiAction.OnEditClientClick(it))
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Items List
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Items",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${sampleInvoiceItems.size} items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            sampleInvoiceItems.forEach { item ->
                InvoiceItemCard(item)
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 5. Add Item Button (Dashed)
            DashedButton(text = "Add Item")

            Spacer(modifier = Modifier.height(24.dp))

            // 6. Calculations
            CalculationRow(label = "Subtotal", amount = subtotal)
            CalculationRow(label = "Add Discount", amount = 0.0, isLink = true)
            CalculationRow(label = "Tax (18%)", amount = taxAmount)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatCurrency(grandTotal),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 7. Notes
            Text(
                text = "Notes / Payment Terms",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "Please pay within 15 days. Thank you for your business!",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                )
            )
        }

        FinanceTopBar(
            title = "Create Invoice",
            navigationIconResId = Res.drawable.ic_close,
            onNavigateBack = { onAction(CreateInvoiceUiAction.OnGoBackClick) }
        )

        InvoiceBottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            grandTotal = grandTotal
        )
    }
}

// --- Components ---

@Composable
fun InvoiceBottomBar(
    grandTotal: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 700.dp) // Align with content width constraint
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "GRAND TOTAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatCurrency(grandTotal),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Button(
                onClick = { /* Save Logic */ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_save),
                    contentDescription = "Add Service",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save & Preview")
            }
        }
    }
}

@Composable
private fun ClientSelectionBox(
    selectedClient: Client?,
    modifier: Modifier = Modifier,
    onAddClientClick: () -> Unit,
    onChangeClientClick: () -> Unit,
    onEditClientClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .dashedBorder(color = MaterialTheme.colorScheme.outlineVariant, cornerRadius = 12.dp)
            .background(
                MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .clickable {
                if (selectedClient == null) {
                    onAddClientClick()
                } else onChangeClientClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = selectedClient
        ) { client ->
            if (client != null) {
                SelectedClientCard(
                    client = client,
                    modifier = modifier,
                    onEditClick = onEditClientClick
                )
            } else {
                EmptyClientPlaceholder()
            }
        }
    }
}

@Composable
private fun SelectedClientCard(
    client: Client,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit
) {
    val status = if (client.status == ClientStatus.REGISTERED) {
        "GSTIN: ${client.gstin}"
    } else "Unregistered"

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Icon(
                        painter = painterResource(Res.drawable.ic_factory),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = client.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            FilledIconButton(
                onClick = onEditClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = client.fullAddressDisplay,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EmptyClientPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_person_add),
                contentDescription = "Add Client",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No Client Selected",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Select a client to auto-fill details",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun InvoiceItemCard(item: InvoiceItemData) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    item.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(formatCurrency(item.total), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (item.quantity > 5) "${item.quantity} hrs × $${item.rate}" else "Fixed Price",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    "Edit",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }
        }
    }
}

@Composable
fun DashedButton(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .dashedBorder(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                cornerRadius = 12.dp
            )
            .background(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                RoundedCornerShape(12.dp)
            )
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.ic_outline_add),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CalculationRow(label: String, amount: Double, isLink: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isLink) {
            Text(
                text = "+ $label",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { }
            )
        } else {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = formatCurrency(amount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}

fun Modifier.dashedBorder(width: Dp = 1.dp, color: Color, cornerRadius: Dp) = drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        ),
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}

@Composable
private fun DateSelectorField(
    label: String,
    value: String,
    iconResId: DrawableResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        OutlinedCard(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            modifier = Modifier.fillMaxWidth().height(56.dp) // Standard height
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun CreateInvoiceScreenPreview() {
    CreateInvoiceScreen(
        state = CreateInvoiceUiState(
            selectedClient = Client(
                id = 5,
                name = "John Ltd Corp",
                address = "Eden Garden, Bengaluru",
                gstin = "1234567890",
                state = IndianState.KARNATAKA
            )
        ),
        onAction = {}
    )
}