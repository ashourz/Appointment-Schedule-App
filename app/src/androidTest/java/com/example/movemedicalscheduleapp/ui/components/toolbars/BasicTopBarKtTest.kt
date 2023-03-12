package com.example.movemedicalscheduleapp.ui.components.toolbars

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.movemedicalscheduleapp.ui.theme.MoveMedicalScheduleAppTheme
import org.junit.Rule
import org.junit.Test

class BasicTopBarKtTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun confirmTitle() {
        val title = "Test Title"
        // Start the app
        composeTestRule.setContent {
            MoveMedicalScheduleAppTheme() {
                BasicTopBar(
                    title = title
                )
            }
        }

        val titleTextBox = composeTestRule.onNodeWithText(title)
        titleTextBox.assertIsDisplayed()
    }
}