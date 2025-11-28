package org.kotlang.freelancerfinance.presentation.invoice

sealed interface InvoiceUiEffect {
    data object NavigateBack : InvoiceUiEffect
    data class ShowError(val message: String) : InvoiceUiEffect
}