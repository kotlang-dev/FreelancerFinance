package org.kotlang.freelancerfinance.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.domain.model.Invoice

interface InvoiceRepository {
    fun getAllInvoices(): Flow<List<Invoice>>
    suspend fun createInvoice(invoice: Invoice)
}