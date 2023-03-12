package com.example.movemedicalscheduleapp.ui.popups

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.ui.theme.MoveMedicalScheduleAppTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

class ConfirmCancelPopupKtTest{
    private var closePerformed = false
    private var cancelPerformed = false
    private var canceledAppointment: Appointment? = null
    private val appointment: Appointment = Appointment(
        title = "TEST TITLE",
        datetime = LocalDateTime.now(),
        duration = Duration.ofHours(2L),
        location = ApptLocation.PARK_CITY,
        description = "THIS IS A DESCRIPTION"
    )
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun before(){
        closePerformed = false
        cancelPerformed = false
        canceledAppointment = null
        // Start the app
        composeTestRule.setContent {
            MoveMedicalScheduleAppTheme() {
                ConfirmCancelPopup(
                    appointment = appointment,
                    onCancelAppointment = {appointment ->
                        canceledAppointment = appointment
                        cancelPerformed = true
                    },
                    onClose = {closePerformed = true}
                )
            }
        }
    }

    @Test
    fun cancelAppointment(){
        //Assert Appointment name is displayed in dialog
        composeTestRule.onNode(hasText(text = appointment.title, substring = true)).assertIsDisplayed()
        //Assert Buttons displayed
        val keepAppointmentLabel = composeTestRule.activity.getString(R.string.keep_appointment)
        val keepAppointmentButton = composeTestRule.onNodeWithText(keepAppointmentLabel).assertIsDisplayed().assertHasClickAction()

        val cancelAppointmentLabel = composeTestRule.activity.getString(R.string.cancel_appointment)
        val cancelAppointmentButton = composeTestRule.onNodeWithText(cancelAppointmentLabel).assertIsDisplayed().assertHasClickAction()

        cancelAppointmentButton.performClick()
        assertThat(canceledAppointment == appointment && cancelPerformed && closePerformed).isTrue()
    }

    @Test
    fun keepAppointment(){
        //Assert Appointment name is displayed in dialog
        composeTestRule.onNode(hasText(text = appointment.title, substring = true)).assertIsDisplayed()
        //Assert Buttons displayed
        val keepAppointmentLabel = composeTestRule.activity.getString(R.string.keep_appointment)
        val keepAppointmentButton = composeTestRule.onNodeWithText(keepAppointmentLabel).assertIsDisplayed().assertHasClickAction()

        val cancelAppointmentLabel = composeTestRule.activity.getString(R.string.cancel_appointment)
        val cancelAppointmentButton = composeTestRule.onNodeWithText(cancelAppointmentLabel).assertIsDisplayed().assertHasClickAction()

        keepAppointmentButton.performClick()
        assertThat(canceledAppointment == null && !cancelPerformed && closePerformed).isTrue()
    }

}