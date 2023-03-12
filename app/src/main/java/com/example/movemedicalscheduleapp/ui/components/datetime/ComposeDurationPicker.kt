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

package com.example.movemedicalscheduleapp.ui.components.datetime

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.fragment.app.FragmentManager
import com.example.movemedicalscheduleapp.extensions.toDisplayFormat
import com.example.movemedicalscheduleapp.ui.components.icons.SizedIcon
import com.example.movemedicalscheduleapp.ui.components.text.ErrorText
import java.time.Duration
import java.time.LocalDateTime

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ComposeDurationPicker(
    fragmentManager: FragmentManager,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    leadingIconDrawable: Int,
    leadingIconContentDescription: String,
    selectedDuration: Duration? = null,
    onDurationSelected: (duration: Duration) -> Unit,
    errorString: String?,
) {
    val localContext = LocalContext.current
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    var lastButtonClickEvent by remember { mutableStateOf<LocalDateTime?>(null) }

    Column() {
        Box() {
            TextField(
                modifier = modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { keyboardController?.hide() }
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedDuration?.let { selectedDuration.toDisplayFormat() } ?: "",
                onValueChange = {},
                label = {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        text = label
                    )
                },
                placeholder = { Text(placeholder) },
                leadingIcon = {
                    SizedIcon(
                        iconDrawable = leadingIconDrawable,
                        contentDescription = leadingIconContentDescription,
                    )
                },
                isError = (errorString != null),
            )
            Surface(modifier = modifier
                .background(androidx.compose.ui.graphics.Color.Transparent, TextFieldDefaults.filledShape)
                .alpha(0f)
                .defaultMinSize(
                    minHeight = TextFieldDefaults.MinHeight
                )
                .fillMaxWidth()
                .clickable(
                    enabled = true,
                    onClick = {
                        singleButtonClickWrapper(
                            lastButtonClick = lastButtonClickEvent,
                            updateLastButtonClick = { eventLocaDateTime -> lastButtonClickEvent = eventLocaDateTime },
                            onButtonClick = {
                                focusRequester.requestFocus()
                                showDurationPicker(
                                    context = localContext,
                                    fragmentManager = fragmentManager,
                                    title = label,
                                    openDuration = selectedDuration ?: Duration.ZERO,
                                    onPositiveButtonClick = onDurationSelected
                                )
                            }
                        )
                    }
                )
            ) {}
        }
        if(errorString != null) {
            ErrorText(errorText = errorString)
        }
    }
}

