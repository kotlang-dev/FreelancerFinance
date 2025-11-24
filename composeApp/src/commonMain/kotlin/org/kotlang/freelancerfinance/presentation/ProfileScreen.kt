package org.kotlang.freelancerfinance.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.kotlang.freelancerfinance.domain.model.BusinessProfile
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository

@Composable
fun ProfileScreen() {

    val repository: ProfileRepository = koinInject()
    val scope = rememberCoroutineScope()

    // Read current profile from DB
    val savedProfile by repository.getProfile().collectAsState(initial = null)

    // UI State
    var businessName by remember { mutableStateOf("") }
    var panNumber by remember { mutableStateOf("") }
    var gstin by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf(IndianState.DELHI) }

    // Populate fields when data loads from DB
    LaunchedEffect(savedProfile) {
        savedProfile?.let {
            businessName = it.businessName
            panNumber = it.panNumber
            gstin = it.gstin ?: ""
            address = it.address
            selectedState = it.state
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Business Setup")

        OutlinedTextField(
            value = businessName,
            onValueChange = { businessName = it },
            label = { Text("Business Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = panNumber,
            onValueChange = { panNumber = it },
            label = { Text("PAN Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = gstin,
            onValueChange = { gstin = it },
            label = { Text("GSTIN (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown for State (Simplified for now)
        // In a real app, use an Expanded DropdownMenu
        Button(onClick = {
            // Cycle through states for simple testing MVP
            // Later we make this a proper dropdown
            val nextOrdinal = (selectedState.ordinal + 1) % IndianState.entries.size
            selectedState = IndianState.entries[nextOrdinal]
        }) {
            Text("State: ${selectedState.stateName} (${selectedState.code})")
        }

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val profile = BusinessProfile(
                    businessName = businessName,
                    panNumber = panNumber,
                    gstin = gstin.ifBlank { null },
                    address = address,
                    city = "Mumbai",
                    pincode = "400001",
                    state = selectedState
                )

                scope.launch {
                    repository.saveProfile(profile)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }

        // Debug Text to verify it works
        if (savedProfile != null) {
            Text("Current Saved: ${savedProfile?.businessName}")
        }
    }
}