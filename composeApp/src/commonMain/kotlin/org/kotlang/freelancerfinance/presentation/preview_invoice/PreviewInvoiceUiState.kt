package org.kotlang.freelancerfinance.presentation.preview_invoice

data class PreviewInvoiceUiState(
    val isLoading: Boolean = false,
    val pdfPath: String? = null,
    val invoiceNumber: String? = null,
    val error: String? = null
)