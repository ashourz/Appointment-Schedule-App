package com.example.movemedicalscheduleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.movemedicalscheduleapp.extensions.findActivity
import com.example.movemedicalscheduleapp.ui.screens.MainActivityNavHost
import com.example.movemedicalscheduleapp.ui.theme.MoveMedicalScheduleAppTheme
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var dataViewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        lifecycleScope.launch() {
            setContent {
                MoveMedicalScheduleAppTheme {
                    MainActivityNavHost(
                        fragmentManager = supportFragmentManager,
                        dataViewModel = dataViewModel
                    )
                }
            }
        }
    }
}

