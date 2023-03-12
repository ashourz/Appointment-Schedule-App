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

import android.app.Application
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.ui.ui_data_class.TempAppointmentProperties
import com.google.common.truth.Truth.assertThat
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.Duration
import java.time.LocalDateTime

@RunWith(JUnit4::class)
class AddUpdateAppointmentValidationKtTest {

    @Test
    fun invalidDataFailure(){
        val tempAppointmentProperties = TempAppointmentProperties()
        var titleError: String? = null
        var durationError: String? = null
        var locationError: String? = null

        addUpdateAppointmentValidation(
            tempAppointmentProperties = tempAppointmentProperties,
            updateAppointmentTitleError = {isError ->
                if(isError){
                    titleError = "error"
                }else{
                    titleError = null
                }},
            updateAppointmentDurationError = {isError ->
                if(isError){
                    durationError = "error"
                }else{
                    durationError = null
                }},
            updateAppointmentLocationError = {isError ->
                if(isError){
                    locationError = "error"
                }else{
                    locationError = null
                }},
            updateUpdateError = {})
        assertThat(
            titleError != null &&
            durationError != null &&
            locationError != null
        ).isTrue()
    }

    @Test
    fun validDataValidation(){
        val datetime = LocalDateTime.now()
        val tempAppointmentProperties = TempAppointmentProperties(
            editingAppointment = Appointment(
                title = "Test Title",
                datetime = datetime,
                duration = Duration.ofMinutes(15L),
                location = ApptLocation.DALLAS,
                description = ""
            ),
            title = "Test Title 2",
            date = datetime.toLocalDate(),
            time = datetime.toLocalTime(),
            duration = Duration.ofMinutes(15L),
            location = ApptLocation.DALLAS,
            description = ""

        )
        var titleError: String? = null
        var durationError: String? = null
        var locationError: String? = null
        var updateError: String? = null

        addUpdateAppointmentValidation(
            tempAppointmentProperties = tempAppointmentProperties,
            updateAppointmentTitleError = {isError ->
                if(isError){
                    titleError = "error"
                }else{
                    titleError = null
                }},
            updateAppointmentDurationError = {isError ->
                if(isError){
                    durationError = "error"
                }else{
                    durationError = null
                }},
            updateAppointmentLocationError = {isError ->
                if(isError){
                    locationError = "error"
                }else{
                    locationError = null
                }},
            updateUpdateError = {isError ->
                if(isError){
                    updateError = "error"
                }else{
                    updateError = null
                }}
        )
        assertThat(
            titleError == null &&
            durationError == null &&
            locationError == null &&
            updateError == null
        ).isTrue()
    }

    @Test
    fun noUpdateDataFailure(){
        val datetime = LocalDateTime.now()
        val tempAppointmentProperties = TempAppointmentProperties(
            editingAppointment = Appointment(
                title = "Test Title",
                datetime = datetime,
                duration = Duration.ofMinutes(15L),
                location = ApptLocation.DALLAS,
                description = ""
            ),
            title = "Test Title",
            date = datetime.toLocalDate(),
            time = datetime.toLocalTime(),
            duration = Duration.ofMinutes(15L),
            location = ApptLocation.DALLAS,
            description = ""

        )
        var updateError: String? = null

        addUpdateAppointmentValidation(
            tempAppointmentProperties = tempAppointmentProperties,
            updateAppointmentTitleError = {},
            updateAppointmentDurationError = {},
            updateAppointmentLocationError = {},
            updateUpdateError = {isError ->
                if(isError){
                    updateError = "error"
                }else{
                    updateError = null
                }})
        assertThat(
            updateError != null
        ).isTrue()
    }
}
