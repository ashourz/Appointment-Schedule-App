package com.example.movemedicalscheduleapp.ui.components.bottombars

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.movemedicalscheduleapp.ui.ComposableConstants
import com.example.movemedicalscheduleapp.ui.components.icons.SizedIcon

@Composable
fun ScheduleBottomBar(
    onScrollToToday: () -> Unit,
    onAddFabClick: () -> Unit
) {
    BottomAppBar(
        actions = {
            Button(
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = {onScrollToToday() }) {
                SizedIcon(iconDrawable = ComposableConstants.todayIcon)
                Text("Scroll To Today")
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {onAddFabClick()}) {
                SizedIcon(iconDrawable = ComposableConstants.addIcon)
            }
        }
    )
}