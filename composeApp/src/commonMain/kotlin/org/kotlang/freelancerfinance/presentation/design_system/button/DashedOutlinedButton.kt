package org.kotlang.freelancerfinance.presentation.design_system.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_outline_add
import org.jetbrains.compose.resources.painterResource

@Composable
fun DashedOutlinedButton(
    text: String, 
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .dashedBorder(color = MaterialTheme.colorScheme.outlineVariant, cornerRadius = 12.dp)
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        border = null
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.ic_outline_add),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontWeight = FontWeight.Bold)
        }
    }
}

fun Modifier.dashedBorder(width: Dp = 1.dp, color: Color, cornerRadius: Dp) = drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        ),
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}