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

package com.example.movemedicalscheduleapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.R
import java.time.LocalDate
import java.time.Month

object ComposableConstants {
    val defaultIconSize = 32.dp

    //region: Icons
    const val nameIcon = R.drawable.baseline_short_text_24
    const val calendarIcon = R.drawable.baseline_calendar_month_24
    const val locationIcon = R.drawable.baseline_location_city_24
    const val descriptionIcon = R.drawable.baseline_notes_24
    const val timeIcon = R.drawable.baseline_access_time_24
    const val durationIcon = R.drawable.baseline_timelapse_24
    const val upArrowIcon = R.drawable.baseline_keyboard_arrow_up_24
    const val downArrowIcon = R.drawable.baseline_keyboard_arrow_down_24
    const val todayIcon = R.drawable.baseline_today_24
    const val addIcon = R.drawable.baseline_add_24
    //endregion
    // Limits set for material date picker
    val calendarMinDate = LocalDate.of(2000, Month.JANUARY, 1)
    val calendarMaxDate = LocalDate.of(3000, Month.JANUARY, 1).minusDays(1L)

    @Composable
    fun fabButtonElevation(): ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = 6.dp,
        pressedElevation = 1.dp,
        focusedElevation = 1.dp,
        hoveredElevation = 3.dp,
        disabledElevation = 0.dp
    )
}