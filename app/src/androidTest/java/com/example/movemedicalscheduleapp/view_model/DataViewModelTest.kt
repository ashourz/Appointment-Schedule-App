package com.example.movemedicalscheduleapp.view_model

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movemedicalscheduleapp.data.database.TypeConverter
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.google.common.truth.Truth
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.Duration
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DataViewModelTest: TestCase() {
    private lateinit var dataViewModel: DataViewModel
    private val typeConverter: TypeConverter = TypeConverter()

    @get:Rule
    val instantTasExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        super.setUp()
        val application = ApplicationProvider.getApplicationContext() as Application
        dataViewModel = DataViewModel(application)
    }

    @After
    @Throws(IOException::class)
    public override fun tearDown() {
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
}