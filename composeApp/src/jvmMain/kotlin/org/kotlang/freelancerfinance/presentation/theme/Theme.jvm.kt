package org.kotlang.freelancerfinance.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun FinanceAppTheme(
    darkTheme: Boolean,
    lightThemeColors: ColorScheme,
    darkThemeColors: ColorScheme,
    content: @Composable (() -> Unit)
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkThemeColors else lightThemeColors,
        content = content
    )
}