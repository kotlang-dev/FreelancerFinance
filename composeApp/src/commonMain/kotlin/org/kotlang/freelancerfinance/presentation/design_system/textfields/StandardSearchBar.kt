package org.kotlang.freelancerfinance.presentation.design_system.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_close
import freelancerfinance.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A custom search bar component for the Finance app.
 *
 * @param value The current text to be displayed in the search bar.
 * @param onValueChange The callback that is triggered when the input service updates the text.
 * @param modifier The modifier to be applied to the component.
 * @param placeholderText The text to be displayed when the search bar is empty.
 */
@Composable
fun StandardSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = ""
) {
    val focusManager = LocalFocusManager.current
    val textStyle = TextStyle(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(50.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium,
            ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholderText,
                            style = textStyle.copy(
                                color = textStyle.color.copy(alpha = 0.5f)
                            )
                        )
                    }
                    innerTextField()
                }
                if (value.isNotBlank()) {
                    IconButton(
                        onClick = { onValueChange("") }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewSearchBar() {
    StandardSearchBar(
        value = "k",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth()
    )
}