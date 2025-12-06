package org.kotlang.freelancerfinance.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_arrow_back
import freelancerfinance.composeapp.generated.resources.ic_person
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.presentation.profile.component.ImagePickerFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pickerFactory = ImagePickerFactory()

    // 2. Create the Picker (Pass the callback to the ViewModel)
    val picker = pickerFactory.createPicker { bytes ->
        viewModel.onLogoPicked(bytes)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Setup Business Profile") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = "Navigate Back"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LogoPicker(
                logoPath = uiState.logoPath,
                onClick = { picker.pickImage() }
            )

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

            Button(
                onClick = {
                    val nextOrdinal = (uiState.selectedState.ordinal + 1) % IndianState.entries.size
                    val nextState = IndianState.entries[nextOrdinal]
                    viewModel.onAction(ProfileUiAction.UpdateState(nextState))
                }
            ) {
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
        }

        // Debug Text
        if (uiState.savedProfile != null) {
            Text("Current Saved: ${uiState.savedProfile?.businessName}")
        }
    }
}


@Composable
fun LogoPicker(
    logoPath: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable { onClick() },
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (logoPath != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_person),
                        contentDescription = "Saved",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Logo Saved", style = MaterialTheme.typography.labelMedium)
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_person),
                        contentDescription = "Add Logo",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                    Text("Add Logo", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
