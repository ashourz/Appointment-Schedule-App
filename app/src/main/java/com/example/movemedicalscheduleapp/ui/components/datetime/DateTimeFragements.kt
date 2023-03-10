package com.example.movemedicalscheduleapp.ui.components.datetime

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.extensions.toEpochMilli
import com.example.movemedicalscheduleapp.ui.ComposableConstants
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.*
import com.google.android.material.timepicker.TimeFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

fun showDatePicker(
    activity: AppCompatActivity,
    title: String,
    openDate: LocalDate,
    onPositiveButtonClick: (datePicked: LocalDate) -> Unit = {},
    onDismissOrCancelClick: () -> Unit = {},
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

    activity.let {
        picker.addOnPositiveButtonClickListener { epochMilli ->
            val selectedDate =
                LocalDateTime.ofEpochSecond((epochMilli / 1000L), 0, ZoneOffset.UTC).toLocalDate()
            onPositiveButtonClick(selectedDate)
        }
        picker.addOnCancelListener { onDismissOrCancelClick() }
        picker.addOnDismissListener { onDismissOrCancelClick() }
        picker.show(it.supportFragmentManager, "DATE_PICKER")

    }
}

fun showTimePicker(
    activity: AppCompatActivity,
    title: String,
    openTime: LocalTime,
    onPositiveButtonClick: (timePicked: LocalTime) -> Unit = {},
    onDismissOrCancelClick: () -> Unit = {},
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

    activity.let {
        picker.addOnPositiveButtonClickListener {
            val selectedTime = LocalTime.of(picker.hour, picker.minute)
            onPositiveButtonClick(selectedTime)

        }
        picker.addOnNegativeButtonClickListener { onDismissOrCancelClick() }
        picker.addOnCancelListener { onDismissOrCancelClick() }
        picker.addOnDismissListener { onDismissOrCancelClick() }
        picker.show(it.supportFragmentManager, "TIME_PICKER")

    }
}

fun showDurationPicker(
    activity: AppCompatActivity,
    title: String,
    openDuration: Duration,
    onPositiveButtonClick: (durationPicked: Duration) -> Unit = {},
    onDismissOrCancelClick: () -> Unit = {},
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

    activity.let {
        picker.addOnPositiveButtonClickListener {
            val selectedTime = Duration.ofHours(picker.hour.toLong()).plusMinutes(picker.minute.toLong())
            onPositiveButtonClick(selectedTime)

        }
        picker.addOnNegativeButtonClickListener { onDismissOrCancelClick() }
        picker.addOnCancelListener { onDismissOrCancelClick() }
        picker.addOnDismissListener { onDismissOrCancelClick() }
        picker.show(it.supportFragmentManager, "DURATION_PICKER")

    }
}