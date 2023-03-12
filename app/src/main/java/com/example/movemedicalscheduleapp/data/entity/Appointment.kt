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

package com.example.movemedicalscheduleapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.LocalDateTime

@Entity(tableName = "appointment_table")
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid") val rowid : Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "datetime") val datetime: LocalDateTime,
    @ColumnInfo(name = "location") val location: ApptLocation,
    @ColumnInfo(name = "duration") val duration: Duration,
    @ColumnInfo(name = "description") val description: String
)