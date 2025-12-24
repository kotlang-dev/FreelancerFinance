package org.kotlang.freelancerfinance.domain.model

data class Invoice(
    val id: Long = 0,
    val invoiceNumber: String,
    val issueDate: Long,
    val dueDate: Long,
    val status: InvoiceStatus,

    val client: ClientSnapshot,
    val businessProfile: BusinessSnapshot,
    val lineItems: List<InvoiceLineItem>,

    val subTotal: Double,
    val taxAmount: Double,
    val totalAmount: Double
)

enum class InvoiceStatus { DRAFT, SENT, PAID, CANCELLED }

data class ClientSnapshot(
    val originalClientId: Long?,
    val name: String,
    val address: String,
    val gstin: String?,
    val state: String
)

data class BusinessSnapshot(
    val name: String,
    val address: String,
    val gstin: String?,
    val logoUrl: String?
)

data class InvoiceLineItem(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val quantity: Double,
    val unitPrice: Double,
    val taxRate: Double
) {
    val total: Double
        get() = (quantity * unitPrice) * (1 + taxRate / 100)
}

data class InvoiceSummary(
    val id: Long,
    val invoiceNumber: String,
    val date: Long,
    val totalAmount: Double,
    val clientName: String
)