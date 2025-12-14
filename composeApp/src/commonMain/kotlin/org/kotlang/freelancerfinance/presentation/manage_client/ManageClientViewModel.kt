package org.kotlang.freelancerfinance.presentation.manage_client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.repository.ClientRepository

class ManageClientViewModel(
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    @OptIn(FlowPreview::class)
    private val debouncedSearchQuery = _searchQuery
        .debounce(300L)
        .map { it.trim() }
        .distinctUntilChanged()

    val uiState: StateFlow<ManageClientUiState> = combine(
        _searchQuery,
        debouncedSearchQuery,
        clientRepository.getAllClients()
    ) { rawQuery, filterQuery, clients ->

        val filteredList = if (filterQuery.isBlank()) {
            clients
        } else {
            clients.filter { client ->
                client.name.contains(filterQuery, ignoreCase = true)
            }
        }

        ManageClientUiState(
            isLoading = false,
            searchQuery = rawQuery,
            filteredClients = filteredList.map { it.toUiModel() }
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