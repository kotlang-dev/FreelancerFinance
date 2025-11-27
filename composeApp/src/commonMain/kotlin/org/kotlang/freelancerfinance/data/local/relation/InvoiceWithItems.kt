package org.kotlang.freelancerfinance.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import org.kotlang.freelancerfinance.data.local.entity.InvoiceEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceItemEntity

data class InvoiceWithItems(
    @Embedded val invoice: InvoiceEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "invoiceId"
    )
    val items: List<InvoiceItemEntity>
)