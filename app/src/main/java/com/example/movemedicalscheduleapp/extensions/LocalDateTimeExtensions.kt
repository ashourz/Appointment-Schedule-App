package com.example.movemedicalscheduleapp.extensions

import android.content.Context
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle
import java.time.format.FormatStyle

fun LocalDateTime.toSQLLong(): Long {
    return this.toEpochSecond(ZoneOffset.UTC)
}

fun getTodaySQLLong(): Long {
    return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).toSQLLong()
}

fun getTomorrowSQLLong(): Long {
    return LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.MIDNIGHT).toSQLLong()
}

fun LocalDate.toEpochMilli(): Long {
    return this.atStartOfDay()
        .atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
        .toInstant().toEpochMilli()
}

fun LocalDate.toDisplayFormat(localContext: Context): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    return this.format(formatter).format(localContext.resources.configuration.locales)
}

fun LocalTime.toDisplayFormat(localContext: Context): String {
    return this.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
}

fun Duration.toDisplayFormat(): String {
    val hours = this.toHours()
    val minutes = this.toMinutes() - (60L* hours)
    return "${if(hours > 0L){"$hours Hour${if(hours != 1L){"s"}else{""}} "}else{""}}$minutes Minute${if(minutes != 1L){"s"}else{""}}"
}