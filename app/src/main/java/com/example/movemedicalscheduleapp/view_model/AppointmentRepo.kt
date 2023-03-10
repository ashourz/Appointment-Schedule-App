package com.example.movemedicalscheduleapp.view_model

import android.content.Context
import com.example.movemedicalscheduleapp.data.dao.AppointmentDao
import com.example.movemedicalscheduleapp.data.database.DatabaseMutex
import com.example.movemedicalscheduleapp.data.database.ScheduleDatabase
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.extensions.toSQLLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.withLock

class AppointmentRepo(context: Context) : DatabaseMutex {
    private var appointmentDao: AppointmentDao
    var pastAppointmentFlow: Flow<List<Appointment>>
    var todayAppointmentFlow: Flow<List<Appointment>>
    var futureAppointmentFlow: Flow<List<Appointment>>

    init{
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
    suspend fun upsertAppointment(appointment: Appointment): Long{
        databaseWriteMutex().withLock {
            return appointmentDao.upsertAppointment(appointment)
        }
    }

    /**
     * Returns number of rows deleted successfully
     * */
    suspend fun deleteAppointment(appointment: Appointment): Int{
        databaseWriteMutex().withLock {
            return appointmentDao.deleteAppointment(appointment)
        }
    }

    /**
     * Returns list of overlapping
     * */
    suspend fun getOverlappingAppointments(appointment: Appointment): List<Appointment> {
        databaseWriteMutex().withLock {
            return appointmentDao.getOverlappingAppointments(
                locationInt = appointment.location.zipCode,
                apptStartSQLLong = appointment.datetime.toSQLLong(),
                apptEndSQLLong = appointment.datetime.plus(appointment.duration).toSQLLong()
            )
        }
    }
}