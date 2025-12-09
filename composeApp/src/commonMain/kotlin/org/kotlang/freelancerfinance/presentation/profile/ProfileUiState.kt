package org.kotlang.freelancerfinance.presentation.profile

import org.kotlang.freelancerfinance.domain.model.BusinessProfile
import org.kotlang.freelancerfinance.domain.model.IndianState

data class ProfileUiState(
    val originalProfile: BusinessProfile? = null,
    val businessName: String = "",
    val panNumber: String = "",
    val gstin: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val pincode: String = "",
    val selectedState: IndianState = IndianState.DELHI,
    val logoPath: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val nameError: String? = null,
    val panError: String? = null,
    val gstinError: String? = null,
    val addressError: String? = null,
    val pincodeError: String? = null,
) {
    val isSaveEnabled: Boolean
        get() {
            // Case A: First time creating profile (No original exists)
            if (originalProfile == null) {
                return businessName.isNotBlank()
            }

            // Case B: Editing existing profile
            val currentProfile = BusinessProfile(
                businessName = businessName,
                panNumber = panNumber,
                gstin = gstin.ifBlank { null },
                addressLine1 = addressLine1,
                addressLine2 = addressLine2,
                pincode = pincode,
                state = selectedState,
                logoPath = logoPath
            )

            val hasErrors = nameError != null || panError != null || gstinError != null || pincodeError != null
            if (hasErrors) return false

            val mandatoryFilled = businessName.isNotBlank() && panNumber.isNotBlank() && addressLine1.isNotBlank() && pincode.isNotBlank()
            if (!mandatoryFilled) return false

            return currentProfile != originalProfile
        }
}