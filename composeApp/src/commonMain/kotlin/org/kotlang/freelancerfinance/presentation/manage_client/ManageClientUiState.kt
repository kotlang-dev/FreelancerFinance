package org.kotlang.freelancerfinance.presentation.manage_client

import org.kotlang.freelancerfinance.domain.model.Client

data class ManageClientUiState(
    val isLoading: Boolean = true,
    val filteredClients: List<Client> = emptyList(),
    val searchQuery: String = ""
) {
    val showEmptyState: Boolean
        get() = !isLoading && filteredClients.isEmpty()

    val showContent: Boolean
        get() = !isLoading && filteredClients.isNotEmpty()

    val showFab: Boolean
        get() = showContent
}