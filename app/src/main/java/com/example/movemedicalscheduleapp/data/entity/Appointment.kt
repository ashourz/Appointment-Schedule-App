package com.example.movemedicalscheduleapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movemedicalscheduleapp.enums.ApptLocation
import java.time.Duration
import java.time.LocalDateTime

@Entity(tableName = "appointment_table")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid") val rowid : Long = 0,
    @ColumnInfo(name = "datetime") val datetime: LocalDateTime,
    @ColumnInfo(name = "location") val location: ApptLocation,
    @ColumnInfo(name = "duration") val duration: Duration,
    @ColumnInfo(name = "description") val description: String
)