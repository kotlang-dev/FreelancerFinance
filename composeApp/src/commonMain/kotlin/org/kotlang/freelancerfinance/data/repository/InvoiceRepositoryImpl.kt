package org.kotlang.freelancerfinance.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kotlang.freelancerfinance.data.local.dao.InvoiceDao
import org.kotlang.freelancerfinance.data.mapper.toDomain
import org.kotlang.freelancerfinance.data.mapper.toEntity
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.model.InvoiceSummary
import org.kotlang.freelancerfinance.domain.repository.InvoiceRepository

class InvoiceRepositoryImpl(
    private val invoiceDao: InvoiceDao
) : InvoiceRepository {

    override fun getAllInvoicesSummary(): Flow<List<InvoiceSummary>> {
        return invoiceDao.getAllInvoicesSummary().map { tuples ->
            tuples.map { it.toDomain() }
        }
    }

    override fun getRecentInvoicesSummary(count: Int): Flow<List<InvoiceSummary>> {
        return invoiceDao.getRecentInvoicesSummary(count).map { tuples ->
            tuples.map { it.toDomain() }
        }
    }

    override fun getTotalRevenue(): Flow<Double> {
        return invoiceDao.getTotalRevenue().map { it ?: 0.0 }
    }

    override suspend fun createInvoice(invoice: Invoice): Long {
        val itemEntities = invoice.lineItems.map { it.toEntity(invoice.id) }
        val invoiceEntity = invoice.toEntity()

        return invoiceDao.createInvoiceWithItems(invoiceEntity, itemEntities)
    }

    override fun getInvoiceById(id: Long): Flow<Invoice?> {
        return invoiceDao.getInvoiceWithItems(id).map {
            it?.toDomain()
        }
    }
}