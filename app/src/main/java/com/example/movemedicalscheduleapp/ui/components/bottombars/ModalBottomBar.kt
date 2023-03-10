package com.example.movemedicalscheduleapp.ui.components.bottombars

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.movemedicalscheduleapp.ui.ComposableConstants

@Composable
fun ModalBottomBar(
    actionText: String,
    onCancelButtonClick: () -> Unit,
    onAddButtonClick: () -> Unit
) {
    //Perform Cancel operation on back gesture
    BackHandler{ onCancelButtonClick() }

    BottomAppBar(
        modifier = Modifier.height(ComposableConstants.defaultNavigationBarHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            TextButton(
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = { onCancelButtonClick() }) {
                Text(
                    text = "Cancel",
                    maxLines = 1,
                    softWrap = false,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,

                )
            }
            TextButton(
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = { onAddButtonClick() }) {
                Text(
                    text = actionText,
                    maxLines = 1,
                    softWrap = false,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}