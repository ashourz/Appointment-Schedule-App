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

package com.example.movemedicalscheduleapp.view_model

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.mutableStateOf
import com.example.movemedicalscheduleapp.data.dao.AppointmentDao
import com.example.movemedicalscheduleapp.data.database.DatabaseMutex
import com.example.movemedicalscheduleapp.data.database.ScheduleDatabase
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.extensions.toSQLLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.sync.withLock

class AppointmentRepo(context: Context) : DatabaseMutex {
    private var appointmentDao: AppointmentDao
    var pastAppointmentFlow: Flow<List<Appointment>>
    var todayAppointmentFlow: Flow<List<Appointment>>
    var futureAppointmentFlow: Flow<List<Appointment>>
    var snackbarMessageFlow: MutableStateFlow<String?> = MutableStateFlow(null)


    init {
        val db: ScheduleDatabase = ScheduleDatabase.getInstance(context)!!
        appointmentDao = db.appointmentDao()
        pastAppointmentFlow = appointmentDao.getAllPastAppointments()
        todayAppointmentFlow = appointmentDao.getAllTodayAppointments()
        futureAppointmentFlow = appointmentDao.getAllFutureAppointments()
    }

    /**
     * Returns rowId on new insertion
     * Returns -1 on update
     * */
    suspend fun upsertAppointment(appointment: Appointment): Long {
        try {
            databaseWriteMutex().withLock {
                val upsertValue =  appointmentDao.upsertAppointment(appointment)
                if(upsertValue >= 0L){
                    snackbarMessageFlow.value = "Appointment Added"
                }else if(upsertValue == -1L){
                    snackbarMessageFlow.value = "Appointment Updated"
                }
                return upsertValue
            }
        } catch (e: SQLiteConstraintException) {
            snackbarMessageFlow.value = "Database Operation Failed"
            return -1L
        } catch (e: Exception) {
            snackbarMessageFlow.value = "An Unknown Error Has Occurred"
            return -1L
        }
    }

    /**
     * Returns number of rows deleted successfully
     * */
    suspend fun deleteAppointment(appointment: Appointment): Int {
        try {
            databaseWriteMutex().withLock {
                val deleteCount = appointmentDao.deleteAppointment(appointment)
                if(deleteCount < 1){
                    snackbarMessageFlow.value = "Appointment Cancellation Failed"
                }else{
                    snackbarMessageFlow.value = "Appointment Canceled"
                }
                return deleteCount
            }
        } catch (e: SQLiteConstraintException) {
            snackbarMessageFlow.value = "Database Operation Failed During Cancellation"
            return -1
        } catch (e: Exception) {
            snackbarMessageFlow.value = "An Unknown Error Has Occurred During Cancellation"
            return -1
        }
    }

    /**
     * Returns list of overlapping
     * */
    suspend fun getOverlappingAppointments(appointment: Appointment): List<Appointment> {
        try {
            databaseWriteMutex().withLock {
                return appointmentDao.getOverlappingAppointments(
                    locationInt = appointment.location.zipCode,
                    apptStartSQLLong = appointment.datetime.toSQLLong(),
                    apptEndSQLLong = appointment.datetime.plus(appointment.duration).toSQLLong()
                )
            }
        } catch (e: SQLiteConstraintException) {
            snackbarMessageFlow.value = "Database Operation Failed During Validation"
            return emptyList()
        } catch (e: Exception) {
            snackbarMessageFlow.value = "An Unknown Error Has Occurred During Validation"
            return emptyList()
        }
    }

    /**
     * Returns list of overlapping
     * */
    suspend fun deleteAll() {
        try {
            databaseWriteMutex().withLock {
                return appointmentDao.deleteAll()
            }
        } catch (e: SQLiteConstraintException) {
            snackbarMessageFlow.value = "Database Operation Failed During Deletion"
        } catch (e: Exception) {
            snackbarMessageFlow.value = "An Unknown Error Has Occurred During Deletion"
        }
    }

}
