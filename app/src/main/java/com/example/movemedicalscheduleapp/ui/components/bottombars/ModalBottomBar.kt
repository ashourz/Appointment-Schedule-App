package com.example.movemedicalscheduleapp.ui.components.bottombars

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.ui.ComposableConstants

@Composable
fun ModalBottomBar(
    positiveActionText: String,
    negativeActionText: String = stringResource(R.string.cancel_text),
    onNegativeButtonClick: () -> Unit,
    onPositiveButtonClick: () -> Unit,
) {
    //Perform Cancel operation on back gesture
    BackHandler{ onNegativeButtonClick() }

    BottomAppBar(
//        modifier = Modifier.height(ComposableConstants.defaultNavigationBarHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            TextButton(
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = { onNegativeButtonClick() }) {
                Text(
                    text = negativeActionText,
                    maxLines = 1,
                    softWrap = false,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,

                )
            }
            TextButton(
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = { onPositiveButtonClick() }) {
                Text(
                    text = positiveActionText,
                    maxLines = 1,
                    softWrap = false,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    )
            }
        }
    }
}