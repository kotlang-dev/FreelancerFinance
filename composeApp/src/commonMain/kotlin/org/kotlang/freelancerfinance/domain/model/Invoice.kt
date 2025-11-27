package org.kotlang.freelancerfinance.domain.model

enum class InvoiceStatus { DRAFT, SENT, PAID }

data class Invoice(
    val id: Long = 0,
    val invoiceNumber: String,
    val client: Client,
    val date: Long,
    val items: List<InvoiceLineItem>,
    val status: InvoiceStatus,
    val subTotal: Double,
    val taxAmount: Double,
    val totalAmount: Double
)

data class InvoiceLineItem(
    val id: Long = 0,
    val description: String,
    val quantity: Double,
    val unitPrice: Double,
    val taxRate: Double
) {
    val amount: Double get() = quantity * unitPrice
}