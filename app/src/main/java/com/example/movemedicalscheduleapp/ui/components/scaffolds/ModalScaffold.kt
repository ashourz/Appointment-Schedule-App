package com.example.movemedicalscheduleapp.ui.components.scaffolds

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.ui.components.bottombars.ModalBottomBar
import com.example.movemedicalscheduleapp.ui.components.toolbars.BasicTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalScaffold(
    title: String,
    actionButtonText: String,
    dataValidation: () -> Boolean,
    overlapValidation: () -> Boolean,
    onActionButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (ColumnScope) -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost,
        topBar = { BasicTopBar(title) },
        bottomBar = {
            ModalBottomBar(
                positiveActionText = actionButtonText,
                onNegativeButtonClick = { onCancelButtonClick() },
                onPositiveButtonClick = {
                    if(dataValidation()) {
                        if(overlapValidation()) {
                            onActionButtonClick()
                        }
                    }}
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .scrollable(
                    state = rememberScrollState(),
                    orientation = Orientation.Vertical
                )
        ) {
            content(this)
        }
    }
}

