package org.kotlang.freelancerfinance.presentation.design_system.textfields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun StandardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    inputLabel: String,
    modifier: Modifier = Modifier,
    placeholderText: String? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    isError: Boolean = false,
    errorMessage: String? = null,
    leadingIconResId: DrawableResource? = null,
    trailingIconResId: DrawableResource? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = inputLabel,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 6.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            isError = isError,
            supportingText = if (errorMessage != null) {
                { Text(text = errorMessage) }
            } else null,
            placeholder = if (placeholderText != null) {
                {
                    Text(
                        text = placeholderText,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            } else null,
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
            } else null,
            trailingIcon = if (trailingIconResId != null) {
                {
                    Icon(
                        painter = painterResource(trailingIconResId),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else null,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }
}