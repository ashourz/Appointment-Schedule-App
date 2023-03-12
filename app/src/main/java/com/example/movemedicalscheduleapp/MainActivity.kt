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

