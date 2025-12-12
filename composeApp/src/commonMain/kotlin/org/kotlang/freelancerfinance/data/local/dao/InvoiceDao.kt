package org.kotlang.freelancerfinance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.data.local.entity.InvoiceEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceItemEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceSummaryTuple

@Dao
interface InvoiceDao {

    @Insert
    suspend fun insertInvoice(invoice: InvoiceEntity): Long

    @Insert
    suspend fun insertItems(items: List<InvoiceItemEntity>)

    @Transaction
    suspend fun createInvoiceWithItems(invoice: InvoiceEntity, items: List<InvoiceItemEntity>) {
        val invoiceId = insertInvoice(invoice)
        val itemsWithId = items.map { it.copy(invoiceId = invoiceId) }
        insertItems(itemsWithId)
    }

    @Query("SELECT SUM(totalAmount) FROM invoices")
    fun getTotalRevenue(): Flow<Double?>

    @Query("""
        SELECT 
            invoices.id, 
            invoices.invoiceNumber, 
            invoices.date, 
            invoices.totalAmount, 
            clients.name as clientName
        FROM invoices
        INNER JOIN clients ON invoices.clientId = clients.id
        ORDER BY invoices.date DESC
    """)
    fun getAllInvoicesSummary(): Flow<List<InvoiceSummaryTuple>>

    @Query("""
        SELECT 
            invoices.id, 
            invoices.invoiceNumber, 
            invoices.date, 
            invoices.totalAmount, 
            clients.name as clientName
        FROM invoices
        INNER JOIN clients ON invoices.clientId = clients.id
        ORDER BY invoices.date DESC
        LIMIT :limit
    """)
    fun getRecentInvoicesSummary(limit: Int): Flow<List<InvoiceSummaryTuple>>
}