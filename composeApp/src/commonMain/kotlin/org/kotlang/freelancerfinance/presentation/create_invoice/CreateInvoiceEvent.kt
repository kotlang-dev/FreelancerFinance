package org.kotlang.freelancerfinance.presentation.create_invoice

sealed interface CreateInvoiceEvent {
    data object NavigateBack : CreateInvoiceEvent
    data object NavigateToAddClient : CreateInvoiceEvent
    data class NavigateToEditClient(val clientId: Long) : CreateInvoiceEvent
    data object NavigateToAddService : CreateInvoiceEvent
    data class NavigateToEditService(val serviceId: Long) : CreateInvoiceEvent
    data class NavigateToPreviewInvoice(val invoiceId: Long) : CreateInvoiceEvent

    data class ShowSnackbar(val message: String) : CreateInvoiceEvent
}