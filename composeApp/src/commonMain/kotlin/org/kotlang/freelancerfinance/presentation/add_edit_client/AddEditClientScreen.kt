package org.kotlang.freelancerfinance.presentation.add_edit_client

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_delete
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
import org.kotlang.freelancerfinance.presentation.design_system.textfields.StandardDropdownField
import org.kotlang.freelancerfinance.presentation.design_system.textfields.StandardTextField
import org.kotlang.freelancerfinance.presentation.util.ObserveAsEvents

@Composable
fun AddEditClientRoot(
    snackbarHostState: SnackbarHostState,
    viewModel: AddEditClientViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is AddEditClientEvent.NavigateBack -> {
                onNavigateBack()
            }

            is AddEditClientEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    AddEditClientScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is AddEditClientUiAction.OnGoBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditClientScreen(
    state: AddEditClientUiState,
    onAction: (AddEditClientUiAction) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        FinanceTopBar(
            title = if (state.isEditMode) "Edit Client" else "Add New Client",
            onNavigateBack = { onAction(AddEditClientUiAction.OnGoBackClick) }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            StandardTextField(
                modifier = Modifier.fillMaxWidth(),
                inputLabel = "Client / Business Name",
                value = state.name,
                onValueChange = { onAction(AddEditClientUiAction.OnNameChange(it)) },
                isError = state.nameError != null,
                errorMessage = state.nameError,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )

            StandardTextField(
                modifier = Modifier.fillMaxWidth(),
                inputLabel = "Billing Address",
                value = state.address,
                onValueChange = { onAction(AddEditClientUiAction.OnAddressChange(it)) },
                isError = state.addressError != null,
                errorMessage = state.addressError,
                singleLine = false,
                minLines = 3,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            StandardTextField(
                modifier = Modifier.fillMaxWidth(),
                inputLabel = "GSTIN (Optional)",
                value = state.gstin,
                onValueChange = { onAction(AddEditClientUiAction.OnGstinChange(it.uppercase())) },
                isError = state.gstinError != null,
                errorMessage = state.gstinError,
                placeholderText = "e.g. 29ABCDE1234F1Z5",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next
                )
            )

            StandardDropdownField(
                inputLabel = "State",
                selectedValue = state.state.stateName,
                options = IndianState.entries,
                itemLabel = { it.stateName },
                onValueChanged = { onAction(AddEditClientUiAction.OnStateChange(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onAction(AddEditClientUiAction.OnSaveClick) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = state.isSaveEnabled
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                }
                Text(
                    text = if (state.isEditMode) "Save Changes" else "Add Client",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- DELETE SECTION (Edit Mode Only) ---
            if (state.isEditMode) {
                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { onAction(AddEditClientUiAction.OnDeleteClick) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Client")
                }

                Text(
                    text = "Deleting this client will not delete their past invoices, but you won't be able to select this client for new ones.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}