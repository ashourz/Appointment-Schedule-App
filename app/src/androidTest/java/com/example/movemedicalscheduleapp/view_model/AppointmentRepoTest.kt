package com.example.movemedicalscheduleapp.view_model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movemedicalscheduleapp.data.database.TypeConverter
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.enums.ApptLocation
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
class AppointmentRepoTest: TestCase() {
    private lateinit var repo: AppointmentRepo
    private val typeConverter: TypeConverter = TypeConverter()

    @get:Rule
    val instantTasExecutorRule = InstantTaskExecutorRule()

    @Before
    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        repo = AppointmentRepo(context)
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
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = repo.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = repo.todayAppointmentFlow.first()
        Truth.assertThat(resultList.any { it == correctedAppt }).isTrue()
    }

    @Test
    fun upsertExistingRead() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val initialAppt = Appointment(
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = repo.upsertAppointment(initialAppt)
        Truth.assertThat(resultId >= 0).isTrue()
        val changedAppt = initialAppt.copy(
            rowid = resultId,
            location = ApptLocation.ORLANDO
        )
        val updateResultId = repo.upsertAppointment(changedAppt)
        Truth.assertThat(updateResultId == -1L).isTrue()
        val resultList = repo.todayAppointmentFlow.first()
        Truth.assertThat(resultList.any { it == changedAppt }).isTrue()


    }


    @Test
    fun upsertDeleteRead() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt = Appointment(
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = repo.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList1 = repo.todayAppointmentFlow.first()
        Truth.assertThat(resultList1.any { it == correctedAppt }).isTrue()

        val deleteCount = repo.deleteAppointment(
            appt.copy(
                rowid = resultId
            )
        )
        Truth.assertThat(deleteCount == 1).isTrue()
        val resultList2 = repo.todayAppointmentFlow.first()
        Truth.assertThat(resultList2.none { it == correctedAppt }).isTrue()
    }

    @Test
    fun deleteInvalid() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt = Appointment(
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val deleteCount = repo.deleteAppointment(appt)
        Truth.assertThat(deleteCount == 0).isTrue()
        val resultList2 = repo.todayAppointmentFlow.first()
        Truth.assertThat(resultList2.none { it == appt }).isTrue()
    }

    @Test
    fun upsertMultipleGetAll() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now())
        )!!
        val appt1 = Appointment(
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description 1"
        )
        val resultId1 = repo.upsertAppointment(appt1)
        Truth.assertThat(resultId1 >= 0).isTrue()
        val correctedAppt1 = appt1.copy(
            rowid = resultId1
        )
        val appt2 = Appointment(
            datetime = sanitizedDateTime,
            location = ApptLocation.ORLANDO,
            duration = Duration.ofMinutes(15L),
            description = "Test Description 2"
        )
        val resultId2 = repo.upsertAppointment(appt2)
        Truth.assertThat(resultId2 >= 0).isTrue()
        val correctedAppt2 = appt2.copy(
            rowid = resultId2
        )
        val resultList = repo.todayAppointmentFlow.first()
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
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = repo.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = repo.todayAppointmentFlow.first()
        Truth.assertThat(resultList.any { it == correctedAppt }).isTrue()
    }

    @Test
    fun upsertGetTodayApptInvalid() = kotlinx.coroutines.test.runTest {
        val sanitizedDateTime = typeConverter.longToLocalDateTime(
            typeConverter.localDateTimeToLong(LocalDateTime.now().plusDays(1L))
        )!!
        val appt = Appointment(
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = repo.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = repo.todayAppointmentFlow.first()
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
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = repo.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = repo.futureAppointmentFlow.first()
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
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = repo.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = repo.futureAppointmentFlow.first()
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
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = repo.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = repo.pastAppointmentFlow.first()
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
            datetime = sanitizedDateTime,
            location = ApptLocation.DALLAS,
            duration = Duration.ofMinutes(45L),
            description = "Test Description"
        )
        val resultId = repo.upsertAppointment(appt)
        Truth.assertThat(resultId >= 0).isTrue()
        val correctedAppt = appt.copy(
            rowid = resultId
        )
        val resultList = repo.pastAppointmentFlow.first()
        Truth.assertThat(resultList.any {
            it == correctedAppt
        }).isFalse()
    }
}