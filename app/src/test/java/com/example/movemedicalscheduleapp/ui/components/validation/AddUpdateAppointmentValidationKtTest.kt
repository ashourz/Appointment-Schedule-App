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
