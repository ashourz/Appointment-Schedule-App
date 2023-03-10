package com.example.movemedicalscheduleapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movemedicalscheduleapp.data.entity.Appointment
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class DataViewModel(application: Application) : AndroidViewModel(application) {
    private val dataViewModelScope = viewModelScope.plus(CoroutineName("viewModelCoroutine") + SupervisorJob() + Dispatchers.IO)
    private val appointmentRepo: AppointmentRepo = AppointmentRepo(application)

    //region: Appointment Flows
    val pastAppointmentStateFlow: Flow<List<Appointment>> = appointmentRepo.pastAppointmentFlow
    val todayAppointmentStateFlow: Flow<List<Appointment>> = appointmentRepo.todayAppointmentFlow
    val futureAppointmentStateFlow: Flow<List<Appointment>> = appointmentRepo.futureAppointmentFlow
    //endregion

    /**
     * Returns rowId on new insertion
     * Returns -1 on update
     * */
    suspend fun upsertAppointment(appointment: Appointment): Long{
        return withContext(dataViewModelScope.coroutineContext) {
            appointmentRepo.upsertAppointment(appointment)
        }
    }

    /**
     * Returns number of rows deleted successfully
     * */
    suspend fun deleteAppointment(appointment: Appointment): Int{
        return withContext(dataViewModelScope.coroutineContext) {
            appointmentRepo.deleteAppointment(appointment)
        }
    }
}