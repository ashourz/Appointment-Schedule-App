package com.example.movemedicalscheduleapp.ui.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movemedicalscheduleapp.view_model.DataViewModel

@Composable
fun MainActivityNavHost(
    activity: AppCompatActivity,
    dataViewModel: DataViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavDestinationEnum.SCHEDULE.destination
    ){

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
                activity = activity ,
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
                activity = activity ,
                dataViewModel = dataViewModel,
                update = true,
                onNavigateAway = {
                    navController.navigateUp()
                }
            )
        }
    }
}