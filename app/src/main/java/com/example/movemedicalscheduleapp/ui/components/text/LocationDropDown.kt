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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.ui.ComposableConstants
import com.example.movemedicalscheduleapp.ui.components.icons.SizedIcon

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationDropDown(
    modifier: Modifier = Modifier,
    label: String,
    selectedLocation: ApptLocation? = null,
    onLocationSelected: (ApptLocation) -> Unit = {},
    errorString: String? = null,
) {
    val localContext = LocalContext.current

    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = expanded) {
        if (expanded) focusRequester.requestFocus()
    }

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
                        text = label
                    )
                },
                placeholder = {
                    Text(
                        fontStyle = FontStyle.Italic,
                        text = "Select Appointment Location"
                    )
                },
                leadingIcon = {
                    SizedIcon(
                        iconDrawable = ComposableConstants.locationIcon,
                        contentDescription = "Select Appointment Location",
                    )
                },
                trailingIcon = {
                    SizedIcon(
                        iconDrawable = if (expanded) {
                            ComposableConstants.upArrowIcon
                        } else {
                            ComposableConstants.downArrowIcon
                        },
                        contentDescription = "Select Appointment Location",
                    )
                },
                isError = (errorString != null),
            )
            DropdownMenu(
                modifier = modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { keyboardController?.hide() }
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
                        modifier = modifier.fillMaxWidth(),
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
