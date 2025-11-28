package org.kotlang.freelancerfinance.domain.repository

import org.kotlang.freelancerfinance.domain.model.Invoice

interface PdfGenerator {
    suspend fun generateInvoicePdf(invoice: Invoice): String
}