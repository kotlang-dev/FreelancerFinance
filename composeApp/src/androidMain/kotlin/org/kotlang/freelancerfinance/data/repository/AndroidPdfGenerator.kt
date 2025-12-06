package org.kotlang.freelancerfinance.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.kotlang.freelancerfinance.domain.model.BusinessProfile
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.repository.PdfGenerator
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AndroidPdfGenerator(
    private val context: Context
) : PdfGenerator {

    override suspend fun generateInvoicePdf(invoice: Invoice, profile: BusinessProfile): String {
        return withContext(Dispatchers.IO) {
            val pdfDocument = PdfDocument()
            
            // 1. Create a Page (A4 standard size in PostScript points: 595 x 842)
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()

            // --- DRAWING LOGIC STARTS ---
            
            var yPosition = 50f // Start cursor at top
            
            // 1. Header "INVOICE"
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 24f
            paint.color = Color.BLACK
            canvas.drawText("INVOICE", 40f, yPosition, paint)

            // A. Draw Logo (Top Right) using profile.logoPath
            if (profile.logoPath != null) {
                val bitmap = BitmapFactory.decodeFile(profile.logoPath)
                if (bitmap != null) {
                    // Resize logic (Max width 100)
                    val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
                    val targetWidth = 100f
                    val targetHeight = targetWidth / aspectRatio

                    // Draw at X=450 (Right), Y=20
                    canvas.drawBitmap(
                        bitmap.scale(targetWidth.toInt(), targetHeight.toInt(), false),
                        450f, 20f, paint
                    )
                }
            }

            // B. Draw Seller Details (Top Left)
            paint.color = Color.BLACK
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textSize = 18f
            canvas.drawText(profile.businessName, 40f, yPosition, paint)

            yPosition += 20f
            paint.textSize = 12f
            paint.typeface = Typeface.DEFAULT
            canvas.drawText(profile.address, 40f, yPosition, paint)
            yPosition += 15f
            canvas.drawText("${profile.city} - ${profile.pincode}", 40f, yPosition, paint)
            yPosition += 15f
            if (profile.gstin != null) {
                canvas.drawText("GSTIN: ${profile.gstin}", 40f, yPosition, paint)
            }
            yPosition += 15f
            canvas.drawText("PAN: ${profile.panNumber}", 40f, yPosition, paint)
            
            // 2. Invoice Details (Right Aligned)
            paint.textSize = 12f
            paint.typeface = Typeface.DEFAULT
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            canvas.drawText("No: ${invoice.invoiceNumber}", 400f, yPosition, paint)
            yPosition += 20f
            canvas.drawText("Date: ${dateFormat.format(Date(invoice.date))}", 400f, yPosition, paint)

            yPosition += 40f

            // 3. Bill To (Client)
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Bill To:", 40f, yPosition, paint)
            yPosition += 20f
            paint.typeface = Typeface.DEFAULT
            canvas.drawText(invoice.client.name, 40f, yPosition, paint)
            yPosition += 15f
            canvas.drawText(invoice.client.address, 40f, yPosition, paint)
            yPosition += 15f
            if (invoice.client.gstin != null) {
                canvas.drawText("GSTIN: ${invoice.client.gstin}", 40f, yPosition, paint)
            }

            yPosition += 30f

            // 4. Table Header
            paint.style = Paint.Style.FILL
            paint.color = Color.LTGRAY
            canvas.drawRect(40f, yPosition - 15f, 550f, yPosition + 5f, paint)
            
            paint.color = Color.BLACK
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Description", 50f, yPosition, paint)
            canvas.drawText("Qty", 300f, yPosition, paint)
            canvas.drawText("Price", 380f, yPosition, paint)
            canvas.drawText("Total", 480f, yPosition, paint)
            
            yPosition += 25f

            // 5. Items Loop
            paint.typeface = Typeface.DEFAULT
            for (item in invoice.items) {
                canvas.drawText(item.description, 50f, yPosition, paint)
                canvas.drawText(item.quantity.toString(), 300f, yPosition, paint)
                canvas.drawText(item.unitPrice.toString(), 380f, yPosition, paint)
                canvas.drawText(item.amount.toString(), 480f, yPosition, paint)
                
                yPosition += 20f
            }
            
            // 6. Line Separator
            paint.strokeWidth = 1f
            canvas.drawLine(40f, yPosition, 550f, yPosition, paint)
            yPosition += 30f

            // 7. Totals
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Subtotal:", 350f, yPosition, paint)
            canvas.drawText(invoice.subTotal.toString(), 480f, yPosition, paint)
            yPosition += 20f
            
            canvas.drawText("Tax:", 350f, yPosition, paint)
            canvas.drawText(invoice.taxAmount.toString(), 480f, yPosition, paint)
            yPosition += 25f
            
            paint.textSize = 16f
            canvas.drawText("Total:", 350f, yPosition, paint)
            canvas.drawText("INR ${invoice.totalAmount}", 480f, yPosition, paint)

            // --- DRAWING ENDS ---

            pdfDocument.finishPage(page)

            // 8. Save File
            // We save to app-specific storage (No permissions needed)
            val fileName = "Invoice_${invoice.invoiceNumber}.pdf"
            val file = File(context.getExternalFilesDir(null), fileName)
            
            try {
                pdfDocument.writeTo(FileOutputStream(file))
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            } finally {
                pdfDocument.close()
            }
            
            file.absolutePath
        }
    }
}