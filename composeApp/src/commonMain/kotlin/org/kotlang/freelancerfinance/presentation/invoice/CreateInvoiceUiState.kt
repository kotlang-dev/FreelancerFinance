package org.kotlang.freelancerfinance.presentation.invoice

import org.kotlang.freelancerfinance.domain.model.Client
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class CreateInvoiceUiState (

    val availableClients: List<Client> = emptyList(),
    // --- Metadata ---
    val invoiceNumber: String = "", // e.g. "INV-001"
    val issueDate: Long = Clock.System.now().toEpochMilliseconds(),
    val dueDate: Long = Clock.System.now().toEpochMilliseconds() + 604_800_000L,

    val selectedClient: Client? = null,
    val clientError: String? = null, // e.g., "Please select a client"

    // --- Line Items ---
    val lineItems: List<InvoiceLineItemUi> = emptyList(),
    val itemsError: String? = null, // e.g., "Add at least one item"

    // --- UI Controls ---
    val isLoading: Boolean = false,
    val isSaving: Boolean = false, // Shows loader on "Save" button
    val showAddServiceSheet: Boolean = false,
    val showClientSelectionSheet: Boolean = false,
    val activeDatePicker: DatePickerType = DatePickerType.None
) {

    val subtotal: Double
        get() = lineItems.sumOf { it.quantity * it.unitPrice }

    val totalTax: Double
        get() = lineItems.sumOf { it.taxAmount }

    val grandTotal: Double
        get() = subtotal + totalTax

    // --- Form Logic ---

    // Save is enabled if we aren't loading, have a client, and have items
    val isSaveEnabled: Boolean
        get() = !isLoading && !isSaving && selectedClient != null && lineItems.isNotEmpty()
}

// Helper Enum for Date Pickers
enum class DatePickerType {
    None, IssueDate, DueDate
}

data class InvoiceLineItemUi(
    val internalId: String = UUID.randomUUID().toString(),
    val serviceId: Long? = null,
    val name: String,
    val quantity: Double = 1.0,
    val unitPrice: Double,
    val taxRate: Double,
) {
    val taxAmount: Double
        get() = (quantity * unitPrice) * (taxRate / 100.0)

    val totalAmount: Double
        get() = (quantity * unitPrice) + taxAmount
}