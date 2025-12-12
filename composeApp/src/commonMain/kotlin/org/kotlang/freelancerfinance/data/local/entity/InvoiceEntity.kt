package org.kotlang.freelancerfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val invoiceNumber: String,
    val clientId: Long,
    val date: Long,
    val status: Int,
    val totalAmount: Double
)