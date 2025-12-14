package org.kotlang.freelancerfinance.presentation.manage_services

data class ManageServicesUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val filteredServices: List<ServiceListItemUi> = emptyList()
) {
    val showEmptyState: Boolean
        get() = !isLoading && filteredServices.isEmpty() && searchQuery.isBlank()

    val showContent: Boolean
        get() = !isLoading && filteredServices.isNotEmpty()

    val showFab: Boolean
        get() = !isLoading
}

data class ServiceListItemUi(
    val id: Long,
    val name: String,
    val initials: String,
    val formattedPrice: String,
    val taxRate: String
)