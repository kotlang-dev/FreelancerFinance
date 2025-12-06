package org.kotlang.freelancerfinance.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BusinessProfile(
    val businessName: String,
    val panNumber: String,
    val gstin: String? = null,
    val address: String,
    val city: String,
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
}