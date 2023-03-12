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

package com.example.movemedicalscheduleapp.ui.components.text

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.ui.theme.MoveMedicalScheduleAppTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class LocationDropDownKtTest{
    private var selectedLocation: ApptLocation? = null

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun onLocationsSelect(){
        // Start the app
        composeTestRule.setContent {
            MoveMedicalScheduleAppTheme() {
                LocationDropDown(
                    selectedLocation = null,
                    onLocationSelected = {
                        selectedLocation = it
                    }
                )
            }
        }
        //Assert Textbox present and clickable
        val textPlaceholder = composeTestRule.activity.getString(R.string.select_appointment_location)
        val textbox = composeTestRule.onNodeWithText(textPlaceholder)
        textbox.assertIsDisplayed().assertHasClickAction()
        textbox.performClick()
        //Confirm no option for unknown location
        composeTestRule.onNodeWithText(ApptLocation.UNKNOWN.getDisplayName(composeTestRule.activity)).assertDoesNotExist()
        //Assert Location options available
        assertThat(selectedLocation == null).isTrue()

        val dropdownMenuItemContentDescription = composeTestRule.activity.getString(R.string.dropdown_menu_item)
        ApptLocation.values().filter { it != ApptLocation.UNKNOWN }.map{
            val locationOption = composeTestRule.onNode(hasText(it.getDisplayName(composeTestRule.activity)).and(hasContentDescription(dropdownMenuItemContentDescription)))
            locationOption.assertIsDisplayed().assertHasClickAction()
            //confirm selected location updated on each location option click
            locationOption.performClick()
            assertThat(selectedLocation == it).isTrue()
            //open drop down menu
            textbox.performClick()
        }
    }

    @Test
    fun onError(){
        val errorString = "THIS IS AN ERROR"
        // Start the app
        composeTestRule.setContent {
            MoveMedicalScheduleAppTheme() {
                LocationDropDown(
                    selectedLocation = null,
                    onLocationSelected = {
                        selectedLocation = it
                    },
                    errorString = errorString
                )
            }
        }
        //Assert error textbox present
        val errorTextElement = composeTestRule.onNodeWithText(errorString)
        errorTextElement.assertIsDisplayed()
    }

}