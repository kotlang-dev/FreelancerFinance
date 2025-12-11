package org.kotlang.freelancerfinance.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository

class DashboardViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            //_uiState.update { it.copy(isLoading = true) }
            profileRepository.getProfile().collect { profile ->
                _uiState.update { currentState ->
                    currentState.copy(
                        companyName = profile?.businessName ?: "Freelancer",
                        logoPath = profile?.logoPath
                    )
                }
            }
        }
    }

}