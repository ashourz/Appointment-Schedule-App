package com.example.movemedicalscheduleapp.ui

import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.R
import java.time.LocalDate
import java.time.Month

object ComposableConstants {
    val defaultNavigationBarHeight = 50.dp
    val defaultIconSize = 32.dp

    //region: Icons
    val nameIcon = R.drawable.baseline_short_text_24
    val calendarIcon = R.drawable.baseline_calendar_month_24
    val locationIcon = R.drawable.baseline_location_city_24
    val descriptionIcon = R.drawable.baseline_notes_24
    val timeIcon = R.drawable.baseline_access_time_24
    val durationIcon = R.drawable.baseline_timelapse_24
    val upArrowIcon = R.drawable.baseline_keyboard_arrow_up_24
    val downArrowIcon = R.drawable.baseline_keyboard_arrow_down_24
    val todayIcon = R.drawable.baseline_today_24
    val addIcon = R.drawable.baseline_add_24
    //endregion

    val calendarMinDate = LocalDate.of(2000, Month.JANUARY, 1)
    val calendarMaxDate = LocalDate.of(3000, Month.JANUARY, 1).minusDays(1L)

}