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