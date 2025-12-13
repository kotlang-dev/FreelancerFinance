package org.kotlang.freelancerfinance.presentation.add_edit_client

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
import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.domain.repository.ClientRepository
import org.kotlang.freelancerfinance.domain.usecase.TextFieldValidator
import org.kotlang.freelancerfinance.domain.usecase.ValidationResult
import org.kotlang.freelancerfinance.presentation.navigation.Route

class AddEditClientViewModel(
    private val repository: ClientRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val clientId: Long? = savedStateHandle.toRoute<Route.AddEditClient>().clientId

    private val _state = MutableStateFlow(AddEditClientUiState(clientId = clientId))
    val state: StateFlow<AddEditClientUiState> = _state.asStateFlow()

    private val _uiEvent = Channel<AddEditClientEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        clientId?.let { id ->
            loadClient(id)
        }
    }

    fun onAction(action: AddEditClientUiAction) {
        when (action) {
            is AddEditClientUiAction.OnNameChange -> updateBusinessName(action.value)
            is AddEditClientUiAction.OnGstinChange -> updateGstin(action.value)
            is AddEditClientUiAction.OnAddressChange -> updateAddress(action.value)
            is AddEditClientUiAction.OnStateChange -> updateState(action.state)
            AddEditClientUiAction.OnSaveClick -> saveClient()
            AddEditClientUiAction.OnDeleteClick -> deleteClient()
            AddEditClientUiAction.OnGoBackClick -> Unit
        }
    }

    private fun loadClient(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            val client = repository.getClientById(id)
            
            if (client != null) {
                _state.update {
                    it.copy(
                        isLoading = false,

                        name = client.name,
                        gstin = client.gstin ?: "",
                        address = client.address,
                        state = client.state,

                        initialName = client.name,
                        initialGstin = client.gstin ?: "",
                        initialAddress = client.address,
                        initialState = client.state,
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateBusinessName(name: String) {
        val result = TextFieldValidator.validateBusinessName(name)
        val error = if (result is ValidationResult.Error) result.message else null

        _state.update {
            it.copy(name = name, nameError = error)
        }
    }

    private fun updateGstin(gstin: String) {
        val capsGstin = gstin.uppercase()
        val result = TextFieldValidator.validateGstin(capsGstin)
        val error = if (result is ValidationResult.Error) result.message else null

        _state.update {
            it.copy(gstin = capsGstin, gstinError = error)
        }
    }

    private fun updateAddress(address: String) {
        val result = TextFieldValidator.validateAddress(address)
        val error = if (result is ValidationResult.Error) result.message else null

        _state.update {
            it.copy(address = address, addressError = error)
        }
    }

    private fun updateState(state: IndianState) {
        _state.update { it.copy(state = state) }
    }

    private fun saveClient() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val clientToSave = Client(
                id = clientId ?: 0,
                name = state.value.name.trim(),
                gstin = state.value.gstin.trim().ifBlank { null },
                address = state.value.address.trim(),
                state = state.value.state
            )

            repository.upsertClient(clientToSave)

            _state.update { it.copy(isSaving = false) }
            _uiEvent.send(AddEditClientEvent.NavigateBack)
        }
    }

    private fun deleteClient() {
        clientId?.let { id ->
            viewModelScope.launch {
                repository.deleteClient(id)
                _uiEvent.send(AddEditClientEvent.NavigateBack)
            }
        }
    }
}

