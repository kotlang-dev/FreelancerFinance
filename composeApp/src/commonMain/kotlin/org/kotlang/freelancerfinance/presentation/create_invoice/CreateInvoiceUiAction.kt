package org.kotlang.freelancerfinance.presentation.create_invoice

import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.ServiceItem

sealed interface CreateInvoiceUiAction {
    // --- Navigation ---
    data object OnGoBackClick : CreateInvoiceUiAction

    // --- Metadata Section ---
    data class OnInvoiceNumberChange(val number: String) : CreateInvoiceUiAction

    data class OnDateFieldClick(val type: DatePickerType) : CreateInvoiceUiAction

    // Date Selected (from Dialog)
    data class OnDateSelected(val dateMillis: Long?) : CreateInvoiceUiAction

    // --- Client Section ---
    data object OnAddClientClick : CreateInvoiceUiAction
    data class OnClientSelected(val client: Client) : CreateInvoiceUiAction
    data object OnDismissClientSheet : CreateInvoiceUiAction
    data object OnAddNewClientClick : CreateInvoiceUiAction
    data class OnEditClientClick(val clientId: Long) : CreateInvoiceUiAction

    // --- Service Items Section ---
    data object OnAddServiceItemClick : CreateInvoiceUiAction
    data class OnServiceSelected(val service: ServiceItem) : CreateInvoiceUiAction
    data object OnDismissServiceSheet : CreateInvoiceUiAction
    data object OnAddNewServiceItemClick : CreateInvoiceUiAction
    data class OnEditServiceItemClick(val serviceId: Long) : CreateInvoiceUiAction
    data class OnRemoveServiceItemClick(val internalId: String) : CreateInvoiceUiAction
    data class OnQuantityChange(val internalId: String, val newQuantity: Double) : CreateInvoiceUiAction

    // --- Footer ---
    data object OnSaveAndPreviewClick : CreateInvoiceUiAction
}