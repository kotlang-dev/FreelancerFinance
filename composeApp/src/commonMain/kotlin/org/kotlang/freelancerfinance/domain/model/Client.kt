package org.kotlang.freelancerfinance.domain.model

data class Client(
    val id: Long = 0,
    val name: String,
    val gstin: String?,
    val address: String,
    val state: IndianState
)