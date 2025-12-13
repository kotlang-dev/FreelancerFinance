package org.kotlang.freelancerfinance.presentation.add_edit_client

import org.kotlang.freelancerfinance.domain.model.IndianState

sealed interface AddEditClientUiAction {
    data object OnGoBackClick : AddEditClientUiAction
    data object OnSaveClick : AddEditClientUiAction
    data object OnDeleteClick : AddEditClientUiAction

    // Field Updates
    data class OnNameChange(val value: String) : AddEditClientUiAction
    data class OnGstinChange(val value: String) : AddEditClientUiAction
    data class OnAddressChange(val value: String) : AddEditClientUiAction
    data class OnStateChange(val state: IndianState) : AddEditClientUiAction
}