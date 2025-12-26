package org.kotlang.freelancerfinance.data.mapper

import org.kotlang.freelancerfinance.data.local.entity.InvoiceEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceItemEntity
import org.kotlang.freelancerfinance.data.local.entity.InvoiceSummaryTuple
import org.kotlang.freelancerfinance.data.local.relation.InvoiceWithItems
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.model.InvoiceLineItem
import org.kotlang.freelancerfinance.domain.model.InvoiceStatus
import org.kotlang.freelancerfinance.domain.model.InvoiceSummary

fun InvoiceWithItems.toDomain(): Invoice {
    val lineItems = items.map { it.toDomain() }

    return Invoice(
        id = invoice.id,
        invoiceNumber = invoice.invoiceNumber,
        dueDate = invoice.dueDate,
        issueDate = invoice.issueDate,
        status = InvoiceStatus.valueOf(invoice.status),
        lineItems = lineItems,
        subTotal = invoice.subTotal,
        taxAmount = invoice.taxAmount,
        client = invoice.clientSnapshot,
        businessProfile = invoice.businessSnapshot,
        totalAmount = invoice.totalAmount
    )
}

fun InvoiceItemEntity.toDomain(): InvoiceLineItem {
    return InvoiceLineItem(
        id = id,
        name = name,
        description = description,
        quantity = quantity,
        unitPrice = unitPrice,
        taxRate = taxRate
    )
}

// 2. Domain -> Entity
fun Invoice.toEntity(): InvoiceEntity {
    return InvoiceEntity(
        id = id,
        invoiceNumber = invoiceNumber,
        dueDate = dueDate,
        issueDate = issueDate,
        status = status.name,
        subTotal = subTotal,
        taxAmount = taxAmount,
        totalAmount = totalAmount,
        clientSnapshot = client,
        businessSnapshot = businessProfile
    )
}

fun InvoiceLineItem.toEntity(invoiceId: Long): InvoiceItemEntity {
    return InvoiceItemEntity(
        id = id,
        invoiceId = invoiceId,
        name = name,
        description = description,
        quantity = quantity,
        unitPrice = unitPrice,
        taxRate = taxRate
    )
}

fun InvoiceSummaryTuple.toDomain(): InvoiceSummary {
    return InvoiceSummary(
        id = id,
        invoiceNumber = invoiceNumber,
        date = date,
        totalAmount = totalAmount,
        clientName = clientName
    )
}