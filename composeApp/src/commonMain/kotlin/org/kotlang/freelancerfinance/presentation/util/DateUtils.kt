package org.kotlang.freelancerfinance.presentation.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Long?.toFormattedDateString(): String {
    val timeMillis = this ?: Clock.System.now().toEpochMilliseconds()
    val instant = Instant.fromEpochMilliseconds(timeMillis)

    val date = instant.toLocalDateTime(TimeZone.UTC).date

    val formatter = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        day()
        char(',')
        char(' ')
        year()
    }
    return formatter.format(date)
}