package com.example.movemedicalscheduleapp.data.database

import kotlinx.coroutines.sync.Mutex

interface DatabaseMutex {
    companion object {
        //Request DB Mutex
        private val databaseWriteMutex = Mutex()
    }
    fun databaseWriteMutex(): Mutex {return databaseWriteMutex
    }

}