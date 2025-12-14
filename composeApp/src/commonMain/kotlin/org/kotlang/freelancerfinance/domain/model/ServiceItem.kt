package org.kotlang.freelancerfinance.domain.model

data class ServiceItem(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val defaultPrice: Double,
    val taxRate: Double,
    val hsnSacCode: String?
)