package org.kotlang.freelancerfinance.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.kotlang.freelancerfinance.domain.model.BusinessSnapshot
import org.kotlang.freelancerfinance.domain.model.ClientSnapshot

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val invoiceNumber: String,
    val issueDate: Long,
    val dueDate: Long,
    val status: String,

    val subTotal: Double,
    val taxAmount: Double,
    val totalAmount: Double,

    @Embedded(prefix = "client_")
    val clientSnapshot: ClientSnapshot,

    @Embedded(prefix = "biz_")
    val businessSnapshot: BusinessSnapshot
)

//TODO keep the tableName and dbName in a separate constant file