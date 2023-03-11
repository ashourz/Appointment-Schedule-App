package com.example.movemedicalscheduleapp.ui.components.toolbars

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopBar(
    title: String
) {
    TopAppBar(title = { Text(text = title,) })
}