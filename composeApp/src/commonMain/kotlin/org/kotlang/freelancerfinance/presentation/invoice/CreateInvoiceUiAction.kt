package org.kotlang.freelancerfinance.presentation.invoice

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

    data object OnAddClientClick : CreateInvoiceUiAction
    data class OnClientSelected(val client: Client) : CreateInvoiceUiAction
    data object OnDismissClientSheet : CreateInvoiceUiAction
    data object OnAddNewClientClick : CreateInvoiceUiAction
    data class OnEditClientClick(val clientId: Long) : CreateInvoiceUiAction

    // --- Line Items Section ---
    data object OnAddLineItemClick : CreateInvoiceUiAction // Opens Service Sheet
    data object OnDismissServiceSheet : CreateInvoiceUiAction

    // When user picks a saved service from the sheet
    data class OnServiceSelected(val service: ServiceItem) : CreateInvoiceUiAction

    // Managing the list
    data class OnRemoveLineItem(val internalId: String) : CreateInvoiceUiAction
    data class OnQuantityChange(val internalId: String, val newQuantity: Double) : CreateInvoiceUiAction

    // --- Footer ---
    data object OnSaveAndPreviewClick : CreateInvoiceUiAction
}