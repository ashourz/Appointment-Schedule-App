package com.example.movemedicalscheduleapp.extensions

import android.content.Context
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation

fun List<Appointment>.toExistingApptErrorMessage(localContext: Context): String {
    val location: ApptLocation = (this.firstOrNull()?.location)?:ApptLocation.UNKNOWN
    var errorMessage = "Appointments Already Exist for Location: ${location.getDisplayName(localContext)} Within the Below Time Periods."
    this.forEach { appointment ->
        errorMessage = errorMessage
            .plus("\n${appointment.datetime.toLocalDate().toDisplayFormat(localContext)}: ")
            .plus("${appointment.datetime.toLocalTime().toDisplayFormat(localContext)} - ")
            .plus(appointment.datetime.plus(appointment.duration).toLocalTime().toDisplayFormat(localContext))

    }
    return errorMessage
}