package org.kotlang.freelancerfinance.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.repository.PdfGenerator
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DesktopPdfGenerator : PdfGenerator {

    override suspend fun generateInvoicePdf(invoice: Invoice): String {
        return withContext(Dispatchers.IO) {
            val document = PDDocument()
            val page = PDPage()
            document.addPage(page)

            val pageHeight = page.mediaBox.height

            PDPageContentStream(document, page).use { contentStream ->
                contentStream.apply {

                    var yPosition = pageHeight - 50f

                    // --- 1. Header ---
                    beginText()
                    setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 24f)
                    newLineAtOffset(50f, yPosition)
                    showText("INVOICE")
                    endText()

                    // --- 2. Details (Right Aligned) ---
                    beginText()
                    setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA), 12f)
                    newLineAtOffset(400f, yPosition)
                    showText("No: ${invoice.invoiceNumber}")
                    newLineAtOffset(0f, -15f)
                    showText(
                        "Date: ${
                            SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(
                                Date(
                                    invoice.date
                                )
                            )
                        }"
                    )
                    endText()

                    yPosition -= 60f

                    // --- 3. Bill To ---
                    beginText()
                    setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14f)
                    newLineAtOffset(50f, yPosition)
                    showText("Bill To:")
                    endText()

                    yPosition -= 20f

                    beginText()
                    setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA), 12f)
                    newLineAtOffset(50f, yPosition)
                    showText(invoice.client.name)
                    newLineAtOffset(0f, -15f)
                    showText(invoice.client.address)
                    if (invoice.client.gstin != null) {
                        newLineAtOffset(0f, -15f)
                        showText("GSTIN: ${invoice.client.gstin}")
                    }
                    endText()

                    yPosition -= 50f

                    // --- 4. Table Header --- Draw Line
                    moveTo(50f, yPosition)
                    lineTo(550f, yPosition)
                    stroke()

                    yPosition -= 15f

                    beginText()
                    setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12f)
                    newLineAtOffset(50f, yPosition)
                    showText("Description")
                    newLineAtOffset(250f, 0f)
                    showText("Qty")
                    newLineAtOffset(60f, 0f)
                    showText("Price")
                    newLineAtOffset(80f, 0f)
                    showText("Total")
                    endText()

                    yPosition -= 25f

                    // --- 5. Items ---
                    setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA), 12f)

                    for (item in invoice.items) {
                        // Description
                        beginText()
                        newLineAtOffset(50f, yPosition)
                        showText(item.description)
                        endText()

                        // Qty
                        beginText()
                        newLineAtOffset(300f, yPosition)
                        showText(item.quantity.toString())
                        endText()

                        // Price
                        beginText()
                        newLineAtOffset(360f, yPosition)
                        showText(item.unitPrice.toString())
                        endText()

                        // Total
                        beginText()
                        newLineAtOffset(440f, yPosition)
                        showText(item.amount.toString())
                        endText()

                        yPosition -= 20f
                    }

                    // --- 6. Totals ---
                    yPosition -= 20f
                    moveTo(50f, yPosition)
                    lineTo(550f, yPosition)
                    stroke()

                    yPosition -= 20f

                    beginText()
                    setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12f)
                    newLineAtOffset(300f, yPosition)
                    showText("Subtotal:  ${invoice.subTotal}")
                    newLineAtOffset(0f, -20f)
                    showText("Tax:       ${invoice.taxAmount}")
                    newLineAtOffset(0f, -25f)
                    setFont(PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16f)
                    showText("Total:    INR ${invoice.totalAmount}")
                    endText()
                }
            }

            // 7. Save to Downloads folder
            val userHome = System.getProperty("user.home")
            val fileName = "Invoice_${invoice.invoiceNumber}.pdf"
            // Works on Windows & Mac
            val file = File("$userHome/Downloads/$fileName")

            document.save(file)
            document.close()

            file.absolutePath
        }
    }
}