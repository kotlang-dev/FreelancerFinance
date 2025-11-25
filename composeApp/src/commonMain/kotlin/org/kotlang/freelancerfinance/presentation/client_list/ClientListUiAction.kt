package org.kotlang.freelancerfinance.presentation.client_list

import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.IndianState

sealed interface ClientListUiAction {
    data class DeleteClient(val client: Client) : ClientListUiAction
    data class AddClient(
        val name: String,
        val gstin: String?,
        val address: String,
        val state: IndianState,
        val email: String?
    ) : ClientListUiAction
}