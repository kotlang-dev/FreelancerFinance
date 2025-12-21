package org.kotlang.freelancerfinance.domain.model

import org.kotlang.freelancerfinance.presentation.util.getInitials
import org.kotlang.freelancerfinance.presentation.util.toIndianCurrency

data class ServiceItem(
    val id: Long = 0,
    val name: String,
    val defaultPrice: Double,
    val taxRate: Double,
    val description: String? = null,
    val hsnSacCode: String? = null
) {
    val initials: String
        get() = name.getInitials()

    val formattedPrice: String
        get() = defaultPrice.toIndianCurrency()

    val taxDisplay: String
        get() = "${taxRate.toString().removeSuffix(".0")}% GST"
}