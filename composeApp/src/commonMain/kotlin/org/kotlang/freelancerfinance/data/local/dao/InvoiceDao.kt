package org.kotlang.freelancerfinance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.data.local.entity.InvoiceEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceItemEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceSummaryTuple
import org.kotlang.freelancerfinance.data.local.relation.InvoiceWithItems

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
            invoices.issueDate as date, 
            invoices.totalAmount, 
            clients.name as clientName
        FROM invoices
        INNER JOIN clients ON invoices.client_originalClientId = clients.id
        ORDER BY invoices.issueDate DESC
    """)
    fun getAllInvoicesSummary(): Flow<List<InvoiceSummaryTuple>>

    @Query("""
        SELECT 
            invoices.id, 
            invoices.invoiceNumber, 
            invoices.issueDate as date, 
            invoices.totalAmount, 
            clients.name as clientName
        FROM invoices
        INNER JOIN clients ON invoices.client_originalClientId = clients.id
        ORDER BY invoices.issueDate DESC
        LIMIT :limit
    """)
    fun getRecentInvoicesSummary(limit: Int): Flow<List<InvoiceSummaryTuple>>

    @Transaction
    @Query("SELECT * FROM invoices WHERE id = :id")
    fun getInvoiceWithItems(id: Long): Flow<InvoiceWithItems?>
}

//TODO think of combining these two InvoicesSummary fn into one.