package org.kotlang.freelancerfinance.presentation.profile

import org.kotlang.freelancerfinance.domain.model.IndianState

sealed interface ProfileUiAction {
    data class UpdateBusinessName(val name: String) : ProfileUiAction
    data class UpdatePanNumber(val pan: String) : ProfileUiAction
    data class UpdateGstin(val gstin: String) : ProfileUiAction
    data class UpdateAddressLine1(val value: String) : ProfileUiAction
    data class UpdateAddressLine2(val value: String) : ProfileUiAction
    data class UpdateState(val state: IndianState) : ProfileUiAction
    data class UpdatePincode(val pincode: String) : ProfileUiAction
    data object SaveProfile : ProfileUiAction
    data class UpdateLogo(val bytes: ByteArray) : ProfileUiAction
}