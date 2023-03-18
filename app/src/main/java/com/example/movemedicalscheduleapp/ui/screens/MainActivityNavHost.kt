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

package com.example.movemedicalscheduleapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movemedicalscheduleapp.view_model.DataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityNavHost(
    fragmentManager: FragmentManager,
    dataViewModel: DataViewModel
) {
    val navController = rememberNavController()

    val snackbarHostState = remember{SnackbarHostState()}
    val snackbarMessages by dataViewModel.snackbarMessages.collectAsState(initial = null)
    /**
     * Hoisted Snack Bar State to Main Activity Scaffold for singular subscription in always active activity.
     * */
    LaunchedEffect(key1 = snackbarMessages) {
        snackbarMessages?.let { snackBarMessage ->
            //Show snackbar message on every non-null value
            snackbarHostState.showSnackbar(
                message = snackBarMessage.message,
                duration = SnackbarDuration.Short
            )
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = NavDestinationEnum.SCHEDULE.destination
        ) {

            composable(NavDestinationEnum.SCHEDULE.destination) {
                ScheduleScaffold(
                    dataViewModel = dataViewModel,
                    onNavigateToAddAppointment = {
                        navController.navigate(NavDestinationEnum.ADD_APPOINTMENT.destination) {
                            launchSingleTop = true
                        }
                    },
                    onNavigateToUpdateAppointment = {
                        navController.navigate(NavDestinationEnum.EDIT_APPOINTMENT.destination) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(NavDestinationEnum.ADD_APPOINTMENT.destination) {
                UpsertScaffold(
                    fragmentManager = fragmentManager,
                    dataViewModel = dataViewModel,
                    update = false,
                    onNavigateAway = {
                        navController.navigateUp()
                    }
                )
            }

            composable(
                NavDestinationEnum.EDIT_APPOINTMENT.destination
            ) {
                UpsertScaffold(
                    fragmentManager = fragmentManager,
                    dataViewModel = dataViewModel,
                    update = true,
                    onNavigateAway = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}