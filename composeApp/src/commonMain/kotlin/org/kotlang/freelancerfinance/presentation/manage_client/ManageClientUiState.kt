package org.kotlang.freelancerfinance.presentation.manage_client

data class ManageClientUiState(
    val isLoading: Boolean = true,
    val filteredClients: List<ClientListItemUi> = emptyList(),
    val searchQuery: String = ""
) {
    val showEmptyState: Boolean
        get() = !isLoading && filteredClients.isEmpty()

    val showContent: Boolean
        get() = !isLoading && filteredClients.isNotEmpty()

    val showFab: Boolean
        get() = showContent
}

data class ClientListItemUi(
    val id: Long,
    val name: String,
    val locationShort: String,
    val status: ClientStatus,
    val initials: String
)

enum class ClientStatus { GSTIN, UNREGISTERED, PENDING }