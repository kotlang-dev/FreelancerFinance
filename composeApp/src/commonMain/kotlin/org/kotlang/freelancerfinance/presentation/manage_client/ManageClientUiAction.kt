package org.kotlang.freelancerfinance.presentation.manage_client

sealed interface ManageClientUiAction {
    data object OnGoBackClick : ManageClientUiAction
    data class OnSearchQueryChange(val newQuery: String) : ManageClientUiAction
    data class OnAddEditClientClick(val clientId: Long?) : ManageClientUiAction
}