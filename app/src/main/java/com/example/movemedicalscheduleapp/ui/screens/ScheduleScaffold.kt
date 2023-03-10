package com.example.movemedicalscheduleapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.extensions.toDisplayFormat
import com.example.movemedicalscheduleapp.ui.ComposableConstants
import com.example.movemedicalscheduleapp.ui.components.bottombars.ScheduleBottomBar
import com.example.movemedicalscheduleapp.ui.components.cards.AppointmentCard
import com.example.movemedicalscheduleapp.ui.components.icons.SizedIcon
import com.example.movemedicalscheduleapp.ui.components.toolbars.ScheduleTopBar
import com.example.movemedicalscheduleapp.ui.popups.ConfirmCancelPopup
import com.example.movemedicalscheduleapp.ui.ui_data_class.TempAppointmentProperties
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleScaffold(
    dataViewModel: DataViewModel,
    onNavigateToAddAppointment: () -> Unit = {},
    onNavigateToUpdateAppointment: () -> Unit = {}
) {
    val pastAppointments by dataViewModel.pastAppointmentStateFlow.collectAsState(initial = emptyList())
    val todayAppointments by dataViewModel.todayAppointmentStateFlow.collectAsState(initial = emptyList())
    val futureAppointments by dataViewModel.futureAppointmentStateFlow.collectAsState(initial = emptyList())
    val deleteAppointment by dataViewModel.deleteAppointment.collectAsState()

    val lazyListState = rememberLazyListState()
    val localContext = LocalContext.current
    val coroutineScopeMain = rememberCoroutineScope().plus(Dispatchers.Main)

    val onEditAppointment = { editAppt: Appointment ->
        dataViewModel.updateTempAppointmentProperties(
            TempAppointmentProperties(
                editingAppointment = editAppt,
                appointmentTitle = editAppt.title,
                appointmentDate = editAppt.datetime.toLocalDate(),
                appointmentTime = editAppt.datetime.toLocalTime(),
                duration = editAppt.duration,
                appointmentLocation = editAppt.location,
                description = editAppt.description
            )
        )
        onNavigateToUpdateAppointment()
    }

    val onCancelAppointment = { cancelAppt: Appointment ->
        dataViewModel.updateDeleteAppointment(cancelAppt)
    }

    Box() {
        Scaffold(
            topBar = { ScheduleTopBar("My Appointment Schedule") },
            bottomBar = {
                ScheduleBottomBar(
                    onScrollToToday = {
                        val todayIndex = pastAppointments.groupBy { it.datetime.toLocalDate() }.map { 1 + it.value.count() }.sum()
                        if (todayIndex < lazyListState.layoutInfo.totalItemsCount) {
                            coroutineScopeMain.launch {
                                lazyListState.scrollToItem(todayIndex, 0)
                            }
                        }
                    },
                    onAddFabClick = { onNavigateToAddAppointment() }
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pastAppointments.groupBy { it.datetime.toLocalDate() }.forEach { (localDate, appointments) ->
                    stickyHeader {
                        Row(
                            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer).fillMaxWidth()
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
                        item {
                            AppointmentCard(
                                appointment = appointment,
                                onEditAppointment = { editAppt ->
                                    onEditAppointment(editAppt)
                                },
                                onCancelAppointment = { cancelAppt ->
                                    onCancelAppointment(cancelAppt)
                                })
                        }
                    }
                }
                stickyHeader {
                    Row(
                        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer).fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            text = "Today: ".plus(LocalDate.now().toDisplayFormat(localContext)),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (todayAppointments.isNotEmpty()) {
                    todayAppointments.sortedBy { it.datetime }.forEach { appointment ->
                        item {
                            AppointmentCard(
                                appointment = appointment,
                                onEditAppointment = { editAppt ->
                                    onEditAppointment(editAppt)
                                },
                                onCancelAppointment = { cancelAppt ->
                                    onCancelAppointment(cancelAppt)
                                })
                        }
                    }
                } else {
                    item {
                        Text("No Appointments for Today")

                    }
                }
                futureAppointments.groupBy { it.datetime.toLocalDate() }.forEach { (localDate, appointments) ->
                    stickyHeader {
                        Row(
                            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer).fillMaxWidth()
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
                        item {
                            AppointmentCard(
                                appointment = appointment,
                                onEditAppointment = { editAppt ->
                                    onEditAppointment(editAppt)
                                },
                                onCancelAppointment = { cancelAppt ->
                                    onCancelAppointment(cancelAppt)
                                })
                        }
                    }
                }
            }
        }
        if (deleteAppointment != null) {
            ConfirmCancelPopup(
                dataViewModel = dataViewModel,
                appointment = deleteAppointment!!,
                onClose = {
                    dataViewModel.updateDeleteAppointment(null)
                })
        }
    }
}