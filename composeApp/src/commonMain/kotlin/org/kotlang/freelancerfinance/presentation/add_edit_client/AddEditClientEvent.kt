package org.kotlang.freelancerfinance.presentation.add_edit_client

sealed interface AddEditClientEvent {
    data object NavigateBack : AddEditClientEvent
    data class ShowSnackbar(val message: String) : AddEditClientEvent
}