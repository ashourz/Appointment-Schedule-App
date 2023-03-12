package com.example.movemedicalscheduleapp.ui.components.datetime

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.extensions.toEpochMilli
import com.example.movemedicalscheduleapp.ui.ComposableConstants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import java.time.*

/**
 * Shows MaterialDatePicker
 * */
fun showDatePicker(
    context: Context,
    fragmentManager: FragmentManager,
    title: String,
    openDate: LocalDate,
    onPositiveButtonClick: (datePicked: LocalDate) -> Unit,
) {

    val pickerBuilder = MaterialDatePicker.Builder.datePicker()
    //Add Calendar constraints if present
    val constraintsBuilder: CalendarConstraints = CalendarConstraints.Builder()
        .setStart(ComposableConstants.calendarMinDate.toEpochMilli())
        .setEnd(ComposableConstants.calendarMaxDate.toEpochMilli())
        .setOpenAt(openDate.toEpochMilli())
        .build()

    val picker = pickerBuilder
        .setCalendarConstraints(constraintsBuilder)
        .setSelection(openDate.toEpochMilli())
        .setTitleText(title)
        .setTheme(R.style.Theme_DatePicker)
        .build()

    picker.addOnPositiveButtonClickListener { epochMilli ->
        val selectedDate =
            LocalDateTime.ofEpochSecond((epochMilli / 1000L), 0, ZoneOffset.UTC).toLocalDate()
        onPositiveButtonClick(selectedDate)
    }
    picker.show(fragmentManager, context.getString(R.string.date_picker_tag))


}

/**
 * Shows MaterialTimePicker formatted for Time selection
 * */
fun showTimePicker(
    context: Context,
    fragmentManager: FragmentManager,
    title: String,
    openTime: LocalTime,
    onPositiveButtonClick: (timePicked: LocalTime) -> Unit,
) {

    val pickerBuilder = MaterialTimePicker.Builder()

    val picker = pickerBuilder
        .setTitleText(title)
        .setHour(openTime.hour)
        .setMinute(openTime.minute)
        .setInputMode(INPUT_MODE_CLOCK)
        .setTimeFormat(TimeFormat.CLOCK_12H)
        .setTheme(R.style.Theme_TimePicker)
        .build()

    picker.addOnPositiveButtonClickListener {
        val selectedTime = LocalTime.of(picker.hour, picker.minute)
        onPositiveButtonClick(selectedTime)

    }
    picker.show(fragmentManager, context.getString(R.string.time_picker_tag))


}

/**
 * Shows MaterialTimePicker formatted for Duration selection
 * */
fun showDurationPicker(
    context: Context,
    fragmentManager: FragmentManager,
    title: String,
    openDuration: Duration,
    onPositiveButtonClick: (durationPicked: Duration) -> Unit
) {

    val pickerBuilder = MaterialTimePicker.Builder()

    val picker = pickerBuilder
        .setTitleText(title)
        .setHour(openDuration.toHours().toInt())
        .setMinute(openDuration.toMinutes().toInt())
        .setInputMode(INPUT_MODE_KEYBOARD)
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setTheme(R.style.Theme_TimePicker)
        .build()

    picker.addOnPositiveButtonClickListener {
        val selectedTime = Duration.ofHours(picker.hour.toLong()).plusMinutes(picker.minute.toLong())
        onPositiveButtonClick(selectedTime)

    }
    picker.show(fragmentManager, context.getString(R.string.duration_picker_tag))

}