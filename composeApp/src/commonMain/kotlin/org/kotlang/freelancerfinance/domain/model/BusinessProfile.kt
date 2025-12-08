package org.kotlang.freelancerfinance.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessProfile(
    val businessName: String,
    val panNumber: String,
    val gstin: String? = null,
    val addressLine1: String,
    val addressLine2: String,
    val pincode: String,
    val state: IndianState,
    val logoPath: String? = null
) {

    val isRegisteredDealer: Boolean
        get() = !gstin.isNullOrBlank()

    fun isGstValid(): Boolean {
        if (gstin == null) return true
        if (gstin.length != 15) return false

        val stateCodeFromGst = gstin.take(2).toIntOrNull() ?: return false
        return stateCodeFromGst == state.code
    }

    val fullAddress: String
        get() = buildString {
            append(addressLine1)
            if (addressLine2.isNotBlank()) append(", $addressLine2")
            append(" - $pincode")
        }
}