package com.example.movemedicalscheduleapp.ui.components.validation

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
    companion object {
        init {}

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            println("BEFORE CLASS")
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            println("AFTER CLASS")
        }
    }

    @Before
    fun before() {
        println("BEFORE")
    }

    @After
    fun after() {
        println("AFTER")
    }

    @Test
    fun invalidDataFailure(){
        val tempAppointmentProperties = TempAppointmentProperties()
        var titleError: String? = null
        var durationError: String? = null
        var locationError: String? = null

        addUpdateAppointmentValidation(
            tempAppointmentProperties = tempAppointmentProperties,
            updateAppointmentTitleError = {titleError = it},
            updateAppointmentDurationError = {durationError = it},
            updateAppointmentLocationError = {locationError = it},
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
            updateAppointmentTitleError = {titleError = it},
            updateAppointmentDurationError = {durationError = it},
            updateAppointmentLocationError = {locationError = it},
            updateUpdateError = {updateError = it})
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
            updateUpdateError = {updateError = it})
        assertThat(
            updateError != null
        ).isTrue()
    }
}
