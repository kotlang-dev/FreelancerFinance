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

fun String?.getInitials(): String {
    if (this.isNullOrBlank()) return ""

    val parts = this.trim().split("\\s+".toRegex())

    return when {
        parts.isEmpty() -> ""
        // Case: "Uber" -> "UB"
        parts.size == 1 -> {
            val name = parts[0]
            if (name.length >= 2) name.take(2).uppercase() else name.uppercase()
        }
        // Case: "Mohammad Arif" -> "MA"
        else -> {
            val first = parts[0].firstOrNull() ?: ""
            val second = parts[1].firstOrNull() ?: ""
            "$first$second".uppercase()
        }
    }
}