package org.kotlang.freelancerfinance.presentation.add_edit_client

import org.kotlang.freelancerfinance.domain.model.IndianState

data class AddEditClientUiState(
    val clientId: Long? = null,

    val name: String = "",
    val gstin: String = "",
    val address: String = "",
    val state: IndianState = IndianState.DELHI,

    val nameError: String? = null,
    val gstinError: String? = null,
    val addressError: String? = null,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,

    val initialName: String = "",
    val initialGstin: String = "",
    val initialAddress: String = "",
    val initialState: IndianState? = null,
) {
    val isEditMode: Boolean
        get() = clientId != null

    val isSaveEnabled: Boolean
        get() {
            if (isLoading || isSaving) return false

            val mandatoryFilled = name.isNotBlank() && address.isNotBlank()
            if (!mandatoryFilled) return false

            val hasNoErrors = nameError == null &&
                    gstinError == null &&
                    addressError == null
            if (!hasNoErrors) return false

            val hasChanges = name != initialName ||
                    gstin != initialGstin ||
                    address != initialAddress ||
                    state != initialState

            return hasChanges
        }
}