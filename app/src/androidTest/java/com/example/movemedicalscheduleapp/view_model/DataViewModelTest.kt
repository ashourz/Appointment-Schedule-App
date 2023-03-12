package com.example.movemedicalscheduleapp.view_model

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movemedicalscheduleapp.data.database.TypeConverter
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.ui.ui_data_class.TempAppointmentProperties
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DataViewModelTest: TestCase() {
    private lateinit var dataViewModel: DataViewModel
    private val typeConverter: TypeConverter = TypeConverter()
    private lateinit var localContext: Context
    @get:Rule
    val instantTasExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        super.setUp()
        val application = ApplicationProvider.getApplicationContext() as Application
        dataViewModel = DataViewModel(application)
        localContext = application.applicationContext
    }

    @After
    public override fun tearDown() {
        super.tearDown()
        //Cancel All Appointments
        runBlocking {
            withContext(Dispatchers.IO) {
                dataViewModel.deleteAll()
            }
        }
    }
    @Test
    fun updateTempAppointmentPropertiesValues() = kotlinx.coroutines.test.runTest{
        val tempDateTime = LocalDateTime.now().plusDays(1L)
        val appt = Appointment(
            title = "Test Title",
            datetime = LocalDateTime.now(),
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        dataViewModel.updateTempAppointmentProperties(
            TempAppointmentProperties(
                editingAppointment = appt,
                title = "Temp Title",
                date = tempDateTime.toLocalDate(),
                time = tempDateTime.toLocalTime(),
                duration = Duration.ofMinutes(45L),
                location = ApptLocation.ORLANDO,
                description = "Temp Description",
                existingApptError = "This is an Error"
            )
        )
        val tempAppointmentProperties1 = dataViewModel.temporaryAppointmentPropertiesFlow.first()
        assertThat(
            tempAppointmentProperties1.editingAppointment == appt &&
            tempAppointmentProperties1.title == "Temp Title" &&
            tempAppointmentProperties1.date == tempDateTime.toLocalDate() &&
            tempAppointmentProperties1.time == tempDateTime.toLocalTime() &&
            tempAppointmentProperties1.duration == Duration.ofMinutes(45L) &&
            tempAppointmentProperties1.location == ApptLocation.ORLANDO &&
            tempAppointmentProperties1.description == "Temp Description" &&
            tempAppointmentProperties1.existingApptError == "This is an Error"
        ).isTrue()
        //Reset tempAppointmentProperties1
        dataViewModel.updateTempAppointmentProperties(
            TempAppointmentProperties()
        )
        val tempAppointmentProperties2 = dataViewModel.temporaryAppointmentPropertiesFlow.first()
        assertThat(
            tempAppointmentProperties2.editingAppointment == null &&
            tempAppointmentProperties2.title == null &&
            tempAppointmentProperties2.date == LocalDate.now() &&
            Duration.between(LocalTime.now(),tempAppointmentProperties1.time).seconds < 1L &&
            tempAppointmentProperties2.duration == Duration.ZERO &&
            tempAppointmentProperties2.location == ApptLocation.UNKNOWN &&
            tempAppointmentProperties2.description == null &&
            tempAppointmentProperties2.existingApptError == null
        ).isTrue()
    }

    @Test
    fun updateCancelAppointmentValue() = kotlinx.coroutines.test.runTest{
        val appt = Appointment(
            title = "Test Title",
            datetime = LocalDateTime.now(),
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        dataViewModel.updateCancelAppointment(appt)
        val result1 = dataViewModel.cancelAppointment.first()
        assertThat(result1 == appt).isTrue()
        dataViewModel.updateCancelAppointment(null)
        val result2 = dataViewModel.cancelAppointment.first()
        assertThat(result2 == null).isTrue()
    }

    @Test
    fun updateExpandedAppointmentCardIdValue() = kotlinx.coroutines.test.runTest{
        val expandedAppointmentCardId = 10L
        dataViewModel.updateExpandedAppointmentCardId(expandedAppointmentCardId)
        val result1 = dataViewModel.expandedAppointmentCardIdStateFlow.first()
        assertThat(result1 == expandedAppointmentCardId).isTrue()
        dataViewModel.updateExpandedAppointmentCardId(null)
        val result2 = dataViewModel.expandedAppointmentCardIdStateFlow.first()
        assertThat(result2 == null).isTrue()
    }

    @Test
    fun upsertNewRead() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = dataViewModel.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = dataViewModel.todayAppointmentStateFlow.first()
        Truth.assertThat(resultList.any { it == correctedAppt }).isTrue()
    }

    @Test
    fun upsertExistingRead() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val initialAppt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = dataViewModel.upsertAppointment(initialAppt)
        Truth.assertThat(resultId >= 0).isTrue()
        val changedAppt = initialAppt.copy(
            rowid = resultId,
            location = ApptLocation.ORLANDO
        )
        val updateResultId = dataViewModel.upsertAppointment(changedAppt)
        Truth.assertThat(updateResultId == -1L).isTrue()
        val resultList = dataViewModel.todayAppointmentStateFlow.first()
        Truth.assertThat(resultList.any { it == changedAppt }).isTrue()


    }


    @Test
    fun upsertDeleteRead() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = dataViewModel.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList1 = dataViewModel.todayAppointmentStateFlow.first()
        Truth.assertThat(resultList1.any { it == correctedAppt }).isTrue()

        val deleteCount = dataViewModel.deleteAppointment(
            appt.copy(
                rowid = resultId
            )
        )
        Truth.assertThat(deleteCount == 1).isTrue()
        val resultList2 = dataViewModel.todayAppointmentStateFlow.first()
        Truth.assertThat(resultList2.none { it == correctedAppt }).isTrue()
    }

    @Test
    fun deleteInvalid() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val deleteCount = dataViewModel.deleteAppointment(appt)
        Truth.assertThat(deleteCount == 0).isTrue()
        val resultList2 = dataViewModel.todayAppointmentStateFlow.first()
        Truth.assertThat(resultList2.none { it == appt }).isTrue()
    }

    @Test
    fun upsertMultipleGetAll() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt1 = Appointment(
            title = "Test Title 1",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description 1"
        )
        val resultId1 = dataViewModel.upsertAppointment(appt1)
        Truth.assertThat(resultId1 >= 0).isTrue()
        val correctedAppt1 = appt1.copy(
            rowid = resultId1
        )
        val appt2 = Appointment(
            title = "Test Title 2",
            datetime = sanitizedDateTime,
            location = ApptLocation.ORLANDO,
            duration = Duration.ofMinutes(15L),
            description = "Test Description 2"
        )
        val resultId2 = dataViewModel.upsertAppointment(appt2)
        Truth.assertThat(resultId2 >= 0).isTrue()
        val correctedAppt2 = appt2.copy(
            rowid = resultId2
        )
        val resultList = dataViewModel.todayAppointmentStateFlow.first()
        Truth.assertThat(
            resultList.containsAll(
                listOf(correctedAppt1, correctedAppt2)
            )
        ).isTrue()

    }

    @Test
    fun upsertGetTodayAppt() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = dataViewModel.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = dataViewModel.todayAppointmentStateFlow.first()
        Truth.assertThat(resultList.any { it == correctedAppt }).isTrue()
    }

    @Test
    fun upsertGetTodayApptInvalid() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now().plusDays(1L))
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = dataViewModel.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = dataViewModel.todayAppointmentStateFlow.first()
        Truth.assertThat(resultList.any {
            it == correctedAppt
        }).isFalse()
    }

    @Test
    fun upsertGetFutureAppt() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now().plusDays(1L))
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = dataViewModel.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = dataViewModel.futureAppointmentStateFlow.first()
        Truth.assertThat(resultList.any {
            it == correctedAppt
        }).isTrue()
    }

    @Test
    fun upsertGetFutureApptInvalid() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = dataViewModel.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = dataViewModel.futureAppointmentStateFlow.first()
        Truth.assertThat(resultList.any {
            it == correctedAppt
        }).isFalse()
    }

    @Test
    fun upsertGetPastAppt() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now().minusDays(1L))
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = dataViewModel.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = dataViewModel.pastAppointmentStateFlow.first()
        Truth.assertThat(resultList.any {
            it == correctedAppt
        }).isTrue()
    }

    @Test
    fun upsertGetPastApptInvalid() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = dataViewModel.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = dataViewModel.pastAppointmentStateFlow.first()
        Truth.assertThat(resultList.any {
            it == correctedAppt
        }).isFalse()
    }

    @Test
    fun partialOverlappingAppointments() = kotlinx.coroutines.test.runTest {
        //Insert Appt 1
        val sanitizedDateTime1 = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt1 = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime1,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId1 = dataViewModel.upsertAppointment(appt1)
        Truth.assertThat(resultId1 >= 0).isTrue()
        val correctedAppt1 = appt1.copy(
            rowid = resultId1
        )
        //Prep Appt 2
        val sanitizedDateTime2 = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now().minusMinutes(10L))
        )!!
        val appt2 = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime2,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        //Confirm Overlap
        val resultList2 = dataViewModel.getOverlappingAppointments(localContext, appt2)
        Truth.assertThat(resultList2.any {
            it == correctedAppt1
        }).isTrue()
    }

    @Test
    fun fullOverlappingAppointments() = kotlinx.coroutines.test.runTest {
        //Insert Appt1
        val sanitizedDateTime1 = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt1 = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime1,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId1 = dataViewModel.upsertAppointment(appt1)
        Truth.assertThat(resultId1 >= 0).isTrue()
        val correctedAppt1 = appt1.copy(
            rowid = resultId1
        )
        //Prep Appt 2
        val sanitizedDateTime2 = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now().minusMinutes(10L))
        )!!
        val appt2 = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime2,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(65L),
            description = "Test Description"
        )
        //Confirm Overlap
        val resultList2 = dataViewModel.getOverlappingAppointments(localContext, appt2)
        Truth.assertThat(resultList2.any {
            it == correctedAppt1
        }).isTrue()
    }

    @Test
    fun noOverlappingAppointments() = kotlinx.coroutines.test.runTest {
        //Insert Appt1
        val sanitizedDateTime1 = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt1 = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime1,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId1 = dataViewModel.upsertAppointment(appt1)
        Truth.assertThat(resultId1 >= 0).isTrue()
        val correctedAppt1 = appt1.copy(
            rowid = resultId1
        )
        //Prep Appt 2
        val sanitizedDateTime2 = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now().plusHours(2L))
        )!!
        val appt2 = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime2,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(65L),
            description = "Test Description"
        )
        //Confirm Overlap
        val resultList2 = dataViewModel.getOverlappingAppointments(localContext, appt2)
        Truth.assertThat(resultList2.none {
            it == correctedAppt1
        }).isTrue()
    }

    @Test
    fun deleteAll() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt = Appointment(
            title = "Test Title",
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        dataViewModel.deleteAll()
        val resultList2 = dataViewModel.todayAppointmentStateFlow.first()
        Truth.assertThat(resultList2.none { it == appt }).isTrue()
    }
}