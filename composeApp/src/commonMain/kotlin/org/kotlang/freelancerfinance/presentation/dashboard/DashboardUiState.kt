package org.kotlang.freelancerfinance.presentation.dashboard

import org.kotlang.freelancerfinance.domain.model.InvoiceSummary

data class DashboardUiState(
    val companyName: String = "Freelancer",
    val logoPath: String? = null,
    val totalInvoicedValue: Double = 0.0,
    val recentInvoices: List<InvoiceSummary> = emptyList(),
    val isLoading: Boolean = false
) {
    val showEmptyState: Boolean
        get() = !isLoading && recentInvoices.isEmpty()

    val showContent: Boolean
        get() = recentInvoices.isNotEmpty()
}