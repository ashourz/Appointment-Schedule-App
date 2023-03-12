package com.example.movemedicalscheduleapp.ui.ui_data_class

import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

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