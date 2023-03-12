package com.example.movemedicalscheduleapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.example.movemedicalscheduleapp.R
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
    fragmentManager: FragmentManager,
    dataViewModel: DataViewModel,
    update: Boolean,
    onNavigateAway: () -> Unit
) {
    val localContext = LocalContext.current
    val coroutineScopeIO = rememberCoroutineScope().plus(Dispatchers.IO)
    val tempAppointmentProperties by dataViewModel.temporaryAppointmentPropertiesFlow.collectAsState()

    //region: Snackbar State, Messages and LaunchedEffect
    val snackbarHostState by dataViewModel.snackbarHostStateFlow.collectAsState()
    val snackbarMessages by dataViewModel.snackbarMessages.collectAsState()
    LaunchedEffect(key1 = snackbarMessages) {
        snackbarMessages?.let { snackBarMessage ->
            //Show snackbar message on every non-null value
            snackbarHostState.showSnackbar(
                message = snackBarMessage,
                duration = SnackbarDuration.Short
            )
        }
    }
    //endregion

    //region: Error States
    var appointmentTitleError: String? by remember { mutableStateOf(null) }
    var appointmentLocationError: String? by remember { mutableStateOf(null) }
    var appointmentDurationError: String? by remember { mutableStateOf(null) }
    var appointmentUpdateError: String? by remember { mutableStateOf(null) }
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        title = if (update) {
            stringResource(R.string.update_appointment)
        } else {
            stringResource(R.string.add_appointment)
        },
        actionButtonText = if (update) {
            stringResource(R.string.update_text)
        } else {
            stringResource(R.string.add_text)
        },
        dataValidation = {
            addUpdateAppointmentValidation(
                tempAppointmentProperties = tempAppointmentProperties,
                updateAppointmentTitleError = { isError ->
                    if(isError){
                        appointmentTitleError = localContext.getString(R.string.error_appointment_title_blank)
                    }else{
                        appointmentTitleError = null
                    }
                },
                updateAppointmentDurationError = { isError ->
                    if(isError){
                        appointmentDurationError = localContext.getString(R.string.error_appointment_duration_zero)
                    }else{
                        appointmentDurationError = null
                    }
                },
                updateAppointmentLocationError = { isError ->
                    if(isError){
                        appointmentLocationError = localContext.getString(R.string.error_appointment_location_blank)
                    }else{
                        appointmentLocationError = null
                    }
                },
                updateUpdateError = { isError ->
                    if(isError){
                        appointmentUpdateError = localContext.getString(R.string.error_no_appointment_changes_made)
                    }else{
                        appointmentUpdateError = null
                    }
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

        if (appointmentUpdateError != null) {
            ErrorText(errorText = appointmentUpdateError)
        }
        if (tempAppointmentProperties.existingApptError != null) {
            ErrorText(errorText = tempAppointmentProperties.existingApptError)
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(
                contentType = localContext.getString(R.string.editable_text_field)
            ) {
                EditableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textValue = tempAppointmentProperties.title,
                    label = stringResource(R.string.appointment_title),
                    placeholder = stringResource(R.string.enter_appointment_title),
                    maxLines = 1,
                    leadingIconDrawable = ComposableConstants.nameIcon,
                    leadingIconContentDescription = stringResource(R.string.appointment_title),
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
                contentType = localContext.getString(R.string.compose_date_picker)
            ) {
                ComposeDatePicker(
                    fragmentManager = fragmentManager,
                    label = stringResource(R.string.appointment_date),
                    placeholder = stringResource(R.string.select_appointment_Date),
                    leadingIconDrawable = ComposableConstants.calendarIcon,
                    leadingIconContentDescription = stringResource(R.string.appointment_date),
                    selectedDate = tempAppointmentProperties.date,
                    onDateSelected = { updatedDate ->
                        dataViewModel.updateTempAppointmentProperties(
                            tempAppointmentProperties.copy(date = updatedDate)
                        )
                    }
                )
            }
            item(
                contentType = localContext.getString(R.string.compose_time_picker)
            ) {
                ComposeTimePicker(
                    fragmentManager = fragmentManager,
                    label = stringResource(R.string.appointment_time),
                    placeholder = stringResource(R.string.select_appointment_time),
                    leadingIconDrawable = ComposableConstants.timeIcon,
                    leadingIconContentDescription = stringResource(R.string.appointment_time),
                    selectedTime = tempAppointmentProperties.time,
                    onTimeSelected = { updatedTime ->
                        dataViewModel.updateTempAppointmentProperties(
                            tempAppointmentProperties.copy(time = updatedTime)
                        )
                    }
                )
            }
            item(
                contentType = localContext.getString(R.string.compose_duration_picker)
            ) {
                ComposeDurationPicker(
                    fragmentManager = fragmentManager,
                    label = stringResource(R.string.appointment_duration),
                    placeholder = stringResource(R.string.select_appointment_duration),
                    leadingIconDrawable = ComposableConstants.durationIcon,
                    leadingIconContentDescription = stringResource(R.string.appointment_duration),
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
                contentType = localContext.getString(R.string.location_drop_down)
            ) {
                LocationDropDown(
                    modifier = Modifier.fillMaxWidth(),
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
                    label = stringResource(R.string.appointment_description),
                    placeholder = stringResource(R.string.enter_appointment_description),
                    leadingIconDrawable = ComposableConstants.descriptionIcon,
                    leadingIconContentDescription = stringResource(R.string.appointment_description),
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