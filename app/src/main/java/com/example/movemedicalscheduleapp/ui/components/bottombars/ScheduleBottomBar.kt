package com.example.movemedicalscheduleapp.ui.components.bottombars

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
                modifier = Modifier.padding(12.dp),
                elevation = ComposableConstants.fabButtonElevation(),
                onClick = {onScrollToToday() }) {
                SizedIcon(iconDrawable = ComposableConstants.todayIcon)
                Text(text = "Today",
                    maxLines = 1,
                    softWrap = false,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {onAddFabClick()}) {
                SizedIcon(iconDrawable = ComposableConstants.addIcon)
            }
        }
    )
}