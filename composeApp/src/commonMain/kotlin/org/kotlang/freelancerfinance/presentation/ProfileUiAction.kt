package org.kotlang.freelancerfinance.presentation

import org.kotlang.freelancerfinance.domain.model.IndianState

sealed interface ProfileUiAction {
    data class UpdateBusinessName(val name: String) : ProfileUiAction
    data class UpdatePanNumber(val pan: String) : ProfileUiAction
    data class UpdateGstin(val gstin: String) : ProfileUiAction
    data class UpdateAddress(val address: String) : ProfileUiAction
    data class UpdateState(val state: IndianState) : ProfileUiAction
    data object SaveProfile : ProfileUiAction
}