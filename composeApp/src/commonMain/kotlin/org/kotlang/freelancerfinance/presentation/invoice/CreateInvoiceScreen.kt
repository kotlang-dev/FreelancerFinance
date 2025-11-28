package org.kotlang.freelancerfinance.presentation.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_arrow_back
import freelancerfinance.composeapp.generated.resources.ic_delete
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoiceScreen(
    viewModel: InvoiceViewModel,
    onFinished: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val draftState by viewModel.draftState.collectAsStateWithLifecycle()
    val mainState by viewModel.uiState.collectAsStateWithLifecycle()
    val isGeneratingPdf by viewModel.isGeneratingPdf.collectAsState()

    // Local State for "Add Item" inputs
    var desc by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("1") }
    var price by remember { mutableStateOf("") }
    
    // Client Selector Dialog
    var showClientDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is InvoiceUiEffect.NavigateBack -> {
                    onFinished()
                }
                is InvoiceUiEffect.ShowError -> {
                    // Show snackbar logic here
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text(text = "New Invoice") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = "Navigate Back"
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Client Selector
        Card(
            modifier = Modifier.fillMaxWidth().clickable { showClientDialog = true }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Bill To:", style = MaterialTheme.typography.bodySmall)
                if (draftState.selectedClient != null) {
                    Text(draftState.selectedClient!!.name, style = MaterialTheme.typography.labelMedium)
                    Text("GSTIN: ${draftState.selectedClient?.gstin ?: "N/A"}")
                } else {
                    Text("Select Client +", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Items List
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(draftState.items) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.description, fontWeight = FontWeight.Bold)
                        Text("${item.quantity} x ₹${item.unitPrice} (+${item.taxRate}%)")
                    }
                    Text("₹${item.amount}", fontWeight = FontWeight.Bold)
                    IconButton(onClick = { viewModel.onAction(InvoiceUiAction.RemoveItem(item)) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_delete),
                            contentDescription = "Remove",
                            tint = Color.Red
                        )
                    }
                }
                HorizontalDivider()
            }
        }

        // 3. Add Item Form (Mini)
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = qty,
                        onValueChange = { qty = it },
                        label = { Text("Qty") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Button(
                    onClick = {
                        if (desc.isNotBlank() && price.isNotBlank()) {
                            viewModel.onAction(InvoiceUiAction.AddItem(desc, qty, price, "18.0"))
                            desc = ""
                            price = ""
                            qty = "1"
                        }
                    },
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
                ) {
                    Text("Add Item")
                }
            }
        }

        // 4. Totals & Save
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("Subtotal:"); Text("₹${draftState.subTotal}") }
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("Tax:"); Text("₹${draftState.totalTax}") }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { 
                    Text("Total:", fontWeight = FontWeight.Bold); 
                    Text("₹${draftState.grandTotal}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        viewModel.onAction(InvoiceUiAction.SaveInvoice)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = draftState.selectedClient != null &&
                            draftState.items.isNotEmpty() &&
                            !isGeneratingPdf
                ) {
                    if (isGeneratingPdf) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Text("Generating PDF...")
                    } else {
                        Text("Save Invoice")
                    }
                }
            }
        }
    }
    // Client Selection Dialog
    if (showClientDialog) {
        AlertDialog(
            onDismissRequest = { showClientDialog = false },
            title = { Text("Select Client") },
            text = {
                LazyColumn {
                    items(mainState.clients) { client ->
                        Text(
                            text = client.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onAction(InvoiceUiAction.SelectClient(client))
                                    showClientDialog = false
                                }
                                .padding(16.dp)
                        )
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showClientDialog = false }) { Text("Cancel") } }
        )
    }
}