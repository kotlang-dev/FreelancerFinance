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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.IndianState

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Business Setup")

        OutlinedTextField(
            value = uiState.businessName,
            onValueChange = { viewModel.onAction(ProfileUiAction.UpdateBusinessName(it)) },
            label = { Text("Business Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.panNumber,
            onValueChange = { viewModel.onAction(ProfileUiAction.UpdatePanNumber(it)) },
            label = { Text("PAN Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.gstin,
            onValueChange = { viewModel.onAction(ProfileUiAction.UpdateGstin(it)) },
            label = { Text("GSTIN (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val nextOrdinal = (uiState.selectedState.ordinal + 1) % IndianState.entries.size
            val nextState = IndianState.entries[nextOrdinal]
            viewModel.onAction(ProfileUiAction.UpdateState(nextState))
        }) {
            Text("State: ${uiState.selectedState.stateName} (${uiState.selectedState.code})")
        }

        OutlinedTextField(
            value = uiState.address,
            onValueChange = { viewModel.onAction(ProfileUiAction.UpdateAddress(it)) },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onAction(ProfileUiAction.SaveProfile)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Profile")
        }

        // Debug Text
        if (uiState.savedProfile != null) {
            Text("Current Saved: ${uiState.savedProfile?.businessName}")
        }
    }
}