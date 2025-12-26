package org.kotlang.freelancerfinance.presentation.preview_invoice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import android.graphics.Bitmap

@Composable
actual fun PdfViewer(
    modifier: Modifier,
    pdfPath: String
) {
    AndroidPdfViewerContent(modifier, pdfPath)
}

@Composable
private fun AndroidPdfViewerContent(
    modifier: Modifier,
    path: String
) {
    val converter = remember { PdfBitmapConverter() }
    var pages by remember { mutableStateOf<List<Bitmap>?>(null) }

    LaunchedEffect(path) {
        pages = converter.pdfToBitmaps(path)
    }

    if (pages == null) {
        // You can add a specific loading state here if needed
    } else {
        LazyColumn(modifier = modifier) {
            items(pages!!) { pageBitmap ->
                Image(
                    bitmap = pageBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(pageBitmap.width.toFloat() / pageBitmap.height.toFloat())
                )
            }
        }
    }
}