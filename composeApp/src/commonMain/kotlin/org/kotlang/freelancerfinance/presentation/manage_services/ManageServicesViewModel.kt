package org.kotlang.freelancerfinance.presentation.manage_services

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
import kotlinx.coroutines.flow.update
import org.kotlang.freelancerfinance.domain.repository.ServiceItemRepository

class ManageServicesViewModel(
    repository: ServiceItemRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    private val debouncedFilterQuery = _searchQuery
        .debounce(300L)
        .map { it.trim() }
        .distinctUntilChanged()

    val uiState: StateFlow<ManageServicesUiState> = combine(
        _searchQuery,
        debouncedFilterQuery,
        repository.getAllServiceItems()
    ) { rawQuery, filterQuery, services ->

        val filteredList = if (filterQuery.isBlank()) {
            services
        } else {
            services.filter { service ->
                service.name.contains(filterQuery, ignoreCase = true)
            }
        }

        ManageServicesUiState(
            isLoading = false,
            searchQuery = rawQuery,
            filteredServices = filteredList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ManageServicesUiState(isLoading = true)
    )

    fun onAction(action: ManageServicesUiAction) {
        when (action) {
            is ManageServicesUiAction.OnSearchQueryChange -> {
                _searchQuery.update { action.query }
            }
            else -> Unit
        }
    }

}