package org.kotlang.freelancerfinance.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Dashboard : Route
    
    @Serializable
    data object Profile : Route

    @Serializable
    data object ClientList : Route

    @Serializable
    data object CreateInvoice : Route
}