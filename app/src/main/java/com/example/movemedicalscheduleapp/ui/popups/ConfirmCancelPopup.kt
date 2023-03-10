package com.example.movemedicalscheduleapp.ui.popups

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@Composable
fun ConfirmCancelPopup(
    dataViewModel: DataViewModel,
    appointment: Appointment,
    onClose: () -> Unit,
) {
    val coroutineScopeIO = rememberCoroutineScope().plus(Dispatchers.IO)

    AlertDialog(
        title = {
            Text(
                text = "Confirm Cancel Appointment"
            )
        },
        text = {
            Text(
                text = "Confirm Cancel Appointment: ${appointment.title}.\nThis Action Cannot Be Undone."
            )
        },
        dismissButton = {
            TextButton(
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = { onClose() }) {
                Text(
                    text = "Keep Appointment",
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.labelMedium,
                )
            }
        },
        confirmButton = {
            TextButton(
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = {
                    coroutineScopeIO.launch {
                        dataViewModel.deleteAppointment(appointment)
                    }
                    onClose()
                }) {
                Text(
                    text = "Cancel Appointment",
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.labelMedium,
                )
            }
        },
        onDismissRequest = { onClose() })
}
