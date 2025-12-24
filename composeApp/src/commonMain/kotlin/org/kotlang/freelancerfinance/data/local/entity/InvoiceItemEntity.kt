package org.kotlang.freelancerfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoice_items")
data class InvoiceItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val invoiceId: Long,
    val name: String,
    val description: String?,
    val quantity: Double,
    val unitPrice: Double,
    val taxRate: Double
)