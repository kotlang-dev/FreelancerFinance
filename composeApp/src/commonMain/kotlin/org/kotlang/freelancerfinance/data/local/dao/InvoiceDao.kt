package org.kotlang.freelancerfinance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.data.local.entity.InvoiceEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceItemEntity
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

    @Transaction
    @Query("SELECT * FROM invoices ORDER BY date DESC")
    fun getAllInvoices(): Flow<List<InvoiceWithItems>>
}