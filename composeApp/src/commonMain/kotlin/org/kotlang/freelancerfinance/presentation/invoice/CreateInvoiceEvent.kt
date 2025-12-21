package org.kotlang.freelancerfinance.presentation.invoice

sealed interface CreateInvoiceEvent {
    data object NavigateBack : CreateInvoiceEvent
    data object NavigateToAddClient : CreateInvoiceEvent
    data class NavigateToEditClient(val clientId: Long) : CreateInvoiceEvent

    data class ShowSnackbar(val message: String) : CreateInvoiceEvent
}