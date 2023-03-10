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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.data.entity.ApptLocation
import com.example.movemedicalscheduleapp.extensions.getLocationDeclarationString
import com.example.movemedicalscheduleapp.extensions.getTimeSpanString
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun AppointmentCard(
    modifier: Modifier = Modifier,
    appointment: Appointment,
    onEditAppointment: (Appointment) -> Unit ,
    onCancelAppointment: (Appointment) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
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
                    onClick = { expanded = !expanded }
                ),
//            shape = MaterialTheme.shapes.medium,
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
                        text = "Description: ".plus(appointment.description)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TextButton(
                            elevation = ButtonDefaults.elevatedButtonElevation(),
                            onClick = { onCancelAppointment(appointment) }) {
                            Text(
                                text = "Cancel",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                        TextButton(
                            elevation = ButtonDefaults.elevatedButtonElevation(),
                            onClick = { onEditAppointment(appointment) }) {
                            Text(
                                text = "Update",
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

@Preview
@Composable
fun AppointmentCardPreview() {
    AppointmentCard(
        modifier = Modifier,
        appointment = Appointment(
            title = "TEST TITLE",
            location = ApptLocation.MEMPHIS,
            datetime = LocalDateTime.now(),
            duration = Duration.ofMinutes(45L),
            description = "THIS IS A LONG DESC \nTHIS IS A LONG DESC \nTHIS IS A LONG DESC \nTHIS IS A LONG DESC \n"
        ),
        onEditAppointment = {},
        onCancelAppointment = {}
    )
}
