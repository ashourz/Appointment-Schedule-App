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

package com.example.movemedicalscheduleapp.ui.components.validation

import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.ui.ui_data_class.TempAppointmentProperties
import java.time.Duration

/**
 * Validation used in UpsertScaffold on Add/Update Button execution
 * */
fun addUpdateAppointmentValidation(
    tempAppointmentProperties: TempAppointmentProperties,
    updateAppointmentTitleError: (Boolean) -> Unit,
    updateAppointmentDurationError: (Boolean) -> Unit,
    updateAppointmentLocationError: (Boolean) -> Unit,
    updateUpdateError: (Boolean) -> Unit

): Boolean {
    var validationBoolean = true
    //region: Reset Error States
    updateAppointmentTitleError(false)
    updateAppointmentDurationError(false)
    updateAppointmentLocationError(false)
    updateUpdateError(false)
    //endregion
    if(tempAppointmentProperties.title.isNullOrBlank()){
        updateAppointmentTitleError(true)
       validationBoolean = false
    }
    if (tempAppointmentProperties.duration == Duration.ZERO) {
        updateAppointmentDurationError(true)
        validationBoolean = false
    }
    if (tempAppointmentProperties.location == ApptLocation.UNKNOWN) {
        updateAppointmentLocationError(true)
        validationBoolean = false
    }
    if (tempAppointmentProperties.editingAppointment != null &&
        tempAppointmentProperties.title ==        tempAppointmentProperties.editingAppointment.title &&
        tempAppointmentProperties.date ==        tempAppointmentProperties.editingAppointment.datetime.toLocalDate() &&
        tempAppointmentProperties.time ==        tempAppointmentProperties.editingAppointment.datetime.toLocalTime() &&
        tempAppointmentProperties.duration ==               tempAppointmentProperties.editingAppointment.duration &&
        tempAppointmentProperties.location ==    tempAppointmentProperties.editingAppointment.location &&
        tempAppointmentProperties.description ==            tempAppointmentProperties.editingAppointment.description
    ) {
        updateUpdateError(true)
        validationBoolean = false
    }
    //Validate For No Errors
    return validationBoolean
}
