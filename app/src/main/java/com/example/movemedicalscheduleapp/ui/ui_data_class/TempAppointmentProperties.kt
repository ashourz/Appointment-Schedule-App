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

package com.example.movemedicalscheduleapp.ui.ui_data_class

import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

/**
 * Default Values and stored value entity for managing datavalue fields in add/update screen within upsert scaffold
 * */
data class TempAppointmentProperties(
    val editingAppointment: Appointment? = null,
    val title: String? = null,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val duration: Duration = Duration.ZERO,
    val location: ApptLocation = ApptLocation.UNKNOWN,
    val description: String? = null,
    val existingApptError: String? = null
)