package org.kotlang.freelancerfinance.presentation.add_edit_service

sealed interface AddEditServiceEvent {
    data object NavigateBack : AddEditServiceEvent
    data class ShowSnackbar(val message: String) : AddEditServiceEvent
}