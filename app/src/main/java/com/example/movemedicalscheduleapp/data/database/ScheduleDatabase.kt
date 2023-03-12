package com.example.movemedicalscheduleapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.dao.AppointmentDao
import com.example.movemedicalscheduleapp.data.entity.Appointment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(
    entities = [
        Appointment::class
    ],
    views = [],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        private var INSTANCE: ScheduleDatabase? = null
        private const val NUMBER_OF_THREADS = 20
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        //Maintain current database instance if exists
        fun getInstance(context: Context): ScheduleDatabase? {
            if (INSTANCE == null) {
                synchronized(Database::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ScheduleDatabase::class.java, context.getString(R.string.schedule_database_name)
                        )
                            //Allows room to recreate database if schema is not found
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private val roomDatabaseCallback: Callback =
            object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    databaseWriteExecutor.execute {
                        CoroutineScope(Dispatchers.IO).launch {

                        }
                    }
                }
            }
    }
}