package com.example.movemedicalscheduleapp.ui.components.validation

import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.ui.ui_data_class.TempAppointmentProperties
import java.time.Duration

fun addUpdateAppointmentValidation(
    tempAppointmentProperties: TempAppointmentProperties,
    updateAppointmentTitleError: (String?) -> Unit,
    updateAppointmentDurationError: (String?) -> Unit,
    updateAppointmentLocationError: (String?) -> Unit,
    updateUpdateError: (String?) -> Unit

): Boolean {
    var validationBoolean = true
    //region: Reset Error States
    updateAppointmentTitleError(null)
    updateAppointmentDurationError(null)
    updateAppointmentLocationError(null)
    updateUpdateError(null)
    //endregion
    if(tempAppointmentProperties.appointmentTitle.isNullOrBlank()){
        updateAppointmentTitleError("Appointment Title Cannot Be Blank")
       validationBoolean = false
    }
    if (tempAppointmentProperties.duration == Duration.ZERO) {
        updateAppointmentDurationError("Appointment Duration Must Be Greater Than 0 Minutes")
        validationBoolean = false
    }
    if (tempAppointmentProperties.appointmentLocation == ApptLocation.UNKNOWN) {
        updateAppointmentLocationError("Appointment Location Cannot Be Blank")
        validationBoolean = false
    }
    if (tempAppointmentProperties.editingAppointment != null &&
        tempAppointmentProperties.appointmentTitle ==        tempAppointmentProperties.editingAppointment.title &&
        tempAppointmentProperties.appointmentDate ==        tempAppointmentProperties.editingAppointment.datetime.toLocalDate() &&
        tempAppointmentProperties.appointmentTime ==        tempAppointmentProperties.editingAppointment.datetime.toLocalTime() &&
        tempAppointmentProperties.duration ==               tempAppointmentProperties.editingAppointment.duration &&
        tempAppointmentProperties.appointmentLocation ==    tempAppointmentProperties.editingAppointment.location &&
        tempAppointmentProperties.description ==            tempAppointmentProperties.editingAppointment.description
    ) {
        updateUpdateError("No Changes Have Been Made")
        validationBoolean = false
    }
    //Validate For No Errors
    return validationBoolean
}
