package org.kotlang.freelancerfinance.presentation.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.NumberFormat
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


data class InvoiceDateUi(
    val month: String,
    val day: String,
    val year: String
)

// Extension function: Long -> InvoiceDateUi
@OptIn(ExperimentalTime::class)
fun Long.toInvoiceDateUi(): InvoiceDateUi {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    
    return InvoiceDateUi(
        month = localDate.month.name.take(3),
        day = localDate.day.toString(),
        year = localDate.year.toString()
    )
}

fun Double.toIndianCurrency(): String {
    val formatter = NumberFormat.getNumberInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = 0
    return formatter.format(this)
}