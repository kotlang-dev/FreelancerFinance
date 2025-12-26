package org.kotlang.freelancerfinance.presentation.preview_invoice

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment

@Composable
actual fun PdfViewer(
    modifier: Modifier,
    pdfPath: String
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text("Desktop PDF Viewer not implemented yet")
    }
}