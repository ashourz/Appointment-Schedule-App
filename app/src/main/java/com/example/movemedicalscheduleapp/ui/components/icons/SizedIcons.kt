package com.example.movemedicalscheduleapp.ui.components.icons

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.movemedicalscheduleapp.ui.ComposableConstants

@Composable
fun SizedIcon(
    modifier: Modifier = Modifier,
    iconDrawable: Int,
    contentDescription: String? = null,
) {
    Icon(
        modifier = modifier.size (ComposableConstants.defaultIconSize),
        painter = painterResource(id = iconDrawable),
        contentDescription = contentDescription,
    )
}