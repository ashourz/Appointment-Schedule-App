package com.example.movemedicalscheduleapp.ui.components.bottombars

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.R
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
                SizedIcon(
                    iconDrawable = ComposableConstants.todayIcon,
                    contentDescription = stringResource(R.string.scroll_to_today))
                Text(text = stringResource(R.string.today_text),
                    maxLines = 1,
                    softWrap = false,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.semantics(mergeDescendants = true, properties = {}),
                onClick = {onAddFabClick()}) {
                SizedIcon(
                    iconDrawable = ComposableConstants.addIcon,
                    contentDescription = stringResource(R.string.add_appointment))
            }
        }
    )
}