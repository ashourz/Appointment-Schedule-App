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

package com.example.movemedicalscheduleapp.extensions

import android.content.Context
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.Appointment

fun List<Appointment>.toExistingApptErrorMessage(localContext: Context): String {
    var errorMessage = ""
    this.firstOrNull()?.let { firstAppointment ->
        errorMessage = "${this.count()} Appointment${if(this.count() > 1){"s"}else{""}} " +
                "Already Exist${if(this.count() == 1){"s"}else{""}} for " +
                "${firstAppointment.getLocationDeclarationString(localContext)} Within the Selected Time Period."
    }?:{
        errorMessage = localContext.getString(R.string.failed_existing_appt_error_message)
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