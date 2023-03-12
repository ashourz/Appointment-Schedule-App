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

package com.example.movemedicalscheduleapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.extensions.toDisplayFormat
import com.example.movemedicalscheduleapp.ui.components.bottombars.ScheduleBottomBar
import com.example.movemedicalscheduleapp.ui.components.cards.AppointmentCard
import com.example.movemedicalscheduleapp.ui.components.toolbars.BasicTopBar
import com.example.movemedicalscheduleapp.ui.popups.ConfirmDeletePopup
import com.example.movemedicalscheduleapp.ui.ui_data_class.TempAppointmentProperties
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleScaffold(
    dataViewModel: DataViewModel,
    onNavigateToAddAppointment: () -> Unit,
    onNavigateToUpdateAppointment: () -> Unit
) {
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    //region: Schedule Scaffold LazyListState
    val scheduleScaffoldLazyListState by dataViewModel.scheduleScaffoldLazyListState.collectAsState()
    //Run onInitialScheduleLoad on first starting the application from a Destroyed State (scrolling lazylist to today header)
    val initialScheduleLoad by dataViewModel.initialScheduleLoad.collectAsState()
    LaunchedEffect(key1 = initialScheduleLoad){
        if(!initialScheduleLoad){
            coroutineScope.launch {
                dataViewModel.onInitialScheduleLoad()
            }
        }
    }
    //endregion

    //region: Data States
    val pastAppointments by dataViewModel.pastAppointmentStateFlow.collectAsState(initial = emptyList())
    val todayAppointments by dataViewModel.todayAppointmentStateFlow.collectAsState(initial = emptyList())
    val futureAppointments by dataViewModel.futureAppointmentStateFlow.collectAsState(initial = emptyList())
    val cancelAppointment by dataViewModel.deleteAppointment.collectAsState()
    val expandedAppointmentCardId by dataViewModel.expandedAppointmentCardIdStateFlow.collectAsState()
    //endregion

    //region: Snackbar State, Messages and LaunchedEffect
    val snackbarHostState by dataViewModel.snackbarHostStateFlow.collectAsState()
    val snackbarMessages by dataViewModel.snackbarMessages.collectAsState(initial = null)
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

    /**
     * onExpandAppointmentCard is executed when any Appointment Card is clicked
     * Updates expandedAppointmentCardIdStateFlow to extend or close an AppointmentCard, only having one AppointmentCard open at any time.
     * */
    val onExpandAppointmentCard = { expandedAppointmentId: Long? ->
        if (expandedAppointmentCardId != expandedAppointmentId) {
            dataViewModel.updateExpandedAppointmentCardId(expandedAppointmentId)
        } else {
            dataViewModel.updateExpandedAppointmentCardId(null)
        }
    }

    /**
     * onUpdateAppointment is executed when Update button of any Appointment Card is clicked
     * Updates editingAppointment values of TempAppointmentProperties then navigates to UpdateAppointment screen in UpsertScaffold
     * */
    val onUpdateAppointment = { updateAppt: Appointment ->
        dataViewModel.updateTempAppointmentProperties(
            TempAppointmentProperties(
                editingAppointment = updateAppt,
                title = updateAppt.title,
                date = updateAppt.datetime.toLocalDate(),
                time = updateAppt.datetime.toLocalTime(),
                duration = updateAppt.duration,
                location = updateAppt.location,
                description = updateAppt.description
            )
        )
        onNavigateToUpdateAppointment()
    }

    /**
     * onCancelAppointment is executed when Update button of any Appointment Card is clicked
     * Updates updateCancelAppointment state to Appointment selected for cancellation.
     * This in turn triggers ConfirmCancelPopup to be displayed on non-null updateCancelAppointment values
     * */
    val onDeleteAppointment = { cancelAppt: Appointment ->
        dataViewModel.updateCancelAppointment(cancelAppt)
    }

    Box() {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { BasicTopBar(stringResource(R.string.my_appointment_schedule)) },
            bottomBar = {
                ScheduleBottomBar(
                    onScrollToToday = {
                        coroutineScope.launch {
                            dataViewModel.animateScrollScheduleLazyListToToday()
                        }
                    },
                    onAddFabClick = { onNavigateToAddAppointment() }
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                state = scheduleScaffoldLazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pastAppointments.groupBy { it.datetime.toLocalDate() }.forEach { (localDate, appointments) ->
                    stickyHeader(
                        contentType = localContext.getString(R.string.sticky_header)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                text = localDate.toDisplayFormat(localContext),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    appointments.sortedBy { it.datetime }.forEach { appointment ->
                        item(
                            contentType = localContext.getString(R.string.appointment_card)
                        ) {
                            AppointmentCard(
                                modifier = Modifier.semantics {
                                    contentDescription = localContext.getString(R.string.appointment_card)
                                },
                                appointment = appointment,
                                expanded = (expandedAppointmentCardId == appointment.rowid),
                                onExpandAppointmentCard = {
                                    onExpandAppointmentCard(appointment.rowid)
                                },
                                onUpdateAppointment = { updateAppt ->
                                    onUpdateAppointment(updateAppt)
                                },
                                onDeleteAppointment = { cancelAppt ->
                                    onDeleteAppointment(cancelAppt)
                                })
                        }
                    }
                }
                stickyHeader(
                    contentType = localContext.getString(R.string.sticky_header)
                ) {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            text = stringResource(R.string.today_prefix).plus(LocalDate.now().toDisplayFormat(localContext)),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (todayAppointments.isNotEmpty()) {
                    todayAppointments.sortedBy { it.datetime }.forEach { appointment ->
                        item(
                            contentType = localContext.getString(R.string.appointment_card)
                        ) {
                            AppointmentCard(
                                modifier = Modifier.semantics {
                                    contentDescription = localContext.getString(R.string.appointment_card)
                                },
                                appointment = appointment,
                                expanded = (expandedAppointmentCardId == appointment.rowid),
                                onExpandAppointmentCard = {
                                    onExpandAppointmentCard(appointment.rowid)
                                },
                                onUpdateAppointment = { updateAppt ->
                                    onUpdateAppointment(updateAppt)
                                },
                                onDeleteAppointment = { cancelAppt ->
                                    onDeleteAppointment(cancelAppt)
                                })
                        }
                    }
                } else {
                    item {
                        Text(stringResource(R.string.no_appointments_for_today))
                    }
                }
                futureAppointments.groupBy { it.datetime.toLocalDate() }.forEach { (localDate, appointments) ->
                    stickyHeader(
                        contentType = localContext.getString(R.string.sticky_header)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                text = localDate.toDisplayFormat(localContext),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    appointments.sortedBy { it.datetime }.forEach { appointment ->
                        item(
                            contentType = localContext.getString(R.string.appointment_card)
                        ) {
                            AppointmentCard(
                                modifier = Modifier.semantics {
                                    contentDescription = localContext.getString(R.string.appointment_card)
                                },
                                appointment = appointment,
                                expanded = (expandedAppointmentCardId == appointment.rowid),
                                onExpandAppointmentCard = {
                                    onExpandAppointmentCard(appointment.rowid)
                                },
                                onUpdateAppointment = { updateAppt ->
                                    onUpdateAppointment(updateAppt)
                                },
                                onDeleteAppointment = { cancelAppt ->
                                    onDeleteAppointment(cancelAppt)
                                })
                        }
                    }
                }
                item(
                    contentType = localContext.getString(R.string.spacer)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        //Display ConfirmCancelPopup on all null
        if (cancelAppointment != null) {
            ConfirmDeletePopup(
                appointment = cancelAppointment!!,
                onCancelAppointment = {appointment ->
                    coroutineScope.launch {
                        dataViewModel.deleteAppointment(appointment)
                    }
                },
                onClose = {
                    dataViewModel.updateCancelAppointment(null)
                })
        }
    }
}