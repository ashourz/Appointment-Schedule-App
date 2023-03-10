package com.example.movemedicalscheduleapp.ui.components.toolbars

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.movemedicalscheduleapp.ui.ComposableConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopBar(
    title: String
) {
    TopAppBar(
        modifier = Modifier.height(ComposableConstants.defaultNavigationBarHeight),
        navigationIcon = {},
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    softWrap = false,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    )
}