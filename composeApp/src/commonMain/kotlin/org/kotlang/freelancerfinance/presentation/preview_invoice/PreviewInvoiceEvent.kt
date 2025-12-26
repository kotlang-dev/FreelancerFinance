package org.kotlang.freelancerfinance.presentation.preview_invoice

sealed interface PreviewInvoiceEvent {
    data class SharePdf(val path: String) : PreviewInvoiceEvent
    data object NavigateBack : PreviewInvoiceEvent
}