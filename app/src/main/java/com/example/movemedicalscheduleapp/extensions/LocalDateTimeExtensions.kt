package com.example.movemedicalscheduleapp.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

fun LocalDateTime.toSQLLong(): Long {
    return this.toEpochSecond(ZoneOffset.UTC)
}

fun getTodaySQLLong(): Long {
    return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).toSQLLong()
}

fun getTomorrowSQLLong(): Long {
    return LocalDateTime.of(LocalDate.now().plusDays(1L), LocalTime.MIDNIGHT).toSQLLong()
}
