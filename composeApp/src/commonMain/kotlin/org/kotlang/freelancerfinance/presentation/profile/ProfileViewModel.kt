package org.kotlang.freelancerfinance.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.model.BusinessProfile
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getProfile().collect { profile ->
                if (profile != null) {
                    _uiState.update {
                        it.copy(
                            businessName = profile.businessName,
                            panNumber = profile.panNumber,
                            gstin = profile.gstin ?: "",
                            address = profile.address,
                            city = profile.city,
                            pincode = profile.pincode,
                            selectedState = profile.state,
                            savedProfile = profile
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.UpdateBusinessName -> {
                _uiState.update { it.copy(businessName = action.name) }
            }
            is ProfileUiAction.UpdatePanNumber -> {
                _uiState.update { it.copy(panNumber = action.pan) }
            }
            is ProfileUiAction.UpdateGstin -> {
                _uiState.update { it.copy(gstin = action.gstin) }
            }
            is ProfileUiAction.UpdateAddress -> {
                _uiState.update { it.copy(address = action.address) }
            }
            is ProfileUiAction.UpdateState -> {
                _uiState.update { it.copy(selectedState = action.state) }
            }
            ProfileUiAction.SaveProfile -> {
                saveProfile()
            }
        }
    }

    private fun saveProfile() {
        val currentState = _uiState.value
        val profile = BusinessProfile(
            businessName = currentState.businessName,
            panNumber = currentState.panNumber,
            gstin = currentState.gstin.ifBlank { null },
            address = currentState.address,
            city = currentState.city,
            pincode = currentState.pincode,
            state = currentState.selectedState
        )
        viewModelScope.launch {
            repository.saveProfile(profile)
        }
    }
}
