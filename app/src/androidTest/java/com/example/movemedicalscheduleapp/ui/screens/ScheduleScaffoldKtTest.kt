package com.example.movemedicalscheduleapp.ui.screens

import android.app.Activity
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.extensions.toDisplayFormat
import com.example.movemedicalscheduleapp.ui.theme.MoveMedicalScheduleAppTheme
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class ScheduleScaffoldKtTest: TestCase(){
    private lateinit var activity: Activity
    private lateinit var dataViewModel: DataViewModel

    private var navigateToAddAppointment = false
    private var natigateToUpdateAppointment = false
    private val appointment = Appointment(
        title = "Test Title",
        datetime = LocalDateTime.now(),
        location = ApptLocation.DALLAS,
        duration = Duration.ofMinutes(45L),
        description = "Test Description"
    )
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    @get:Rule
    val instantTasExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        super.setUp()
        val application = ApplicationProvider.getApplicationContext() as Application
        dataViewModel = DataViewModel(application)
        activity = composeTestRule.activity

        // Start the app
        composeTestRule.setContent {
            MoveMedicalScheduleAppTheme() {
                ScheduleScaffold(
                    dataViewModel = dataViewModel,
                    onNavigateToAddAppointment = {
                        navigateToAddAppointment = true
                    },
                    onNavigateToUpdateAppointment = {
                        natigateToUpdateAppointment = true
                    }
                )
            }
        }
        //Cancel All Appointments
        runBlocking {
            dataViewModel.pastAppointmentStateFlow.first().forEach {
                dataViewModel.deleteAppointment(it)
            }
            dataViewModel.todayAppointmentStateFlow.first().forEach {
                dataViewModel.deleteAppointment(it)
            }
            dataViewModel.futureAppointmentStateFlow.first().forEach {
                dataViewModel.deleteAppointment(it)
            }

            //Ensure at least one appointment displayed

            withContext(Dispatchers.IO) { dataViewModel.upsertAppointment(appointment) }
        }
    }

    @Test
    fun assertBasicElementsDisplayed(){
        //Assert Top Bar Displayed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Bottom Bar Buttons Displayed
        val todayLabel = activity.getString(R.string.today_text)
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val todayButton = composeTestRule.onNodeWithText(todayLabel)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        todayButton.assertIsDisplayed().assertHasClickAction()
        fab.assertIsDisplayed().assertHasClickAction()
    }


    @Test
    fun onFabClicked(){
        //Assert Top Bar Dispalyed
        val topBarTitle = activity.getString(R.string.my_appointment_schedule)
        val titleTextBox = composeTestRule.onNodeWithText(topBarTitle).assertIsDisplayed()

        //Assert Bottom Bar Button Displayed
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        fab.assertIsDisplayed().assertHasClickAction()

        fab.performClick()
        assertThat(navigateToAddAppointment).isTrue()
    }

    @Test
    fun onTodayClicked(){
        //Assert Bottom Bar Button Displayed
        val todayLabel = activity.getString(R.string.today_text)
        val todayButton = composeTestRule.onNodeWithText(todayLabel)
        todayButton.assertIsDisplayed().assertHasClickAction()

        //Assert Today Sticky Header Present
        val todayStickyHeaderText = activity.getString(R.string.today_prefix).plus(LocalDate.now().toDisplayFormat(activity))
        todayButton.performClick()
        val todayStickyHeader = composeTestRule.onNodeWithText(todayStickyHeaderText).assertIsDisplayed()
    }

    @Test
    fun onCancelAppointments(){
        //Find All Appointment Cards
        val appointmentCardContentDescription = activity.getString(R.string.elevated_card)
        val clickableAppointmentCard = composeTestRule.onNode(hasContentDescription(appointmentCardContentDescription).and(hasText(text = appointment.title, substring = true)))
            .assertIsDisplayed().assertHasClickAction()
        clickableAppointmentCard.performClick()
        //Assert Update and Cancel Button Options are displayed
        val updateLabel = activity.getString(R.string.update_text)
        val cancelLabel = activity.getString(R.string.cancel_text)
        val update = composeTestRule.onNodeWithText(updateLabel).assertIsDisplayed().assertHasClickAction()
        val cancel = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction()
        cancel.performClick()
        //Assert Confirm Cancel Appointment Dialog displayed
        //Assert Buttons displayed
        val keepAppointmentLabel = composeTestRule.activity.getString(R.string.keep_appointment)
        val keepAppointmentButton = composeTestRule.onNodeWithText(keepAppointmentLabel).assertIsDisplayed().assertHasClickAction()

        val cancelAppointmentLabel = composeTestRule.activity.getString(R.string.cancel_appointment)
        val cancelAppointmentButton = composeTestRule.onNodeWithText(cancelAppointmentLabel).assertIsDisplayed().assertHasClickAction()

        cancelAppointmentButton.performClick()
        clickableAppointmentCard.assertDoesNotExist()
        keepAppointmentButton.assertDoesNotExist()
        cancelAppointmentButton.assertDoesNotExist()
    }

    @Test
    fun onKeepAppointments() = kotlinx.coroutines.test.runTest{
        val appointmentCardContentDescription = activity.getString(R.string.elevated_card)
        val clickableAppointmentCard = composeTestRule.onNode(hasContentDescription(appointmentCardContentDescription).and(hasText(text = appointment.title, substring = true)))
            .assertIsDisplayed().assertHasClickAction()
        clickableAppointmentCard.performClick()
        //Assert Update and Cancel Button Options are displayed
        val updateLabel = activity.getString(R.string.update_text)
        val cancelLabel = activity.getString(R.string.cancel_text)
        val update = composeTestRule.onNodeWithText(updateLabel).assertIsDisplayed().assertHasClickAction()
        val cancel = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction()
        cancel.performClick()
        //Assert Confirm Cancel Appointment Dialog displayed
        //Assert Buttons displayed
        val keepAppointmentLabel = composeTestRule.activity.getString(R.string.keep_appointment)
        val keepAppointmentButton = composeTestRule.onNodeWithText(keepAppointmentLabel).assertIsDisplayed().assertHasClickAction()

        val cancelAppointmentLabel = composeTestRule.activity.getString(R.string.cancel_appointment)
        val cancelAppointmentButton = composeTestRule.onNodeWithText(cancelAppointmentLabel).assertIsDisplayed().assertHasClickAction()

        keepAppointmentButton.performClick()
        clickableAppointmentCard.assertIsDisplayed()
        keepAppointmentButton.assertDoesNotExist()
        cancelAppointmentButton.assertDoesNotExist()
    }

    @Test
    fun onUpdateAppointments() = kotlinx.coroutines.test.runTest{
        //Find All Appointment Cards
        val appointmentCardContentDescription = activity.getString(R.string.elevated_card)
        composeTestRule.onRoot().printToLog("TAG")
        val clickableAppointmentCard = composeTestRule.onNode(hasContentDescription(appointmentCardContentDescription).and(hasText(text = appointment.title, substring = true)))
            .assertIsDisplayed().assertHasClickAction()
        clickableAppointmentCard.performClick()
        //Assert Update and Cancel Button Options are displayed
        val updateLabel = activity.getString(R.string.update_text)
        val cancelLabel = activity.getString(R.string.cancel_text)
        val update = composeTestRule.onNodeWithText(updateLabel).assertIsDisplayed().assertHasClickAction()
        val cancel = composeTestRule.onNodeWithText(cancelLabel).assertIsDisplayed().assertHasClickAction()
        update.performClick()
        assertThat(!navigateToAddAppointment && natigateToUpdateAppointment).isTrue()
    }
}