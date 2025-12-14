package org.kotlang.freelancerfinance.presentation.dashboard

sealed interface DashboardUiAction {
    data object OnManageClientsClick : DashboardUiAction
    data object OnManageServicesClick : DashboardUiAction
    data object OnEditProfileClick : DashboardUiAction
    data object OnCreateInvoiceClick : DashboardUiAction
}