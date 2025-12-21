package org.kotlang.freelancerfinance.presentation.manage_services

import org.kotlang.freelancerfinance.domain.model.ServiceItem

data class ManageServicesUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val filteredServices: List<ServiceItem> = emptyList()
) {
    val showEmptyState: Boolean
        get() = !isLoading && filteredServices.isEmpty() && searchQuery.isBlank()

    val showContent: Boolean
        get() = !isLoading && filteredServices.isNotEmpty()

    val showFab: Boolean
        get() = !isLoading
}