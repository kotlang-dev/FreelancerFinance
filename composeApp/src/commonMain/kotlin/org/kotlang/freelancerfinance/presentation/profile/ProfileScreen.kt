package org.kotlang.freelancerfinance.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_card
import freelancerfinance.composeapp.generated.resources.ic_edit
import freelancerfinance.composeapp.generated.resources.ic_outline_add
import freelancerfinance.composeapp.generated.resources.ic_receipt_long
import freelancerfinance.composeapp.generated.resources.img_company_logo_placeholder
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
import org.kotlang.freelancerfinance.presentation.design_system.textfields.StandardDropdownField
import org.kotlang.freelancerfinance.presentation.design_system.textfields.StandardTextField
import org.kotlang.freelancerfinance.presentation.profile.component.ImagePickerFactory
import org.kotlang.freelancerfinance.presentation.util.ObserveAsEvents

@Composable
fun ProfileScreenRoot(
    snackbarHostState: SnackbarHostState,
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ProfileUiEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    ProfileScreen(
        state = uiState,
        onAction = { action ->
            viewModel.onAction(action)
        },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    onAction: (ProfileUiAction) -> Unit,
    onNavigateBack: () -> Unit
) {

    val pickerFactory = ImagePickerFactory()
    val picker = pickerFactory.createPicker { bytes ->
        onAction(ProfileUiAction.UpdateLogo(bytes))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier = Modifier
                .widthIn(max = 700.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 60.dp, bottom = 100.dp)
        ) {

            ProfileHeaderSection(
                logoPath = state.logoPath,
                onEditLogoClick = { picker.pickImage() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                StandardTextField(
                    inputLabel = "Business Name",
                    value = state.businessName,
                    onValueChange = { onAction(ProfileUiAction.UpdateBusinessName(it)) },
                    placeholderText = "Company Name",
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.nameError != null,
                    errorMessage = state.nameError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tax Details Card
                SectionCard(title = "Tax Details") {
                    ResponsiveFormRow(
                        contentStart = {
                            StandardTextField(
                                inputLabel = "PAN Number",
                                value = state.panNumber,
                                onValueChange = { onAction(ProfileUiAction.UpdatePanNumber(it)) },
                                placeholderText = "Enter your PAN",
                                leadingIconResId = Res.drawable.ic_card,
                                isError = state.panError != null,
                                errorMessage = state.panError,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Characters,
                                    autoCorrectEnabled = false,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )
                        },
                        contentEnd = {
                            StandardTextField(
                                inputLabel = "GSTIN (Optional for Unregistered)",
                                value = state.gstin,
                                onValueChange = { onAction(ProfileUiAction.UpdateGstin(it)) },
                                placeholderText = "Enter your GSTIN",
                                leadingIconResId = Res.drawable.ic_receipt_long,
                                isError = state.gstinError != null,
                                errorMessage = state.gstinError,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Characters,
                                    autoCorrectEnabled = false,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Address Details Card
                SectionCard(title = "Address Details") {

                    ResponsiveFormRow(
                        contentStart = {
                            StandardTextField(
                                inputLabel = "Address Line 1",
                                value = state.addressLine1,
                                onValueChange = { onAction(ProfileUiAction.UpdateAddressLine1(it)) },
                                placeholderText = "Enter your address",
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )

                        },
                        contentEnd = {
                            StandardTextField(
                                inputLabel = "Address Line 2 (Optional)",
                                value = state.addressLine2,
                                onValueChange = { onAction(ProfileUiAction.UpdateAddressLine2(it)) },
                                placeholderText = "Apartment, suite, etc.",
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ResponsiveFormRow(
                        contentStart = {
                            StandardDropdownField(
                                inputLabel = "State",
                                selectedValue = state.selectedState.stateName,
                                options = IndianState.entries,
                                itemLabel = { it.stateName },
                                onValueChanged = { onAction(ProfileUiAction.UpdateState(it)) }
                            )
                        },
                        contentEnd = {
                            StandardTextField(
                                inputLabel = "Pincode",
                                value = state.pincode,
                                onValueChange = { onAction(ProfileUiAction.UpdatePincode(it)) },
                                placeholderText = "Enter pincode",
                                isError = state.pincodeError != null,
                                errorMessage = state.pincodeError,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                )
                            )
                        }
                    )
                }
            }
        }

        FinanceTopBar(
            title = "Setup Business Profile",
            onNavigateBack = onNavigateBack
        )

        SaveButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .widthIn(max = 700.dp)
                .padding(16.dp),
            enabled = state.isSaveEnabled,
            onClick = { onAction(ProfileUiAction.SaveProfile) }
        )
    }
}

@Composable
fun ProfileHeaderSection(
    logoPath: String?,
    onEditLogoClick: () -> Unit
) {
    val iconResId =
        if (logoPath.isNullOrEmpty()) Res.drawable.ic_outline_add else Res.drawable.ic_edit
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Blue Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp) // Banner height
                .align(Alignment.TopCenter)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )

        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                model = logoPath,
                contentDescription = "Company Logo",
                modifier = Modifier
                    .size(120.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .padding(4.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.img_company_logo_placeholder),
                fallback = painterResource(Res.drawable.img_company_logo_placeholder)
            )

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clickable { onEditLogoClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = "Edit Logo",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled
    ) {
        Text(
            text = "Save Changes",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ResponsiveFormRow(
    modifier: Modifier = Modifier,
    spacing: Dp = 16.dp,
    threshold: Dp = 500.dp,
    contentStart: @Composable ColumnScope.() -> Unit,
    contentEnd: @Composable ColumnScope.() -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        if (maxWidth >= threshold) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    contentStart()
                }
                Column(modifier = Modifier.weight(1f)) {
                    contentEnd()
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                Column { contentStart() }
                Column { contentEnd() }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        state = ProfileUiState(),
        onAction = {},
        onNavigateBack = {}
    )
}