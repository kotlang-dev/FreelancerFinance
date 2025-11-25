package org.kotlang.freelancerfinance.presentation.client_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.repository.ClientRepository

class ClientListViewModel(
    private val repository: ClientRepository
) : ViewModel() {

    val uiState: StateFlow<ClientListUiState> = repository.getAllClients()
        .map { list -> ClientListUiState(clients = list, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ClientListUiState(isLoading = true)
        )

    fun onAction(action: ClientListUiAction) {
        when (action) {
            is ClientListUiAction.DeleteClient -> {
                viewModelScope.launch { repository.deleteClient(action.client) }
            }
            is ClientListUiAction.AddClient -> {
                val newClient = Client(
                    name = action.name,
                    gstin = action.gstin?.ifBlank { null },
                    address = action.address,
                    state = action.state,
                    email = action.email
                )
                viewModelScope.launch { repository.insertClient(newClient) }
            }
        }
    }
}