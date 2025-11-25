package org.kotlang.freelancerfinance.presentation.client_list

import org.kotlang.freelancerfinance.domain.model.Client

data class ClientListUiState(
    val clients: List<Client> = emptyList(),
    val isLoading: Boolean = true
)