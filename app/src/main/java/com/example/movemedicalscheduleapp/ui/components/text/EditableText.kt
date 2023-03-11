package com.example.movemedicalscheduleapp.ui.components.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.movemedicalscheduleapp.ui.components.icons.SizedIcon

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditableTextField(
    modifier: Modifier = Modifier,
    textValue: String?,
    label: String,
    placeholder: String,
    maxLines: Int = Int.MAX_VALUE,
    leadingIconDrawable: Int,
    leadingIconContentDescription: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit = {},
    errorString: String? = null
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    Column() {
        TextField(
            modifier = modifier,
            value = textValue ?: "",
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
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
                    text = placeholder
                )
            },
            maxLines =  maxLines,
            leadingIcon = {
                SizedIcon(
                    iconDrawable = leadingIconDrawable,
                    contentDescription = leadingIconContentDescription,
                )
            },
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
            ),
            isError = (errorString != null)
        )
        if(errorString!= null){
            ErrorText(errorText = errorString)
        }
    }
}