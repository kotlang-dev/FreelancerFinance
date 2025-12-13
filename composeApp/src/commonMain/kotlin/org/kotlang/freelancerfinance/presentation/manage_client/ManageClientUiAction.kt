package org.kotlang.freelancerfinance.presentation.manage_client

import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.IndianState

sealed interface ManageClientUiAction {
    data object OnGoBackClick : ManageClientUiAction
    data object OnAddClientClick : ManageClientUiAction
    data class OnSearchQueryChange(val newQuery: String) : ManageClientUiAction
    data class OnEditClientClick(val clientId: Long) : ManageClientUiAction
    data class OnDeleteClientClick(val clientId: Long) : ManageClientUiAction
    data class DeleteClient(val client: Client) : ManageClientUiAction
    data class AddClient(
        val name: String,
        val gstin: String?,
        val address: String,
        val state: IndianState,
        val email: String?
    ) : ManageClientUiAction
}