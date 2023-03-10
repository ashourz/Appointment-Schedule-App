package com.example.movemedicalscheduleapp.extensions

import android.content.Context
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation

fun List<Appointment>.toExistingApptErrorMessage(localContext: Context): String {
    var errorMessage = ""
    this.firstOrNull()?.let { firstAppointment ->
        errorMessage = "Appointments Already Exist for ${firstAppointment.getLocationDeclarationString(localContext)} Within the Below Time Periods."
        this.forEach { appointment ->
            errorMessage = errorMessage
                .plus("\n${appointment.datetime.toLocalDate().toDisplayFormat(localContext)}: ")
                .plus(appointment.getTimeSpanString(localContext))
        }
    }?:{
        errorMessage = "ERROR GETTING EXISTING APPOINTMENTS MESSAGE"
    }
    return errorMessage
}

fun Appointment.getLocationDeclarationString(localContext: Context): String {
    return "Location: ${this.location.getDisplayName(localContext)}"
}

fun Appointment.getTimeSpanString(localContext: Context): String {
    return "${this.datetime.toLocalTime().toDisplayFormat(localContext)} - "
        .plus(this.datetime.plus(this.duration).toLocalTime().toDisplayFormat(localContext))
}