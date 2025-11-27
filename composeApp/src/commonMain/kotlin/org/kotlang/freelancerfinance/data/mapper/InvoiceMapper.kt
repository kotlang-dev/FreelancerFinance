package org.kotlang.freelancerfinance.data.mapper

import org.kotlang.freelancerfinance.data.local.entity.InvoiceEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceItemEntity
import org.kotlang.freelancerfinance.data.local.relation.InvoiceWithItems
import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.model.InvoiceLineItem
import org.kotlang.freelancerfinance.domain.model.InvoiceStatus

// 1. Entity -> Domain (Reading from DB)
fun InvoiceWithItems.toDomain(client: Client): Invoice {
    val lineItems = items.map { it.toDomain() }

    val subTotal = lineItems.sumOf { it.amount }
    val totalTax = lineItems.sumOf { it.amount * (it.taxRate / 100.0) }

    return Invoice(
        id = invoice.id,
        invoiceNumber = invoice.invoiceNumber,
        client = client,
        date = invoice.date,
        items = lineItems,
        status = InvoiceStatus.entries.getOrElse(invoice.status) { InvoiceStatus.DRAFT },
        subTotal = subTotal,
        taxAmount = totalTax,
        totalAmount = subTotal + totalTax
    )
}

fun InvoiceItemEntity.toDomain(): InvoiceLineItem {
    return InvoiceLineItem(
        id = id,
        description = description,
        quantity = quantity,
        unitPrice = unitPrice,
        taxRate = taxRate
    )
}

// 2. Domain -> Entity
fun Invoice.toEntity(clientId: Long): InvoiceEntity {
    return InvoiceEntity(
        id = id,
        invoiceNumber = invoiceNumber,
        clientId = clientId,
        date = date,
        status = status.ordinal
    )
}

fun InvoiceLineItem.toEntity(invoiceId: Long): InvoiceItemEntity {
    return InvoiceItemEntity(
        id = id,
        invoiceId = invoiceId,
        description = description,
        quantity = quantity,
        unitPrice = unitPrice,
        taxRate = taxRate
    )
}