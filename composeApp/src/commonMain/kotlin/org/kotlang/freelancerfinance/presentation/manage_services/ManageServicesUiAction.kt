package org.kotlang.freelancerfinance.presentation.manage_services

sealed interface ManageServicesUiAction {
    data object OnGoBackClick : ManageServicesUiAction
    data class OnSearchQueryChange(val query: String) : ManageServicesUiAction
    data class OnAddEditServiceClick(val serviceId: Long?) : ManageServicesUiAction
}