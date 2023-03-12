/*
 * Copyright 2023 Zakaraya Thomas Ashour
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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