package org.kotlang.freelancerfinance.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.kotlang.freelancerfinance.data.local.dao.ClientDao
import org.kotlang.freelancerfinance.data.local.dao.InvoiceDao
import org.kotlang.freelancerfinance.data.mapper.toDomain
import org.kotlang.freelancerfinance.data.mapper.toEntity
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.repository.InvoiceRepository

class InvoiceRepositoryImpl(
    private val invoiceDao: InvoiceDao,
    private val clientDao: ClientDao
) : InvoiceRepository {

    override fun getAllInvoices(): Flow<List<Invoice>> {
        return combine(
            invoiceDao.getAllInvoices(),
            clientDao.getAllClients()
        ) { invoiceRelations, clientEntities ->
            
            invoiceRelations.mapNotNull { relation ->
                val clientEntity = clientEntities.find { it.id == relation.invoice.clientId }
                
                if (clientEntity != null) {
                    val clientDomain = clientEntity.toDomain()
                    relation.toDomain(clientDomain)
                } else {
                    null
                }
            }
        }
    }

    override suspend fun createInvoice(invoice: Invoice) {
        val invoiceEntity = invoice.toEntity(invoice.client.id)
        val itemEntities = invoice.items.map { it.toEntity(0) } // ID 0, DAO will fix it
        
        invoiceDao.createInvoiceWithItems(invoiceEntity, itemEntities)
    }
}