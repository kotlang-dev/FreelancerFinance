package org.kotlang.freelancerfinance.presentation.create_invoice

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_calendar_today
import freelancerfinance.composeapp.generated.resources.ic_calender_event
import freelancerfinance.composeapp.generated.resources.ic_close
import freelancerfinance.composeapp.generated.resources.ic_edit
import freelancerfinance.composeapp.generated.resources.ic_factory
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
import org.kotlang.freelancerfinance.presentation.create_invoice.component.ClientSelectionSheet
import org.kotlang.freelancerfinance.presentation.create_invoice.component.ServiceSelectionSheet
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
import org.kotlang.freelancerfinance.presentation.design_system.button.DashedOutlinedButton
import org.kotlang.freelancerfinance.presentation.design_system.button.dashedBorder
import org.kotlang.freelancerfinance.presentation.design_system.dialog.StandardDatePickerDialog
import org.kotlang.freelancerfinance.presentation.util.ObserveAsEvents
import org.kotlang.freelancerfinance.presentation.util.toFormattedDateString
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CreateInvoiceScreenRoot(
    viewModel: CreateInvoiceViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    onNavigateToAddClient: () -> Unit,
    onNavigateToEditClient: (Long) -> Unit,
    onNavigateToAddService: () -> Unit,
    onNavigateToEditService: (Long) -> Unit,
    onNavigateToPreviewInvoice: (Long) -> Unit
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is CreateInvoiceEvent.NavigateBack -> onNavigateBack()
            is CreateInvoiceEvent.NavigateToAddClient -> onNavigateToAddClient()
            is CreateInvoiceEvent.NavigateToEditClient -> onNavigateToEditClient(event.clientId)
            CreateInvoiceEvent.NavigateToAddService -> onNavigateToAddService()
            is CreateInvoiceEvent.NavigateToEditService -> onNavigateToEditService(event.serviceId)
            is CreateInvoiceEvent.NavigateToPreviewInvoice -> onNavigateToPreviewInvoice(event.invoiceId)

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

    if (state.showClientSelectionSheet) {
        ClientSelectionSheet(
            clients = state.availableClients,
            onDismissRequest = { onAction(CreateInvoiceUiAction.OnDismissClientSheet) },
            onClientSelected = { onAction(CreateInvoiceUiAction.OnClientSelected(it)) },
            onAddNewClientClick = { onAction(CreateInvoiceUiAction.OnAddNewClientClick) },
            onEditClientClick = { onAction(CreateInvoiceUiAction.OnEditClientClick(it)) }
        )
    }

    if (state.showServiceSelectionSheet) {
        ServiceSelectionSheet(
            services = state.availableServices,
            onDismissRequest = { onAction(CreateInvoiceUiAction.OnDismissServiceSheet) },
            onServiceSelected = { onAction(CreateInvoiceUiAction.OnServiceSelected(it)) },
            onAddNewServiceClick = { onAction(CreateInvoiceUiAction.OnAddNewServiceItemClick) },
            onEditServiceClick = { onAction(CreateInvoiceUiAction.OnEditServiceItemClick(it)) }
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
                .widthIn(max = 700.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 70.dp)
        ) {
            InvoiceHeaderSection(
                invoiceNumber = state.invoiceNumber,
                issueDate = state.issueDate.toFormattedDateString(),
                dueDate = state.dueDate.toFormattedDateString(),
                onIssueDateClick = {
                    onAction(CreateInvoiceUiAction.OnDateFieldClick(DatePickerType.IssueDate))
                },
                onDueDateClick = {
                    onAction(CreateInvoiceUiAction.OnDateFieldClick(DatePickerType.DueDate))
                },
                onInvoiceNumberChange = {
                    onAction(CreateInvoiceUiAction.OnInvoiceNumberChange(it))
                }
            )

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
                onEditClientClick = { onAction(CreateInvoiceUiAction.OnEditClientClick(it)) },
                onClick = { onAction(CreateInvoiceUiAction.OnAddClientClick) }
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
                    text = "${state.lineItems.size} items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            state.lineItems.forEach { item ->
                Spacer(modifier = Modifier.height(12.dp))
                InvoiceItemCard(
                    item = item,
                    onEditClick = { },
                    onRemoveClick = { onAction(CreateInvoiceUiAction.OnRemoveServiceItemClick(it)) }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            DashedOutlinedButton(
                text = "Add Item",
                onClick = { onAction(CreateInvoiceUiAction.OnAddServiceItemClick) }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 6. Calculations
            CalculationRow(label = "Subtotal", amount = state.subtotal)
            CalculationRow(label = "Add Discount", amount = 0.0, isLink = true)
            CalculationRow(label = "Tax (18%)", amount = state.totalTax)

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
                    text = formatCurrency(state.grandTotal),
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
            grandTotal = state.grandTotal,
            isSaveEnabled = state.isSaveEnabled,
            onSaveClick = { onAction(CreateInvoiceUiAction.OnSaveAndPreviewClick) }
        )
    }
}

// --- Components ---

@Composable
fun InvoiceBottomBar(
    grandTotal: Double,
    isSaveEnabled: Boolean,
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
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
                modifier = Modifier.height(50.dp),
                onClick = onSaveClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = isSaveEnabled
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
    onEditClientClick: (id: Long) -> Unit,
    onClick: () -> Unit
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
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = selectedClient) { client ->
            if (client != null) {
                SelectedClientCard(
                    client = client,
                    onEditClick = { onEditClientClick(client.id) }
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
private fun InvoiceItemCard(
    item: InvoiceLineItemUi,
    onEditClick: (serviceId: Long) -> Unit,
    onRemoveClick: (internalId: String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )
                Text(
                    text = "₹${item.totalAmount}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (!item.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(
                        text = "${item.quantity} x ₹${item.unitPrice}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "+ ${item.taxRate}% Tax",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = { onRemoveClick(item.internalId) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Remove")
                }

                TextButton(
                    onClick = { item.serviceId?.let { onEditClick(it) } }
                ) {
                    Text("Edit", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun InvoiceHeaderSection(
    invoiceNumber: String,
    issueDate: String,
    dueDate: String,
    onIssueDateClick: () -> Unit,
    onDueDateClick: () -> Unit,
    onInvoiceNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            InputLabel("Invoice No.")
            CompactInvoiceNumberField(
                value = invoiceNumber,
                onValueChange = onInvoiceNumberChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DateSelectorField(
                    label = "Issue Date",
                    value = issueDate,
                    iconResId = Res.drawable.ic_calendar_today,
                    onClick = onIssueDateClick,
                    modifier = Modifier.weight(1f)
                )

                DateSelectorField(
                    label = "Due Date",
                    value = dueDate,
                    iconResId = Res.drawable.ic_calender_event,
                    onClick = onDueDateClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CompactInvoiceNumberField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh) // Light Gray Background
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_tag),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(modifier = Modifier.weight(1f)) {
                    innerTextField()
                }
            }
        }
    )
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

@Composable
private fun DateSelectorField(
    label: String,
    value: String,
    iconResId: DrawableResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        InputLabel(label)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh) // Same Gray Background
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun InputLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 6.dp)
    )
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