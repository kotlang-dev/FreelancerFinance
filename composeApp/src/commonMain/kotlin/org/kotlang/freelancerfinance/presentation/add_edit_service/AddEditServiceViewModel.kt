package org.kotlang.freelancerfinance.presentation.add_edit_service

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.model.ServiceItem
import org.kotlang.freelancerfinance.domain.repository.ServiceItemRepository
import org.kotlang.freelancerfinance.presentation.navigation.Route

class AddEditServiceViewModel(
    private val repository: ServiceItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val serviceId: Long? = savedStateHandle.toRoute<Route.AddEditService>().serviceId

    private val _state = MutableStateFlow(AddEditServiceUiState(serviceId = serviceId))
    val state: StateFlow<AddEditServiceUiState> = _state.asStateFlow()

    private val _uiEvent = Channel<AddEditServiceEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        serviceId?.let { loadService(it) }
    }

    fun onAction(action: AddEditServiceUiAction) {
        when (action) {
            is AddEditServiceUiAction.OnNameChange -> {
                _state.update { it.copy(name = action.value, nameError = null) }
            }
            is AddEditServiceUiAction.OnPriceChange -> {
                // validate strictly numbers or decimals
                _state.update { it.copy(price = action.value, priceError = null) }
            }
            is AddEditServiceUiAction.OnTaxRateChange -> {
                _state.update { it.copy(taxRate = action.value) }
            }
            is AddEditServiceUiAction.OnHsnCodeChange -> {
                _state.update { it.copy(hsnCode = action.value) }
            }
            is AddEditServiceUiAction.OnDescriptionChange -> {
                _state.update { it.copy(description = action.value) }
            }
            AddEditServiceUiAction.OnSaveClick -> saveService()
            AddEditServiceUiAction.OnDeleteClick -> deleteService()
            AddEditServiceUiAction.OnGoBackClick -> Unit
        }
    }

    private fun loadService(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val service = repository.getServiceItemById(id)
            
            if (service != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        // Set Current State
                        name = service.name,
                        price = service.defaultPrice.toString().removeSuffix(".0"), // Clean formatting
                        taxRate = service.taxRate.toString().removeSuffix(".0"),
                        hsnCode = service.hsnSacCode ?: "",
                        description = service.description ?: "",
                        
                        // Set Initial Baseline (For Dirty Check)
                        initialName = service.name,
                        initialPrice = service.defaultPrice.toString().removeSuffix(".0"),
                        initialTaxRate = service.taxRate.toString().removeSuffix(".0"),
                        initialHsnCode = service.hsnSacCode ?: "",
                        initialDescription = service.description ?: ""
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun saveService() {
        val currentState = state.value
        
        if (currentState.name.isBlank()) {
            _state.update { it.copy(nameError = "Service name is required") }
            return
        }
        
        val priceValue = currentState.price.toDoubleOrNull()
        if (priceValue == null || priceValue < 0) {
            _state.update { it.copy(priceError = "Enter a valid price") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            // 2. Map State -> Domain
            val serviceToSave = ServiceItem(
                id = serviceId ?: 0,
                name = currentState.name.trim(),
                defaultPrice = priceValue,
                taxRate = currentState.taxRate.toDoubleOrNull() ?: 0.0,
                hsnSacCode = currentState.hsnCode.trim().ifBlank { null },
                description = currentState.description.trim().ifBlank { null }
            )

            // 3. Save to DB
            repository.upsertServiceItem(serviceToSave)

            // 4. Success
            _state.update { it.copy(isSaving = false) }
            _uiEvent.send(AddEditServiceEvent.ShowSnackbar("Service saved successfully"))
            _uiEvent.send(AddEditServiceEvent.NavigateBack)
        }
    }

    private fun deleteService() {
        serviceId?.let { id ->
            viewModelScope.launch {
                repository.deleteServiceItem(id)
                _uiEvent.send(AddEditServiceEvent.ShowSnackbar("Service deleted"))
                _uiEvent.send(AddEditServiceEvent.NavigateBack)
            }
        }
    }
}