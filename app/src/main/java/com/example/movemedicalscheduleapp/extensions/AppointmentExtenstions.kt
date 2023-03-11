package com.example.movemedicalscheduleapp.extensions

import android.content.Context
import com.example.movemedicalscheduleapp.data.entity.Appointment

fun List<Appointment>.toExistingApptErrorMessage(localContext: Context): String {
    var errorMessage = ""
    this.firstOrNull()?.let { firstAppointment ->
        errorMessage = "${this.count()} Appointment${if(this.count() > 1){"s"}else{""}} " +
                "Already Exist${if(this.count() == 1){"s"}else{""}} for " +
                "${firstAppointment.getLocationDeclarationString(localContext)} Within the Selected Time Period."
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