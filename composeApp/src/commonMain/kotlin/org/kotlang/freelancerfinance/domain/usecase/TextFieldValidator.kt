package org.kotlang.freelancerfinance.domain.usecase

object TextFieldValidator {

    // Regex Patterns
    private val PAN_REGEX = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")
    private val GSTIN_REGEX = Regex("[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}")
    private val PINCODE_REGEX = Regex("^[1-9][0-9]{5}$")

    fun validateBusinessName(name: String): ValidationResult {
        if (name.isBlank()) return ValidationResult.Error("Business name cannot be empty")
        if (name.length > 100) return ValidationResult.Error("Name is too long (Max 100 chars)")
        return ValidationResult.Valid
    }

    fun validatePan(pan: String): ValidationResult {
        if (pan.isBlank()) return ValidationResult.Error("PAN is required")
        if (!pan.matches(PAN_REGEX)) return ValidationResult.Error("Invalid PAN format (e.g., ABCDE1234F)")
        return ValidationResult.Valid
    }

    fun validateGstin(gstin: String): ValidationResult {
        if (gstin.isBlank()) return ValidationResult.Valid
        if (gstin.length != 15) return ValidationResult.Error("GSTIN must be 15 characters")
        if (!gstin.matches(GSTIN_REGEX)) return ValidationResult.Error("Invalid GSTIN format")
        
        return ValidationResult.Valid
    }
    
    fun validatePincode(pin: String): ValidationResult {
        if (pin.isBlank()) return ValidationResult.Error("Pincode is required")
        if (!pin.matches(PINCODE_REGEX)) return ValidationResult.Error("Invalid Pincode (6 digits, cannot start with 0)")
        return ValidationResult.Valid
    }
    
    fun validateGstinMatchesPan(gstin: String, pan: String): ValidationResult {
        if (gstin.isBlank()) return ValidationResult.Valid
        if (pan.isBlank()) return ValidationResult.Valid
        
        // Extract chars 3 to 12 (Indices 2..11) from GSTIN
        val embeddedPan = gstin.substring(2, 12)
        return if (embeddedPan == pan) ValidationResult.Valid 
               else ValidationResult.Error("GSTIN does not match the entered PAN")
    }

    fun validateAddress(address: String): ValidationResult {
        if (address.isBlank()) return ValidationResult.Error("Address cannot be empty")
        return ValidationResult.Valid
    }
}

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Error(val message: String) : ValidationResult
}