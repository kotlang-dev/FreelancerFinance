package org.kotlang.freelancerfinance.presentation.add_edit_service

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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_delete
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
import org.kotlang.freelancerfinance.presentation.design_system.textfields.StandardDropdownField
import org.kotlang.freelancerfinance.presentation.design_system.textfields.StandardTextField
import org.kotlang.freelancerfinance.presentation.util.ObserveAsEvents

@Composable
fun AddEditServiceRoot(
    snackbarHostState: SnackbarHostState,
    viewModel: AddEditServiceViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is AddEditServiceEvent.NavigateBack -> {
                onNavigateBack()
            }
            is AddEditServiceEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    AddEditServiceScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is AddEditServiceUiAction.OnGoBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditServiceScreen(
    state: AddEditServiceUiState,
    onAction: (AddEditServiceUiAction) -> Unit
) {

    // Helper list for GST Dropdown
    val gstRates = listOf("0.0", "5.0", "12.0", "18.0", "28.0")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        FinanceTopBar(
            title = if (state.isEditMode) "Edit Service" else "Add New Service",
            onNavigateBack = { onAction(AddEditServiceUiAction.OnGoBackClick) }
        )

        Column(
            modifier = Modifier
                .widthIn(max = 700.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            StandardTextField(
                modifier = Modifier.fillMaxWidth(),
                inputLabel = "Service Name / Title",
                value = state.name,
                onValueChange = { onAction(AddEditServiceUiAction.OnNameChange(it)) },
                isError = state.nameError != null,
                errorMessage = state.nameError,
                placeholderText = "e.g. Android App Development",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                )
            )

            StandardTextField(
                modifier = Modifier.fillMaxWidth(),
                inputLabel = "Default Price (₹)",
                value = state.price,
                onValueChange = { onAction(AddEditServiceUiAction.OnPriceChange(it)) },
                isError = state.priceError != null,
                errorMessage = state.priceError,
                placeholderText = "0.00",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )

            StandardDropdownField(
                inputLabel = "GST Rate (%)",
                selectedValue = state.taxRate,
                options = gstRates,
                itemLabel = { "$it%" },
                onValueChanged = { onAction(AddEditServiceUiAction.OnTaxRateChange(it)) }
            )

            StandardTextField(
                modifier = Modifier.fillMaxWidth(),
                inputLabel = "HSN / SAC Code (Optional)",
                value = state.hsnCode,
                onValueChange = { onAction(AddEditServiceUiAction.OnHsnCodeChange(it)) },
                placeholderText = "e.g. 998313",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            StandardTextField(
                modifier = Modifier.fillMaxWidth(),
                inputLabel = "Description / Details (Optional)",
                value = state.description,
                onValueChange = { onAction(AddEditServiceUiAction.OnDescriptionChange(it)) },
                singleLine = false,
                minLines = 3,
                maxLines = 5,
                placeholderText = "Default details to appear on the invoice...",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onAction(AddEditServiceUiAction.OnSaveClick) },
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
                    text = if (state.isEditMode) "Save Service" else "Add Service",
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
                    onClick = { onAction(AddEditServiceUiAction.OnDeleteClick) },
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
                    Text("Delete Service")
                }

                Text(
                    text = "Deleting this service will not affect past invoices where this item was used.",
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