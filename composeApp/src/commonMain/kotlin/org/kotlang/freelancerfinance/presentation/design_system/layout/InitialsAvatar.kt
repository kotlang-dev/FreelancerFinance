package org.kotlang.freelancerfinance.presentation.design_system.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun InitialsAvatar(
    name: String,
    initials: String,
    modifier: Modifier = Modifier,
    avatarSizeDp: Int = 48
) {

    // Deterministic random color based on name hash
    val colorIndex = kotlin.math.abs(name.hashCode()) % 4
    val (bgColor, textColor) = when (colorIndex) {
        0 -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        1 -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        2 -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        else -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }

    Box(
        modifier = modifier
            .size(avatarSizeDp.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}