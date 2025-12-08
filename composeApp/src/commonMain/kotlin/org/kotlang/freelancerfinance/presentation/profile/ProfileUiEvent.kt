package org.kotlang.freelancerfinance.presentation.profile

sealed interface ProfileUiEvent {
    data class ShowSnackbar(val message: String) : ProfileUiEvent
}