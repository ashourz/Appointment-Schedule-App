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