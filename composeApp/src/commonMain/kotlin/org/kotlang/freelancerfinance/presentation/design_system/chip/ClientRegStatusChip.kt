package org.kotlang.freelancerfinance.presentation.design_system.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.kotlang.freelancerfinance.domain.model.ClientStatus
import org.kotlang.freelancerfinance.presentation.theme.MoneyGreen

@Composable
fun ClientRegStatusChip(status: ClientStatus) {
    val (text, color) = when (status) {
        ClientStatus.REGISTERED -> "GSTIN" to MoneyGreen
        ClientStatus.UNREGISTERED -> "Unreg" to Color.Gray
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        color = color.copy(alpha = 0.08f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}