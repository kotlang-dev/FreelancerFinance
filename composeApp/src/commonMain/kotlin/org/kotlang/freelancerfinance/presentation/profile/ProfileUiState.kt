package org.kotlang.freelancerfinance.presentation.profile

import org.kotlang.freelancerfinance.domain.model.BusinessProfile
import org.kotlang.freelancerfinance.domain.model.IndianState

data class ProfileUiState(
    val businessName: String = "",
    val panNumber: String = "",
    val gstin: String = "",
    val address: String = "",
    val city: String = "Mumbai",
    val pincode: String = "400001",
    val selectedState: IndianState = IndianState.DELHI,
    val isLoading: Boolean = false,
    val savedProfile: BusinessProfile? = null
)