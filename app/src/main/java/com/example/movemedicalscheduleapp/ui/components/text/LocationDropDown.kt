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

package com.example.movemedicalscheduleapp.ui.components.text

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.ui.ComposableConstants
import com.example.movemedicalscheduleapp.ui.components.icons.SizedIcon

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationDropDown(
    modifier: Modifier = Modifier,
    selectedLocation: ApptLocation? = null,
    onLocationSelected: (ApptLocation) -> Unit,
    errorString: String? = null,
) {
    val localContext = LocalContext.current

    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }
    //Update focus when AppointmentCard is expanded
//    LaunchedEffect(key1 = expanded) {
//        if (expanded) focusRequester.requestFocus()
//    }

    Column() {
        ExposedDropdownMenuBox(
            modifier = modifier.width(IntrinsicSize.Max),
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            TextField(
                modifier = modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedLocation?.let { selectedLocation.getDisplayName(localContext) } ?: "",
                onValueChange = {},
                label = {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        text = stringResource(R.string.appointment_location),

                        )
                },
                placeholder = {
                    Text(
                        fontStyle = FontStyle.Italic,
                        text = stringResource(R.string.select_appointment_location)
                    )
                },
                leadingIcon = {
                    SizedIcon(
                        iconDrawable = ComposableConstants.locationIcon,
                        contentDescription = stringResource(R.string.select_appointment_location),
                    )
                },
                trailingIcon = {
                    SizedIcon(
                        iconDrawable = if (expanded) {
                            ComposableConstants.upArrowIcon
                        } else {
                            ComposableConstants.downArrowIcon
                        },
                        contentDescription = stringResource(R.string.dropdown_arrow),
                    )
                },
                isError = (errorString != null),
            )
            DropdownMenu(
                modifier = modifier
                    .exposedDropdownSize(),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),

                    ) {

                }
                ApptLocation.values().filter { it != ApptLocation.UNKNOWN }.sortedBy { it.getDisplayName(localContext) }.forEach { location ->
                    DropdownMenuItem(
                        modifier = modifier.fillMaxWidth().semantics { contentDescription = localContext.getString(R.string.dropdown_menu_item) },
                        text = { Text(modifier = Modifier.padding(start = ComposableConstants.defaultIconSize), text = location.getDisplayName(localContext)) },
                        enabled = true,
                        onClick = {
                            onLocationSelected(location)
                            expanded = false
                        }
                    )
                }
            }
        }
        if(errorString != null) {
            ErrorText(errorText = errorString)
        }
    }
}

