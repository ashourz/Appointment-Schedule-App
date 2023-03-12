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

package com.example.movemedicalscheduleapp.ui.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.extensions.getLocationDeclarationString
import com.example.movemedicalscheduleapp.extensions.getTimeSpanString

@Composable
fun AppointmentCard(
    modifier: Modifier = Modifier,
    appointment: Appointment,
    expanded: Boolean,
    onExpandAppointmentCard: () -> Unit,
    onUpdateAppointment: (Appointment) -> Unit,
    onDeleteAppointment: (Appointment) -> Unit
) {
    val localContext = LocalContext.current
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = true,
                    role = Role.Button,
                    onClick = { onExpandAppointmentCard() }
                ).semantics {
                    contentDescription = localContext.getString(R.string.elevated_card)
                },
            elevation = CardDefaults.elevatedCardElevation()
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = appointment.title
                )
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    text = appointment.getLocationDeclarationString(localContext)
                )
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    text = appointment.getTimeSpanString(localContext)
                )
            }
        }
        this.AnimatedVisibility(visible = expanded, enter = expandVertically(), exit = shrinkVertically()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(0.dp),
                    topEnd = CornerSize(0.dp)
                ),
                elevation = CardDefaults.outlinedCardElevation()
            ) {

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = stringResource(R.string.description_prefix).plus(appointment.description)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TextButton(
                            elevation = ButtonDefaults.elevatedButtonElevation(),
                            onClick = { onDeleteAppointment(appointment) }) {
                            Text(
                                text = stringResource(id = R.string.delete_text),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                        TextButton(
                            elevation = ButtonDefaults.elevatedButtonElevation(),
                            onClick = { onUpdateAppointment(appointment) }) {
                            Text(
                                text = stringResource(R.string.update_text),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

