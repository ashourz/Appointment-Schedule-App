package com.example.movemedicalscheduleapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.ui.ComposableConstants
import com.example.movemedicalscheduleapp.ui.components.datetime.ComposeDatePicker
import com.example.movemedicalscheduleapp.ui.components.datetime.ComposeDurationPicker
import com.example.movemedicalscheduleapp.ui.components.datetime.ComposeTimePicker
import com.example.movemedicalscheduleapp.ui.components.scaffolds.ModalScaffold
import com.example.movemedicalscheduleapp.ui.components.text.EditableTextField
import com.example.movemedicalscheduleapp.ui.components.text.ErrorText
import com.example.movemedicalscheduleapp.ui.components.text.LocationDropDown
import com.example.movemedicalscheduleapp.ui.components.validation.addUpdateAppointmentValidation
import com.example.movemedicalscheduleapp.ui.ui_data_class.TempAppointmentProperties
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import kotlinx.coroutines.*

@Composable
fun UpsertScaffold(
    activity: AppCompatActivity,
    dataViewModel: DataViewModel,
    update: Boolean,
    onNavigateAway: () -> Unit
) {
    val localContext = LocalContext.current
    val coroutineScopeIO = rememberCoroutineScope().plus(Dispatchers.IO)
    val tempAppointmentProperties by dataViewModel.temporaryAppointmentPropertiesFlow.collectAsState()
    //region: Error States
    var appointmentTitleError: String? by remember { mutableStateOf(null) }
    var appointmentLocationError: String? by remember { mutableStateOf(null) }
    var appointmentDurationError: String? by remember { mutableStateOf(null) }
    var updateError: String? by remember { mutableStateOf(null) }
    //endregion

    DisposableEffect(key1 = Unit) {
        if (!update) {
            //Refresh temp properties if this is an add operation
            dataViewModel.updateTempAppointmentProperties(TempAppointmentProperties())
        }
        onDispose {
            dataViewModel.updateTempAppointmentProperties(TempAppointmentProperties())
        }
    }

    BackHandler() {
        onNavigateAway()
    }

    ModalScaffold(
        title = if (update) {
            "Update Appointment"
        } else {
            "Add Appointment"
        },
        actionButtonText = if (update) {
            "Update"
        } else {
            "Add"
        },
        dataValidation = {
            addUpdateAppointmentValidation(
                tempAppointmentProperties = tempAppointmentProperties,
                updateAppointmentTitleError = { updatedTitleError ->
                    appointmentTitleError = updatedTitleError
                },
                updateAppointmentDurationError = { updatedDurationError ->
                    appointmentDurationError = updatedDurationError
                },
                updateAppointmentLocationError = { updatedLocationError ->
                    appointmentLocationError = updatedLocationError
                },
                updateUpdateError = { updatedUpdateError ->
                    updateError = updatedUpdateError
                }
            )
        },
        overlapValidation = { runBlocking {
            return@runBlocking coroutineScopeIO.async {
                return@async dataViewModel.getOverlappingAppointments(
                    localContext = localContext,
                    appointment = Appointment(
                        title = tempAppointmentProperties.title?:"",
                        location = tempAppointmentProperties.location,
                        datetime = tempAppointmentProperties.date.atTime(tempAppointmentProperties.time),
                        duration = tempAppointmentProperties.duration,
                        description = tempAppointmentProperties.description?:""
                    ))
            }.await().isEmpty()
        }},
        onActionButtonClick = {
            coroutineScopeIO.launch {
                dataViewModel.upsertAppointment(
                    Appointment(
                        rowid = tempAppointmentProperties.editingAppointment?.rowid ?: 0,
                        title = tempAppointmentProperties.title ?: "",
                        datetime = tempAppointmentProperties.date.atTime(tempAppointmentProperties.time),
                        location = tempAppointmentProperties.location,
                        duration = tempAppointmentProperties.duration,
                        description = tempAppointmentProperties.description ?: ""
                    )
                )
            }
            onNavigateAway()
        },
        onCancelButtonClick = {
            onNavigateAway()
        }
    ) {

        if (updateError != null) {
            ErrorText(errorText = updateError)
        }
        if (tempAppointmentProperties.existingApptError != null) {
            ErrorText(errorText = tempAppointmentProperties.existingApptError)
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(
                contentType = "EditableTextField"
            ) {
                EditableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValue = tempAppointmentProperties.title,
                    label = "Appointment Title",
                    placeholder = "Enter Appointment Title",
                    maxLines = 1,
                    leadingIconDrawable = ComposableConstants.nameIcon,
                    leadingIconContentDescription = "Appointment Title",
                    onValueChange = { updatedName ->
                        dataViewModel.updateTempAppointmentProperties(
                            tempAppointmentProperties.copy(
                                title = updatedName
                            )
                        )
                    },
                    errorString = appointmentTitleError
                )
            }
            item(
                contentType = "ComposeDatePicker"
            ) {
                ComposeDatePicker(
                    activity = activity,
                    label = "Appointment Date",
                    placeholder = "Select Appointment Date",
                    leadingIconDrawable = ComposableConstants.calendarIcon,
                    leadingIconContentDescription = "Appointment Date",
                    selectedDate = tempAppointmentProperties.date,
                    onDateSelected = { updatedDate ->
                        dataViewModel.updateTempAppointmentProperties(
                            tempAppointmentProperties.copy(date = updatedDate)
                        )
                    }
                )
            }
            item(
                contentType = "ComposeTimePicker"
            ) {
                ComposeTimePicker(
                    activity = activity,
                    label = "Appointment Time",
                    placeholder = "Select Appointment Time",
                    leadingIconDrawable = ComposableConstants.timeIcon,
                    leadingIconContentDescription = "Appointment Date",
                    selectedTime = tempAppointmentProperties.time,
                    onTimeSelected = { updatedTime ->
                        dataViewModel.updateTempAppointmentProperties(
                            tempAppointmentProperties.copy(time = updatedTime)
                        )
                    }
                )
            }
            item(
                contentType = "ComposeDurationPicker"
            ) {
                ComposeDurationPicker(
                    activity = activity,
                    label = "Appointment Duration",
                    placeholder = "Select Appointment Duration",
                    leadingIconDrawable = ComposableConstants.durationIcon,
                    leadingIconContentDescription = "Appointment Date",
                    selectedDuration = tempAppointmentProperties.duration,
                    onDurationSelected = { updatedDuration ->
                        dataViewModel.updateTempAppointmentProperties(
                            tempAppointmentProperties.copy(duration = updatedDuration)
                        )
                    },
                    errorString = appointmentDurationError
                )
            }
            item(
                contentType = "LocationDropDown"
            ) {
                LocationDropDown(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Appointment Location",
                    selectedLocation = if (tempAppointmentProperties.location == ApptLocation.UNKNOWN) {
                        null
                    } else {
                        tempAppointmentProperties.location
                    },
                    onLocationSelected = { updatedApptLocation ->
                        dataViewModel.updateTempAppointmentProperties(
                            tempAppointmentProperties.copy(
                                location = updatedApptLocation
                            )
                        )
                    },
                    errorString = appointmentLocationError
                )
            }
            item {
                EditableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValue = tempAppointmentProperties.description,
                    label = "Description",
                    placeholder = "Enter Description",
                    leadingIconDrawable = ComposableConstants.descriptionIcon,
                    leadingIconContentDescription = "Description",
                    onValueChange = { updatedDesc ->
                        dataViewModel.updateTempAppointmentProperties(
                            tempAppointmentProperties.copy(
                                description = updatedDesc
                            )
                        )
                    }
                )
            }
        }
    }
}