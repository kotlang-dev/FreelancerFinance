package org.kotlang.freelancerfinance.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.repository.InvoiceRepository
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository

class DashboardViewModel(
    private val profileRepository: ProfileRepository,
    private val invoiceRepository: InvoiceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            combine(
                profileRepository.getProfile(),
                invoiceRepository.getTotalRevenue(),
                invoiceRepository.getRecentInvoicesSummary()
            ) { profile, totalRevenue, recentList ->
                DashboardUiState(
                    companyName = profile?.businessName ?: "Freelancer",
                    logoPath = profile?.logoPath,
                    totalInvoicedValue = totalRevenue,
                    recentInvoices = recentList,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

}