package com.example.movemedicalscheduleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.movemedicalscheduleapp.ui.MainActivityScaffold
import com.example.movemedicalscheduleapp.ui.theme.MoveMedicalScheduleAppTheme
import com.example.movemedicalscheduleapp.view_model.DataViewModel

class MainActivity : ComponentActivity() {
    private lateinit var dataViewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

        setContent {
            MoveMedicalScheduleAppTheme {
                MainActivityScaffold(dataViewModel)
            }
        }
    }
}
