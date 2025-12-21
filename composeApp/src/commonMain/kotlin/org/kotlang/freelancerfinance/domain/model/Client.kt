package org.kotlang.freelancerfinance.domain.model

import org.kotlang.freelancerfinance.presentation.util.getInitials

data class Client(
    val id: Long = 0,
    val name: String,
    val gstin: String?,
    val address: String,
    val state: IndianState
) {
    val initials: String
        get() = name.getInitials()

    val status: ClientStatus
        get() = if (!gstin.isNullOrBlank()) ClientStatus.REGISTERED else ClientStatus.UNREGISTERED

    val locationShort: String
        get() {
            val flatAddress = address.replace("\n", ", ").trim()

            val maxLength = 25
            val addressPart = if (flatAddress.length > maxLength) {
                "${flatAddress.take(maxLength)}..."
            } else flatAddress

            return if (addressPart.isBlank()) {
                state.stateName
            } else {
                "$addressPart, ${state.stateName}"
            }
        }

    val fullAddressDisplay: String
        get() = "$address\n${state.stateName}"
}

enum class ClientStatus { REGISTERED, UNREGISTERED }