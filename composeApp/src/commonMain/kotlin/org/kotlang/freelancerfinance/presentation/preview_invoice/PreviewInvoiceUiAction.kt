package org.kotlang.freelancerfinance.presentation.preview_invoice

sealed interface PreviewInvoiceUiAction {
    data object OnRetryClick : PreviewInvoiceUiAction
    data object OnShareClick : PreviewInvoiceUiAction
    data object OnBackClick : PreviewInvoiceUiAction
}