package org.kotlang.freelancerfinance.presentation.invoice

import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.model.InvoiceLineItem

data class InvoiceUiState(
    val invoices: List<Invoice> = emptyList(),
    val clients: List<Client> = emptyList(),
    val isLoading: Boolean = true
)

// State for the "Create Invoice" form
data class DraftInvoiceState(
    val selectedClient: Client? = null,
    val items: List<InvoiceLineItem> = emptyList(),
    val invoiceNumber: String = "INV-${System.currentTimeMillis() % 10000}",
    val subTotal: Double = 0.0,
    val totalTax: Double = 0.0,
    val grandTotal: Double = 0.0
)