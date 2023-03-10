package com.example.movemedicalscheduleapp.data.dao

import androidx.room.*
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.extensions.getTodaySQLLong
import com.example.movemedicalscheduleapp.extensions.getTomorrowSQLLong
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    /**
     * Returns rowId on new insertion
     * Returns -1 on update
     * */
    @Upsert
    fun upsertAppointment(appointment: Appointment): Long

    /**
     * Returns number of rows deleted successfully
     * */
    @Delete
    fun deleteAppointment(appointment: Appointment): Int

    @Transaction
    @Query("""SELECT * FROM appointment_table WHERE datetime >= :todaySQLiteLong AND datetime < (:todaySQLiteLong + 86400) """)
    fun getAllTodayAppointments(todaySQLiteLong: Long = getTodaySQLLong()): Flow<List<Appointment>>

    @Transaction
    @Query("""SELECT * FROM appointment_table WHERE datetime < :todaySQLiteLong """)
    fun getAllPastAppointments(todaySQLiteLong: Long = getTodaySQLLong()): Flow<List<Appointment>>

    @Transaction
    @Query("""SELECT * FROM appointment_table WHERE datetime >= :tomorrowSQLiteLong """)
    fun getAllFutureAppointments(tomorrowSQLiteLong: Long = getTomorrowSQLLong()): Flow<List<Appointment>>

//    @Transaction
//    @Query("SELECT * FROM appointment_table")
//    fun getAllAppointments(): List<Appointment>
}