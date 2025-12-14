package org.kotlang.freelancerfinance.presentation.add_edit_service

sealed interface AddEditServiceUiAction {
    data object OnGoBackClick : AddEditServiceUiAction
    data object OnSaveClick : AddEditServiceUiAction
    data object OnDeleteClick : AddEditServiceUiAction
    data class OnNameChange(val value: String) : AddEditServiceUiAction
    data class OnPriceChange(val value: String) : AddEditServiceUiAction
    data class OnTaxRateChange(val value: String) : AddEditServiceUiAction
    data class OnHsnCodeChange(val value: String) : AddEditServiceUiAction
    data class OnDescriptionChange(val value: String) : AddEditServiceUiAction
}