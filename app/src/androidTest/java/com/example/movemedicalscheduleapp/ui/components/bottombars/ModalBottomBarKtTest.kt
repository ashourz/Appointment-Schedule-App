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

package com.example.movemedicalscheduleapp.ui.components.bottombars

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.movemedicalscheduleapp.ui.theme.MoveMedicalScheduleAppTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class ModalBottomBarKtTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun positiveButtonClick() {
        val positiveActionText = "Positive"
        val negativeActionText = "Negative"
        var positiveButtonClicked = false
        var negativeButtonClicked = false
        // Start the app
        composeTestRule.setContent {
            MoveMedicalScheduleAppTheme() {
                ModalBottomBar(
                    positiveActionText = positiveActionText,
                    negativeActionText = negativeActionText,
                    onPositiveButtonClick = {positiveButtonClicked = true},
                    onNegativeButtonClick = {negativeButtonClicked = true}
                )
            }
        }
        composeTestRule.onNodeWithText(positiveActionText).assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText(negativeActionText).assertIsDisplayed()
        assertThat(positiveButtonClicked && !negativeButtonClicked).isTrue()
    }

    @Test
    fun negativeButtonClick() {
        val positiveActionText = "Positive"
        val negativeActionText = "Negative"
        var positiveButtonClicked = false
        var negativeButtonClicked = false
        // Start the app
        composeTestRule.setContent {
            MoveMedicalScheduleAppTheme() {
                ModalBottomBar(
                    positiveActionText = positiveActionText,
                    negativeActionText = negativeActionText,
                    onPositiveButtonClick = {positiveButtonClicked = true},
                    onNegativeButtonClick = {negativeButtonClicked = true}
                )
            }
        }
        composeTestRule.onNodeWithText(positiveActionText).assertIsDisplayed()
        composeTestRule.onNodeWithText(negativeActionText).assertIsDisplayed().performClick()
        assertThat(!positiveButtonClicked && negativeButtonClicked).isTrue()
    }

    @Test
    fun bothButtonsClick() {
        val positiveActionText = "Positive"
        val negativeActionText = "Negative"
        var positiveButtonClicked = false
        var negativeButtonClicked = false
        // Start the app
        composeTestRule.setContent {
            MoveMedicalScheduleAppTheme() {
                ModalBottomBar(
                    positiveActionText = positiveActionText,
                    negativeActionText = negativeActionText,
                    onPositiveButtonClick = {positiveButtonClicked = true},
                    onNegativeButtonClick = {negativeButtonClicked = true}
                )
            }
        }
        composeTestRule.onNodeWithText(positiveActionText).assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText(negativeActionText).assertIsDisplayed().performClick()
        assertThat(positiveButtonClicked && negativeButtonClicked).isTrue()
    }
}