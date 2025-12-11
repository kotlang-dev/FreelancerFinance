package org.kotlang.freelancerfinance.presentation.dashboard

data class DashboardUiState(
    val companyName: String = "Freelancer",
    val logoPath: String? = null,
    val totalInvoicedValue: Double = 0.0,
    val recentInvoices: List<Invoice> = emptyList(),
    val isLoading: Boolean = false
)