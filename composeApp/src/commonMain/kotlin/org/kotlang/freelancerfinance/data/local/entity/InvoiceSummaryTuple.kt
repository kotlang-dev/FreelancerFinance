package org.kotlang.freelancerfinance.data.local.entity

data class InvoiceSummaryTuple(
    val id: Long,
    val invoiceNumber: String,
    val date: Long,
    val totalAmount: Double,
    val clientName: String
)