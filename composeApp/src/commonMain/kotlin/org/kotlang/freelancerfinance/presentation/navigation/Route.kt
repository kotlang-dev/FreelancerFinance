package org.kotlang.freelancerfinance.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Dashboard : Route
    
    @Serializable
    data object Profile : Route

    @Serializable
    data object ManageClients : Route

    @Serializable
    data class AddEditClient(val clientId: Long?) : Route

    @Serializable
    data object ManageServices : Route

    @Serializable
    data class AddEditService(val serviceId: Long?) : Route

    @Serializable
    data object CreateInvoice : Route
}