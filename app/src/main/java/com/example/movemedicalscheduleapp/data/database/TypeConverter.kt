package com.example.movemedicalscheduleapp.data.database

import androidx.room.TypeConverter
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset


class TypeConverter {
    @TypeConverter
    fun longToLocalDateTime(long: Long?): LocalDateTime? {
        return long?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun localDateTimeToLong(localDateTime: LocalDateTime?): Long? {
        return localDateTime?.withNano(0)?.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun intToApptLocation(int: Int?): ApptLocation? {
        return int?.let { ApptLocation.getLocationByZipCode(zipCode = int) }
    }

    @TypeConverter
    fun apptLocationToInt(apptLocation: ApptLocation?): Int? {
        return apptLocation?.zipCode
    }

    @TypeConverter
    fun durationToLong(duration: Duration?): Long? {
        return duration?.seconds
    }

    @TypeConverter
    fun longToDuration(long: Long?): Duration? {
        return long?.let{Duration.ofSeconds(long)}
    }


}