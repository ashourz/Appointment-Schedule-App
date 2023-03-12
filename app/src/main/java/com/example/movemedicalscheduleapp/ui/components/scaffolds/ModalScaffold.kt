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

