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

package com.example.movemedicalscheduleapp.ui.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.movemedicalscheduleapp.MainActivity
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.extensions.toDisplayFormat
import com.example.movemedicalscheduleapp.ui.ui_data_class.TempAppointmentProperties
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*
import java.util.concurrent.CountDownLatch

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@LargeTest
class UpsertScaffoldKtTest : TestCase() {
    private lateinit var activity: MainActivity
    private lateinit var dataViewModel: DataViewModel
//    private lateinit var initialDateTime: LocalDateTime

    private val appointment: Appointment = Appointment(
        title = "TEST TITLE",
        datetime = LocalDateTime.now(),
        duration = Duration.ofHours(2L),
        location = ApptLocation.PARK_CITY,
        description = "THIS IS A DESCRIPTION"
    )



    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    @get:Rule
    val instantTasExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        super.setUp()
        activity = composeTestRule.activity
        dataViewModel = DataViewModel(activity.application)
//        initialDateTime = LocalDateTime.now()
        //Cancel All Appointments
        runBlocking {
            withContext(Dispatchers.IO) {
                dataViewModel.deleteAll()
            }

        }
    }


    @After
    public override fun tearDown() {
        super.tearDown()
        //Cancel All Appointments
        runBlocking {
            withContext(Dispatchers.IO) {
                dataViewModel.deleteAll()
                dataViewModel.updateExpandedAppointmentCardId(null)
            }
        }
    }

    @Test
    fun canNavigateToInsertScaffold() = runTest{
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }

    @Test
    fun defaultValuesInTextFields() =  runTest {
        val localContext = composeTestRule.activity.applicationContext

        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()


        val tempAppointmentProperties = dataViewModel.temporaryAppointmentPropertiesFlow.first()

        //Assert Each TextField/ DropDown Present
        val titleBoxLabel = activity.getString(R.string.appointment_title)
        val titleClickableField = composeTestRule.onNodeWithText(titleBoxLabel).assertIsDisplayed().assertHasClickAction()
        val dateBoxLabel = activity.getString(R.string.appointment_date)
        val dateClickableField = composeTestRule.onNodeWithText(dateBoxLabel).assertIsDisplayed().assertHasClickAction()
        dateClickableField.assert(hasText(tempAppointmentProperties.date.toDisplayFormat(localContext)))
        val timeBoxLabel = activity.getString(R.string.appointment_time)
        val timeClickableField = composeTestRule.onNodeWithText(timeBoxLabel).assertIsDisplayed().assertHasClickAction()
        timeClickableField.assert(hasText(tempAppointmentProperties.time.toDisplayFormat(localContext)))
        val durationBoxLabel = activity.getString(R.string.appointment_duration)
        val durationClickableField = composeTestRule.onNodeWithText(durationBoxLabel).assertIsDisplayed().assertHasClickAction()
        durationClickableField.assert(hasText(tempAppointmentProperties.duration.toDisplayFormat()))
        val locationBoxLabel = activity.getString(R.string.appointment_location)
        val locationClickableField = composeTestRule.onNodeWithText(locationBoxLabel).assertIsDisplayed().assertHasClickAction()
        val descriptionBoxLabel = activity.getString(R.string.appointment_description)
        val descriptionClickableField = composeTestRule.onNodeWithText(descriptionBoxLabel).assertIsDisplayed().assertHasClickAction()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }

    @Test
    fun errorMessagesOnAddInvalidValues(){
        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        //Assert Bottom Bar button Present
        val addText = activity.getString(R.string.add_text)
        val cancelText = activity.getString(R.string.cancel_text)
        val addButton = composeTestRule.onNodeWithText(addText).assertIsDisplayed()
        val cancelButton = composeTestRule.onNodeWithText(cancelText).assertIsDisplayed()
        //Click Add with default values present
        addButton.performClick()
        //Assert Error Messages Present
        val appointmentTitleError = activity.getString(R.string.error_appointment_title_blank)
        val appointmentTitleErrorTextField = composeTestRule.onNodeWithText(appointmentTitleError).assertIsDisplayed()
        val appointmentDurationError = activity.getString(R.string.error_appointment_duration_zero)
        val appointmentDurationErrorTextField = composeTestRule.onNodeWithText(appointmentDurationError).assertIsDisplayed()
        val appointmentLocationError = activity.getString(R.string.error_appointment_location_blank)
        val appointmentLocationErrorTextField = composeTestRule.onNodeWithText(appointmentLocationError).assertIsDisplayed()

        //Return to Schedule Scaffold
        cancelButton.performClick()
    }

    @Test
    fun navigateUpdateTitleValue() =  runTest {
        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        updateTitleValue()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }

    private fun updateTitleValue() =  runTest {

        val titleBoxLabel = activity.getString(R.string.appointment_title)
        val titleClickableField = composeTestRule.onNodeWithText(titleBoxLabel).assertIsDisplayed().assertHasClickAction()
        titleClickableField.performTextInput(appointment.title)
        titleClickableField.assertTextContains(appointment.title)
    }

    @Test
    fun navigateUpdateDateValue() =  runTest {
        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        updateDateValue()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }

    private fun updateDateValue() =  runTest {
        val localContext = composeTestRule.activity.applicationContext

        val dateBoxLabel = activity.getString(R.string.appointment_date)
        val dateClickableField = composeTestRule.onNodeWithText(dateBoxLabel).assertIsDisplayed().assertHasClickAction()
        dateClickableField.performClick()
        val composableDatePicker = onView(withChild(withText("OK"))).check(matches(isDisplayed()))
        val selectedDate = if(LocalDate.now().dayOfMonth != 1){LocalDate.now().withDayOfMonth(1)}else{LocalDate.now().withDayOfMonth(2)}
        //Click on selectedDate number
        onView(withContentDescription(
            "${selectedDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)}, " +
                    "${selectedDate.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)} " +
                    "${selectedDate.dayOfMonth}").also{ hasClickAction()}).check(matches(isDisplayed())).perform(click())
        //Click on OK button
        onView(withText("OK").also{hasClickAction()}).check(matches(isDisplayed())).perform(click())
        val correctedDateValue = selectedDate.toDisplayFormat(localContext)
        dateClickableField.assertTextContains(correctedDateValue)
    }

    @Test
    fun navigateUpdateTimeValue() =  runTest {
        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        updateTimeValue()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }

    private fun updateTimeValue() =  runTest {
        val tempAppointmentProperties = dataViewModel.temporaryAppointmentPropertiesFlow.first()
        val updateHour = if(tempAppointmentProperties.time.hour<=12){tempAppointmentProperties.time.hour + 1}else{tempAppointmentProperties.time.hour - 1}
        val localContext = composeTestRule.activity.applicationContext
        val initialTime = tempAppointmentProperties.time
        val timeBoxLabel = activity.getString(R.string.appointment_time)
        val dateClickableField = composeTestRule.onNodeWithText(timeBoxLabel).assertIsDisplayed().assertHasClickAction()
        dateClickableField.performClick()
        val composableTimePicker = onView(withChild(withText("OK")))
        //Select updated hour
        val onScreenHourToSelect = (if(updateHour == 0){12}else if(updateHour >=13){updateHour-12}else{updateHour}).toString()
        onView(withText(onScreenHourToSelect)).check(matches(isDisplayed())).perform(click())

        //Click on OK button
        onView(withId(2131230955).also{hasClickAction()}).check(matches(isDisplayed())).perform(click())
        //Confirm Time Update in Add Scaffold
        getInstrumentation().waitForIdleSync();

        val updatedTimeValue = initialTime.withHour(updateHour).toDisplayFormat(localContext)
        dateClickableField.assertTextContains(updatedTimeValue)
    }

    @Test
    fun navigateUpdateDurationValue() =  runTest {
        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        updateDurationValue()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }

    private fun updateDurationValue() =  runTest {
        val durationBoxLabel = activity.getString(R.string.appointment_duration)
        val durationClickableField = composeTestRule.onNodeWithText(durationBoxLabel).assertIsDisplayed().assertHasClickAction()
        durationClickableField.performClick()
        val composableDurationPicker = onView(withChild(withText("OK"))).check(matches(isDisplayed()))
        //Select updated hour
        val materialHourTextInput = onView(withId(2131230946)).check(matches(isDisplayed()))
        val materialTimepickerModeButton = onView(withId(2131230954)).check(matches(isDisplayed())).perform(click())
        getInstrumentation().waitForIdleSync();
        onView(withText((10).toString())).check(matches(isDisplayed())).perform(click())
        getInstrumentation().waitForIdleSync();
        //Click on OK button
        onView(withId(2131230955).also{hasClickAction()}).check(matches(isDisplayed())).perform(click())
        getInstrumentation().waitForIdleSync();
        //Confirm Time Update in Add Scaffold
        durationClickableField.assertTextContains(value = "10 Hours", substring = true)
    }

    @Test
    fun navigateUpdateLocationValue() =  runTest {
        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        updateLocationValue()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }

    private fun updateLocationValue(location: ApptLocation = appointment.location){
        val locationBoxLabel = activity.getString(R.string.appointment_location)
        val locationClickableField = composeTestRule.onNodeWithText(locationBoxLabel).assertIsDisplayed().assertHasClickAction()
        locationClickableField.performClick()
        val dropdownMenuItemContentDescription = composeTestRule.activity.getString(R.string.dropdown_menu_item)
        val locationOption = composeTestRule.onNode(hasText(location.getDisplayName(composeTestRule.activity)).and(hasContentDescription(dropdownMenuItemContentDescription)))
        locationOption.assertIsDisplayed().assertHasClickAction()
        //confirm selected location updated on each location option click
        locationOption.performClick()
        locationClickableField.assertTextContains(location.getDisplayName(composeTestRule.activity))
    }

    @Test
    fun navigateUpdateDescriptionValue() =  runTest {
        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        updateDescriptionValue()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }

    private fun updateDescriptionValue(){
        val descriptionBoxLabel = activity.getString(R.string.appointment_description)
        val descriptionClickableField = composeTestRule.onNodeWithText(descriptionBoxLabel).assertIsDisplayed().assertHasClickAction()
//        descriptionClickableField.performClick()
        descriptionClickableField.performTextInput(appointment.description)
        descriptionClickableField.assertTextContains(appointment.description)
    }
//    private fun appendDescriptionValue(replacementDescription: String){
//        val descriptionBoxLabel = activity.getString(R.string.appointment_description)
//        val descriptionClickableField = composeTestRule.onNodeWithText(descriptionBoxLabel).assertIsDisplayed().assertHasClickAction()
////        descriptionClickableField.performClick()
//        descriptionClickableField.performTextClearance()
//        descriptionClickableField.performTextInput(replacementDescription)
//        descriptionClickableField.assertTextContains(replacementDescription)
//    }
//    p

    @Test
    fun insertAppointment() =  runTest {
        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        updateTitleValue()
        updateDateValue()
        updateTimeValue()
        updateDurationValue()
        updateLocationValue()
        updateDescriptionValue()

        //Add Appointment
        val addText = activity.getString(R.string.add_text)
        val addButton = composeTestRule.onNodeWithText(addText).assertIsDisplayed().performClick()
        //Assert Appointment Add
        val appointmentCardContentDescription = activity.getString(R.string.elevated_card)
        val clickableAppointmentCard = composeTestRule.onNode(hasContentDescription(appointmentCardContentDescription)).also{hasText(text = appointment.title, substring = true)}
            .assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun noChangesMadeUpdate() =  runTest {
        insertAppointment()

        //Click on Appointment Card
        val appointmentCardContentDescription = activity.getString(R.string.elevated_card)
        val clickableAppointmentCard = composeTestRule.onNode(hasContentDescription(appointmentCardContentDescription)).also{hasText(text = appointment.title, substring = true)}
            .assertIsDisplayed().assertHasClickAction().performClick()
        //Click on Update Button
        val updateLabel = activity.getString(R.string.update_text)
        val update = composeTestRule.onNodeWithText(updateLabel).assertIsDisplayed().assertHasClickAction()
        update.performClick()
        //Update Appointment
        val updateText = activity.getString(R.string.update_text)
        val updateButton = composeTestRule.onNodeWithText(updateText).assertIsDisplayed().performClick()
        //Assert Error Test present
        val errorText = activity.getString(R.string.error_no_appointment_changes_made)
        val errorTextField = composeTestRule.onNodeWithText(errorText).assertIsDisplayed()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }

    @Test
    fun yesChangesMadeUpdate() =  runTest {
        val localContext = composeTestRule.activity.applicationContext
        insertAppointment()

        //Click on Appointment Card
        val appointmentCardContentDescription = activity.getString(R.string.elevated_card)
        val clickableAppointmentCard = composeTestRule.onNode(hasContentDescription(appointmentCardContentDescription)).also{hasText(text = appointment.title, substring = true)}
            .assertIsDisplayed().assertHasClickAction().performClick()
        //Click on Update Button
        val updateLabel = activity.getString(R.string.update_text)
        val update = composeTestRule.onNodeWithText(updateLabel).assertIsDisplayed().assertHasClickAction()
        update.performClick()
        //Update Appointment Location
        updateLocationValue(ApptLocation.DALLAS)
        val updateText = activity.getString(R.string.update_text)
        val updateButton = composeTestRule.onNodeWithText(updateText).assertIsDisplayed().performClick()
        //Assert Appointment Update
        val updatedAppointmentCard = composeTestRule.onNode(hasContentDescription(appointmentCardContentDescription)).also{hasText(text = ApptLocation.DALLAS.getDisplayName(localContext), substring
        = true)}.assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun updateWithOverlap() =  runTest {
        val localContext = composeTestRule.activity.applicationContext

        insertAppointment()
        //Insert a second identical Appointment
        //Navigate To Insert Scaffold
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Schedule Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        //Assert Insert Appointment Scaffold Navigation
        val insertTopBarTitle = activity.getString(R.string.add_appointment)
        val insertTitleTextBox = composeTestRule.onNodeWithText(insertTopBarTitle).assertIsDisplayed()

        updateTitleValue()
        updateDateValue()
        updateTimeValue()
        updateDurationValue()
        updateLocationValue()
        updateDescriptionValue()

        //Add Appointment
        val addText = activity.getString(R.string.add_text)
        val addButton = composeTestRule.onNodeWithText(addText).assertIsDisplayed().performClick()
        // Confirm Still in Insert Scaffold
        //Assert Insert Appointment Scaffold Navigation
        insertTitleTextBox.assertIsDisplayed()

        //Return to Schedule Scaffold
        val cancelLabel = activity.getString(R.string.cancel_text)
        val cancelButton = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction().performClick()
    }
}