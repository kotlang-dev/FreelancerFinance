package org.kotlang.freelancerfinance.presentation.add_edit_service

data class AddEditServiceUiState(
    val serviceId: Long? = null,
    
    val name: String = "",
    val price: String = "",
    val taxRate: String = "18.0",
    val hsnCode: String = "",
    val description: String = "",

    val nameError: String? = null,
    val priceError: String? = null,

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,

    val initialName: String = "",
    val initialPrice: String = "",
    val initialTaxRate: String = "18.0",
    val initialHsnCode: String = "",
    val initialDescription: String = ""
) {
    val isEditMode: Boolean
        get() = serviceId != null

    val isSaveEnabled: Boolean
        get() {
            if (isLoading || isSaving) return false

            val mandatoryFilled = name.isNotBlank() &&
                    price.isNotBlank() &&
                    price.toDoubleOrNull() != null
            if (!mandatoryFilled) return false

            val hasNoErrors = nameError == null &&
                    priceError == null
            if (!hasNoErrors) return false

            val hasChanges = name != initialName ||
                    price != initialPrice ||
                    taxRate != initialTaxRate ||
                    hsnCode != initialHsnCode ||
                    description != initialDescription

            return hasChanges
        }
}