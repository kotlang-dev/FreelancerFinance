package org.kotlang.freelancerfinance.presentation.preview_invoice

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.io.File
import androidx.core.graphics.createBitmap

class PdfBitmapConverter {

    suspend fun pdfToBitmaps(path: String): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            val file = File(path)
            if (!file.exists()) return@withContext emptyList()

            try {
                // Open the file
                val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(fileDescriptor)

                // Render all pages in parallel
                val bitmapList = (0 until renderer.pageCount).map { index ->
                    async {
                        // Open the page
                        renderer.openPage(index).use { page ->
                            // 1. Create a Bitmap (High Quality)
                            // We double the width for crisp text (Screen Width * 2 is usually safe)
                            val width = 1080 
                            val height = (width * 1.414).toInt() // A4 Aspect Ratio
                            
                            val bitmap = createBitmap(width, height)

                            // 2. Clear background to White (otherwise it's transparent/black)
                            bitmap.eraseColor(Color.WHITE)

                            // 3. Render
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                            bitmap
                        }
                    }
                }.awaitAll() // Wait for all pages to finish

                renderer.close()
                fileDescriptor.close()
                
                return@withContext bitmapList

            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext emptyList()
            }
        }
    }
}