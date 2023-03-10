package com.example.movemedicalscheduleapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.extensions.toDisplayFormat
import com.example.movemedicalscheduleapp.ui.ComposableConstants
import com.example.movemedicalscheduleapp.ui.components.cards.AppointmentCard
import com.example.movemedicalscheduleapp.ui.components.icons.SizedIcon
import com.example.movemedicalscheduleapp.ui.components.toolbars.ScheduleTopBar
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.time.LocalDate

//@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleScaffold(
    dataViewModel: DataViewModel,
    onAddNewAppointment: () -> Unit = {}
) {
    val pastAppointments by dataViewModel.pastAppointmentStateFlow.collectAsState(initial = emptyList())
    val todayAppointments by dataViewModel.todayAppointmentStateFlow.collectAsState(initial = emptyList())
    val futureAppointments by dataViewModel.futureAppointmentStateFlow.collectAsState(initial = emptyList())
    val lazyListState = rememberLazyListState()
    val localContext = LocalContext.current
    val coroutineScopeMain = rememberCoroutineScope().plus(Dispatchers.Main)

    Scaffold(
        topBar = { ScheduleTopBar("My Appointment Schedule") },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Button(onClick = {
                        val todayIndex = pastAppointments.groupBy { it.datetime.toLocalDate() }.map{1+it.value.count()}.sum()
                        if(todayIndex < lazyListState.layoutInfo.totalItemsCount) {
                            coroutineScopeMain.launch {
                                lazyListState.scrollToItem(todayIndex, 0)
                            }
                        }
                    }) {
                        SizedIcon(iconDrawable = ComposableConstants.todayIcon)
                        Text("Scroll To Today")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = onAddNewAppointment) {
                        SizedIcon(iconDrawable = ComposableConstants.addIcon)
                    }
                }
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
                    Text(localDate.toDisplayFormat(localContext))
                }
                appointments.sortedBy { it.datetime }.forEach {appointment ->
                    item {
                        AppointmentCard(
                            appointment = appointment,
                            onEditAppointment = { editAppt ->

                            },
                            onCancelAppointment = { cancelAppt ->

                            })
                    }
                }
            }
            stickyHeader(
                key = "TODAY"
            ) {
                Text("Today: ".plus(LocalDate.now().toDisplayFormat(localContext)))
            }
            if(todayAppointments.isNotEmpty()) {
                todayAppointments.sortedBy { it.datetime }.forEach { appointment ->
                    item {
                        AppointmentCard(
                            appointment = appointment,
                            onEditAppointment = { editAppt ->

                            },
                            onCancelAppointment = { cancelAppt ->

                            })
                    }
                }
            }else{
                item {
                    Text("No Appointments for Today")

                }
            }
            futureAppointments.groupBy { it.datetime.toLocalDate() }.forEach { (localDate, appointments) ->
                stickyHeader {
                    Text(localDate.toDisplayFormat(localContext))
                }
                appointments.sortedBy { it.datetime }.forEach {appointment ->
                    item {
                        AppointmentCard(
                            appointment = appointment,
                            onEditAppointment = { editAppt ->

                            },
                            onCancelAppointment = { cancelAppt ->

                            })
                    }
                }
            }
        }
    }
}