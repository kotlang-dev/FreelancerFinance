package org.kotlang.freelancerfinance.presentation.create_invoice

import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.ServiceItem
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class CreateInvoiceUiState (
    val invoiceNumber: String = "",
    val issueDate: Long = Clock.System.now().toEpochMilliseconds(),
    val dueDate: Long = Clock.System.now().toEpochMilliseconds() + 604_800_000L,

    val availableClients: List<Client> = emptyList(),
    val selectedClient: Client? = null,
    val clientError: String? = null,

    val availableServices: List<ServiceItem> = emptyList(),

    val lineItems: List<InvoiceLineItemUi> = emptyList(),
    val itemsError: String? = null,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val showAddServiceSheet: Boolean = false,
    val showClientSelectionSheet: Boolean = false,
    val showServiceSelectionSheet: Boolean = false,
    val activeDatePicker: DatePickerType = DatePickerType.None
) {

    val subtotal: Double
        get() = lineItems.sumOf { it.quantity * it.unitPrice }

    val totalTax: Double
        get() = lineItems.sumOf { it.taxAmount }

    val grandTotal: Double
        get() = subtotal + totalTax


    val isSaveEnabled: Boolean
        get() = !isLoading && !isSaving && selectedClient != null && lineItems.isNotEmpty()
}

enum class DatePickerType {
    None, IssueDate, DueDate
}

data class InvoiceLineItemUi(
    val internalId: String = UUID.randomUUID().toString(),
    val serviceId: Long? = null,
    val name: String,
    val description: String? = null,
    val quantity: Double = 1.0,
    val unitPrice: Double,
    val taxRate: Double,
) {
    val taxAmount: Double
        get() = (quantity * unitPrice) * (taxRate / 100.0)

    val totalAmount: Double
        get() = (quantity * unitPrice) + taxAmount
}