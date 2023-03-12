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

import android.app.Activity
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.room.Transaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.extensions.toDisplayFormat
import com.example.movemedicalscheduleapp.ui.theme.MoveMedicalScheduleAppTheme
import com.example.movemedicalscheduleapp.view_model.AppointmentRepo
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ScheduleScaffoldKtTodayTest : TestCase() {
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

        //Cancel All Appointments
        runBlocking {
            withContext(Dispatchers.IO) {
                repeat(
                    20
                ) {
                    //Ensure many appointments
                    dataViewModel.upsertAppointment(
                        appointment.copy(
                            datetime = LocalDateTime.now().minusDays(1L)
                        )
                    )
                }
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
            }
        }
    }


    @Test
    fun onTodayClicked() = kotlinx.coroutines.test.runTest {
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
        //Assert Bottom Bar Button Displayed
        val todayLabel = activity.getString(R.string.today_text)
        val todayButton = composeTestRule.onNodeWithText(todayLabel)
        todayButton.assertIsDisplayed().assertHasClickAction()

        //Assert Today Sticky Header Present
        val todayStickyHeaderText = activity.getString(R.string.today_prefix).plus(LocalDate.now().toDisplayFormat(activity))
        todayButton.performClick()
        val todayStickyHeader = composeTestRule.onNodeWithText(todayStickyHeaderText).assertIsDisplayed()
    }

}