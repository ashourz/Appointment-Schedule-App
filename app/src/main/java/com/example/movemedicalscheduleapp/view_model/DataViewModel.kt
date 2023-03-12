package com.example.movemedicalscheduleapp.view_model

import android.app.Application
import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.extensions.toExistingApptErrorMessage
import com.example.movemedicalscheduleapp.ui.ui_data_class.TempAppointmentProperties
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class DataViewModel(application: Application) : AndroidViewModel(application) {
    private val dataViewModelScope = viewModelScope.plus(CoroutineName("viewModelCoroutine") + SupervisorJob() + Dispatchers.IO)
//    private val mainCoroutineScope = viewModelScope.plus(CoroutineName("mainThreadCoroutine") + SupervisorJob() + Dispatchers.Main)

    private val appointmentRepo: AppointmentRepo = AppointmentRepo(application)

    //region: Temporary Appointment Properties
    private var _temporaryAppointmentPropertiesFlow: MutableStateFlow<TempAppointmentProperties> = MutableStateFlow(TempAppointmentProperties())
    val temporaryAppointmentPropertiesFlow: StateFlow<TempAppointmentProperties> = _temporaryAppointmentPropertiesFlow.asStateFlow()
    fun updateTempAppointmentProperties(updatedAppointmentProperties: TempAppointmentProperties) {
        _temporaryAppointmentPropertiesFlow.value = updatedAppointmentProperties
    }
    //endregion

    //region: Appointment Flows
    val pastAppointmentStateFlow: Flow<List<Appointment>> = appointmentRepo.pastAppointmentFlow.flowOn(Dispatchers.IO)
    val todayAppointmentStateFlow: Flow<List<Appointment>> = appointmentRepo.todayAppointmentFlow.flowOn(Dispatchers.IO)
    val futureAppointmentStateFlow: Flow<List<Appointment>> = appointmentRepo.futureAppointmentFlow.flowOn(Dispatchers.IO)
    //endregion

    //region: Schedule Scaffold States
    /**
     * Schedule Scaffold LazyList States and Operations
     * */
    private val _scheduleScaffoldLazyListState: MutableStateFlow<LazyListState> = MutableStateFlow(LazyListState())
    val scheduleScaffoldLazyListState: StateFlow<LazyListState> = _scheduleScaffoldLazyListState.asStateFlow()
    // Used by init in DataViewModel
    // Scroll Schedule Scaffold LazyListState to the top of the Today Appointment Card section
    private fun initializeScrollScheduleLazyListToToday() {
        // Get Sum of all date groups plus all appointments within each.
        // This will equal the number of items produced by pastAppointmentStateFlow.
        // This will equal the index of the today item
        dataViewModelScope.launch {
            pastAppointmentStateFlow.firstOrNull()?.groupBy { it.datetime.toLocalDate() }?.map { 1 + it.value.count() }?.sum()?.let { todayIndex ->
                if (todayIndex < _scheduleScaffoldLazyListState.value.layoutInfo.totalItemsCount) {
                        _scheduleScaffoldLazyListState.value.scrollToItem(todayIndex, 0)
                }
            }
        }
    }
    // Used by Today Button in Schedule Scaffold:
    // Animate Scroll Schedule Scaffold LazyListState to the top of the Today Appointment Card section
    suspend fun animateScrollScheduleLazyListToToday() {
        // Get Sum of all date groups plus all appointments within each.
        // This will equal the number of items produced by pastAppointmentStateFlow.
        // This will equal the index of the today item
            pastAppointmentStateFlow.firstOrNull()?.groupBy { it.datetime.toLocalDate() }?.map { 1 + it.value.count() }?.sum()?.let { todayIndex ->
                if (todayIndex < _scheduleScaffoldLazyListState.value.layoutInfo.totalItemsCount) {
                    _scheduleScaffoldLazyListState.value.animateScrollToItem(todayIndex, 0)
                }
            }
    }
    /**
     * Cancel Appointment States and Operations
     * */
    //Mutable state of Appointment selected to be deleted for displaying it to ConfirmCancelPopup
    private var _cancelAppointment: MutableStateFlow<Appointment?> = MutableStateFlow(null)
    // Holds state of Appointment selected to be deleted for displaying it to ConfirmCancelPopup
    val cancelAppointment: StateFlow<Appointment?> = _cancelAppointment.asStateFlow()
    //Updates state of Appointment selected to be deleted for displaying it to ConfirmCancelPopup
    fun updateCancelAppointment(appointment: Appointment?) {
        _cancelAppointment.value = appointment
    }

    /**
     * Expanded AppointmentCard States and Operations
     * Maintains only one expanded AppointmentCard at anytime.
     * Ensures that canceling an expanded card does not open another card that replaces its index.
     * */
    private var _expandedAppointmentCardIdStateFlow: MutableStateFlow<Long?> = MutableStateFlow(null)
    val expandedAppointmentCardIdStateFlow: StateFlow<Long?> = _expandedAppointmentCardIdStateFlow.asStateFlow()
    fun updateExpandedAppointmentCardId(expandedAppointmentCardId: Long?){
        _expandedAppointmentCardIdStateFlow.value = expandedAppointmentCardId
    }
    //endregion

    //region: Snackbar Message Flow and State
    val snackbarHostStateFlow: StateFlow<SnackbarHostState> = MutableStateFlow(SnackbarHostState()).asStateFlow()
    val snackbarMessages: StateFlow<String?> = appointmentRepo.snackbarMessageFlow.asStateFlow()
    //endregion

    init{
        /**
         * Scroll to Todays Date in Schedule Scaffold on opening the app
         * */
        initializeScrollScheduleLazyListToToday()
    }

    //region: Database Operations
    /**
     * Returns rowId on new insertion
     * Returns -1 on update
     * */
    suspend fun upsertAppointment(appointment: Appointment): Long {
        return withContext(dataViewModelScope.coroutineContext) {
            appointmentRepo.upsertAppointment(appointment)
        }
    }

    /**
     * Returns number of rows deleted successfully
     * */
    suspend fun deleteAppointment(appointment: Appointment): Int {
        return withContext(dataViewModelScope.coroutineContext) {
            appointmentRepo.deleteAppointment(appointment)
        }
    }

    suspend fun getOverlappingAppointments(localContext: Context, appointment: Appointment): List<Appointment> {
        withContext(dataViewModelScope.coroutineContext) {
            appointmentRepo.getOverlappingAppointments(appointment)
        }.let { overlappingAppointmentList ->
            updateTempAppointmentProperties(
                _temporaryAppointmentPropertiesFlow.value.copy(
                    existingApptError = overlappingAppointmentList.toExistingApptErrorMessage(localContext)
                )
            )
            return overlappingAppointmentList
        }
    }
    //endregion
}