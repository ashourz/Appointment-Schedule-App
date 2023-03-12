package com.example.movemedicalscheduleapp.ui.components.bottombars

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.ui.theme.MoveMedicalScheduleAppTheme
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ScheduleBottomBarKtTest{
    private var scrollPerformed = false
    private var fabClicked = false

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun before(){
        scrollPerformed = false
        fabClicked = false
        // Start the app
        composeTestRule.setContent {
            MoveMedicalScheduleAppTheme() {
                ScheduleBottomBar(
                    onScrollToToday = {scrollPerformed = true},
                    onAddFabClick = {fabClicked = true}
                )
            }
        }
    }

    @Test
    fun todayButtonClick() {
        val activity = composeTestRule.activity

        val todayLabel = activity.getString(R.string.today_text)
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val todayButton = composeTestRule.onNodeWithText(todayLabel)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        todayButton.assertIsDisplayed().assertHasClickAction()
        fab.assertIsDisplayed().assertHasClickAction()
        todayButton.performClick()
        Truth.assertThat(scrollPerformed && !fabClicked).isTrue()
    }

    @Test
    fun fabButtonClick() {
        val activity = composeTestRule.activity

        val todayLabel = activity.getString(R.string.today_text)
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val todayButton = composeTestRule.onNodeWithText(todayLabel)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        todayButton.assertIsDisplayed().assertHasClickAction()
        fab.assertIsDisplayed().assertHasClickAction()
        fab.performClick()
        Truth.assertThat(!scrollPerformed && fabClicked).isTrue()
    }

    @Test
    fun bothButtonaClick() {
        val activity = composeTestRule.activity

        val todayLabel = activity.getString(R.string.today_text)
        val fabContentDescription = activity.getString(R.string.add_appointment)
        val todayButton = composeTestRule.onNodeWithText(todayLabel)
        val fab = composeTestRule.onNode(hasContentDescription(fabContentDescription))
        todayButton.assertIsDisplayed().assertHasClickAction()
        fab.assertIsDisplayed().assertHasClickAction()
        todayButton.performClick()
        fab.performClick()
        Truth.assertThat(scrollPerformed && fabClicked).isTrue()
    }
}