package org.kotlang.freelancerfinance.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.model.InvoiceSummary

interface InvoiceRepository {
    fun getAllInvoicesSummary(): Flow<List<InvoiceSummary>>
    fun getRecentInvoicesSummary(count: Int = 5): Flow<List<InvoiceSummary>>
    fun getTotalRevenue(): Flow<Double>
    suspend fun createInvoice(invoice: Invoice): Long
    fun getInvoiceById(id: Long): Flow<Invoice?>
}