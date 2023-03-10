package com.example.movemedicalscheduleapp.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorText(errorText: String?) {
    errorText?.let { nnErrorText ->
        Text(
            text = nnErrorText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}