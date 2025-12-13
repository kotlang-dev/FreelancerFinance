package org.kotlang.freelancerfinance.presentation.manage_client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.repository.ClientRepository

class ManageClientViewModel(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<ManageClientUiState> = combine(
        _searchQuery,
        clientRepository.getAllClients()
    ) { query, clients ->

        val filteredList = if (query.isBlank()) {
            clients
        } else {
            clients.filter { client ->
                client.name.contains(query, ignoreCase = true) ||
                        (client.gstin?.contains(query, ignoreCase = true) == true) ||
                        client.address.contains(query, ignoreCase = true)
            }
        }

        // B. Map Domain Model -> UI Model
        val uiList = filteredList.map { client ->
            client.toUiModel()
        }

        // C. Return final state
        ManageClientUiState(
            isLoading = false,
            searchQuery = query,
            filteredClients = uiList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ManageClientUiState(isLoading = true)
    )

    fun onAction(action: ManageClientUiAction) {
        when (action) {
            is ManageClientUiAction.OnSearchQueryChange -> {
                _searchQuery.value = action.newQuery
            }
            is ManageClientUiAction.OnAddEditClientClick -> Unit
            ManageClientUiAction.OnGoBackClick -> Unit
        }
    }

    private fun Client.toUiModel(): ClientListItemUi {
        return ClientListItemUi(
            id = this.id,
            name = this.name,
            locationShort = "${this.address}, ${this.state.name}",
            status = if (gstin.isNullOrBlank()) ClientStatus.UNREGISTERED else ClientStatus.GSTIN,
            initials = getInitials(this.name)
        )
    }

    private fun getInitials(name: String): String {
        val parts = name.trim().split(" ")
        return when {
            parts.isEmpty() -> ""
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].first()}${parts[1].first()}".uppercase()
        }
    }
}