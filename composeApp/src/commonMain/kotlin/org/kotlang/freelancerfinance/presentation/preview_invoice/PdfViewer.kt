package org.kotlang.freelancerfinance.presentation.preview_invoice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PdfViewer(
    modifier: Modifier = Modifier,
    pdfPath: String
)