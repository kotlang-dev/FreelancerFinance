package org.kotlang.freelancerfinance.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.model.BusinessProfile
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.domain.repository.FileSystemHelper
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository
import org.kotlang.freelancerfinance.domain.usecase.TextFieldValidator
import org.kotlang.freelancerfinance.domain.usecase.ValidationResult

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val fileSystemHelper: FileSystemHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<ProfileUiEvent>(Channel.BUFFERED)
    val events = _eventChannel.receiveAsFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val saved = repository.getProfile().firstOrNull()

            if (saved != null) {
                _uiState.update {
                    it.copy(
                        originalProfile = saved,
                        businessName = saved.businessName,
                        panNumber = saved.panNumber,
                        gstin = saved.gstin ?: "",
                        addressLine1 = saved.addressLine1,
                        addressLine2 = saved.addressLine2,
                        pincode = saved.pincode,
                        selectedState = saved.state,
                        logoPath = saved.logoPath,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onAction(action: ProfileUiAction) {
        when (action) {
            is ProfileUiAction.UpdateBusinessName -> updateBusinessName(action.name)
            is ProfileUiAction.UpdatePanNumber -> updatePanNumber(action.pan)
            is ProfileUiAction.UpdateGstin -> updateGstin(action.gstin)
            is ProfileUiAction.UpdateAddressLine1 -> updateAddressLine1(action.value)
            is ProfileUiAction.UpdateAddressLine2 -> updateAddressLine2(action.value)
            is ProfileUiAction.UpdateState -> updateState(action.state)
            is ProfileUiAction.UpdatePincode -> updatePincode(action.pincode)
            is ProfileUiAction.UpdateLogo -> updateLogo(action.bytes)
            ProfileUiAction.SaveProfile -> saveProfile()
        }
    }

    private fun updateBusinessName(name: String) {
        val result = TextFieldValidator.validateBusinessName(name)
        val error = if (result is ValidationResult.Error) result.message else null

        _uiState.update {
            it.copy(businessName = name, nameError = error)
        }
    }

    private fun updatePanNumber(pan: String) {
        val capsPan = pan.uppercase()
        val result = TextFieldValidator.validatePan(capsPan)
        val error = if (result is ValidationResult.Error) result.message else null

        _uiState.update {
            it.copy(panNumber = capsPan, panError = error)
        }

        validateGstinConsistency(_uiState.value.gstin, capsPan)
    }

    private fun updateGstin(gstin: String) {
        val capsGstin = gstin.uppercase()
        var error: String? = null
        val formatResult = TextFieldValidator.validateGstin(capsGstin)
        if (formatResult is ValidationResult.Error) {
            error = formatResult.message
        } else {
            val matchResult = TextFieldValidator.validateGstinMatchesPan(capsGstin, _uiState.value.panNumber)
            if (matchResult is ValidationResult.Error) error = matchResult.message
        }

        _uiState.update {
            it.copy(gstin = capsGstin, gstinError = error)
        }
    }

    private fun updateAddressLine1(value: String) {
        _uiState.update { it.copy(addressLine1 = value) }
    }

    private fun updateAddressLine2(value: String) {
        _uiState.update { it.copy(addressLine2 = value) }
    }

    private fun updateState(state: IndianState) {
        _uiState.update { it.copy(selectedState = state) }
    }

    private fun updatePincode(pincode: String) {
        val result = TextFieldValidator.validatePincode(pincode)
        val error = if (result is ValidationResult.Error) result.message else null
        _uiState.update {
            it.copy(pincode = pincode, pincodeError = error)
        }
    }

    private fun saveProfile() {
        val currentState = _uiState.value
        val newProfile = BusinessProfile(
            businessName = currentState.businessName,
            panNumber = currentState.panNumber,
            gstin = currentState.gstin.ifBlank { null },
            addressLine1 = currentState.addressLine1,
            addressLine2 = currentState.addressLine2,
            pincode = currentState.pincode,
            state = currentState.selectedState,
            logoPath = currentState.logoPath
        )
        viewModelScope.launch {
            repository.saveProfile(newProfile)
            _uiState.update { it.copy(originalProfile = newProfile) }
            _eventChannel.send(ProfileUiEvent.ShowSnackbar("Profile saved successfully"))
        }
    }

    fun updateLogo(bytes: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val savedPath = fileSystemHelper.saveFile("company_logo.jpg", bytes)
                val dbProfile = _uiState.value.originalProfile

                val profileToSave = if (dbProfile != null) {
                    dbProfile.copy(logoPath = savedPath)
                } else {
                    val currentInput = _uiState.value
                    BusinessProfile(
                        businessName = currentInput.businessName,
                        panNumber = currentInput.panNumber,
                        gstin = currentInput.gstin.ifBlank { null },
                        addressLine1 = currentInput.addressLine1,
                        addressLine2 = currentInput.addressLine2,
                        pincode = currentInput.pincode,
                        state = currentInput.selectedState,
                        logoPath = savedPath
                    )
                }

                repository.saveProfile(profileToSave)

                _uiState.update {
                    it.copy(
                        logoPath = savedPath,
                        originalProfile = profileToSave
                    )
                }

                _eventChannel.send(ProfileUiEvent.ShowSnackbar("Logo updated successfully!"))
            } catch (e: Exception) {
                e.printStackTrace()
                _eventChannel.send(ProfileUiEvent.ShowSnackbar("Failed to save logo"))
            }
        }
    }

    private fun validateGstinConsistency(gstin: String, pan: String) {
        val matchResult = TextFieldValidator.validateGstinMatchesPan(gstin, pan)
        val error = if (matchResult is ValidationResult.Error) matchResult.message else null
        _uiState.update { it.copy(gstinError = error) }
    }
}
