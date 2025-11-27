package org.kotlang.freelancerfinance.presentation.invoice

import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.InvoiceLineItem

sealed interface InvoiceUiAction {
    data class SelectClient(val client: Client) : InvoiceUiAction
    data class AddItem(val desc: String, val qty: String, val price: String, val taxRate: String) : InvoiceUiAction
    data class RemoveItem(val item: InvoiceLineItem) : InvoiceUiAction
    data object SaveInvoice : InvoiceUiAction
    data object ResetDraft : InvoiceUiAction
}