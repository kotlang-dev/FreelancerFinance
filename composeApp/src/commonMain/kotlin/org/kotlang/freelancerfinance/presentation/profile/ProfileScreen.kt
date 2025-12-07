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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_arrow_back_ios
import freelancerfinance.composeapp.generated.resources.ic_card
import freelancerfinance.composeapp.generated.resources.ic_edit
import freelancerfinance.composeapp.generated.resources.ic_outline_add
import freelancerfinance.composeapp.generated.resources.ic_receipt_long
import freelancerfinance.composeapp.generated.resources.img_company_logo_placeholder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
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
                logoPath = uiState.logoPath,
                onEditLogoClick = { picker.pickImage() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                InputLabel(text = "Business Name")
                CustomTextField(
                    value = uiState.businessName,
                    onValueChange = { viewModel.onAction(ProfileUiAction.UpdateBusinessName(it)) },
                    placeholder = "Company Name",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tax Details Card
                SectionCard(title = "Tax Details") {
                    ResponsiveFormRow(
                        contentStart = {
                            InputLabel(text = "PAN Number")
                            CustomTextField(
                                value = uiState.panNumber,
                                onValueChange = {
                                    viewModel.onAction(
                                        ProfileUiAction.UpdatePanNumber(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Enter your PAN",
                                leadingIconResId = Res.drawable.ic_card
                            )
                        },
                        contentEnd = {
                            InputLabel(text = "GSTIN")
                            CustomTextField(
                                value = uiState.gstin,
                                onValueChange = { viewModel.onAction(ProfileUiAction.UpdateGstin(it)) },
                                placeholder = "Enter your GSTIN",
                                leadingIconResId = Res.drawable.ic_receipt_long
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Address Details Card
                SectionCard(title = "Address Details") {

                    ResponsiveFormRow(
                        contentStart = {
                            InputLabel(text = "Address Line 1")
                            CustomTextField(
                                value = uiState.address,
                                onValueChange = {
                                    viewModel.onAction(
                                        ProfileUiAction.UpdateAddress(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Enter your address"
                            )

                        },
                        contentEnd = {
                            InputLabel(text = "Address Line 2")
                            CustomTextField(
                                value = uiState.address,
                                onValueChange = {
                                    viewModel.onAction(
                                        ProfileUiAction.UpdateAddress(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Apartment, suite, etc. (optional)"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ResponsiveFormRow(
                        contentStart = {
                            InputLabel(text = "State")
                            DropdownField(
                                selectedValue = uiState.selectedState.stateName,
                                onValueChange = { viewModel.onAction(ProfileUiAction.UpdateState(it)) },
                                options = IndianState.entries.map { it.stateName }
                            )
                        },
                        contentEnd = {
                            InputLabel(text = "Pincode")
                            CustomTextField(
                                value = uiState.pincode,
                                onValueChange = { },
                                placeholder = "Enter pincode"
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
                .padding(16.dp)
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
fun InputLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(bottom = 6.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIconResId: DrawableResource? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        ),
        leadingIcon = if (leadingIconResId != null) {
            {
                Icon(
                    painter = painterResource(leadingIconResId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else null
    )
}

@Composable
fun DropdownField(
    selectedValue: String,
    onValueChange: (String) -> Unit,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedValue.ifEmpty { "Select a state" },
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back_ios),
                    contentDescription = "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .rotate(270f)
                        .clickable { expanded = true }
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SaveButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /* Save Logic */ },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp)
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
        onNavigateBack = {}
    )
}