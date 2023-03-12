package com.example.movemedicalscheduleapp.ui.popups

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.movemedicalscheduleapp.R
import com.example.movemedicalscheduleapp.data.entity.Appointment
import com.example.movemedicalscheduleapp.view_model.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

@Composable
fun ConfirmCancelPopup(
    appointment: Appointment,
    onCancelAppointment: (Appointment) -> Unit,
    onClose: () -> Unit,
) {

    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.confirm_cancel_appointment)
            )
        },

        text = {
            Text(
                text = stringResource(R.string.confirm_cancel_appointment_prefix).plus(appointment.title).plus("\n").plus(stringResource(R.string.this_action_cannot_be_undone))
            )
        },
        dismissButton = {
            TextButton(
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = { onClose() }) {
                Text(
                    text = stringResource(R.string.keep_appointment)
                )
            }
        },
        confirmButton = {
            TextButton(
                elevation = ButtonDefaults.elevatedButtonElevation(),
                onClick = {
                    onCancelAppointment(appointment)
                    onClose()
                }) {
                Text(
                    text = stringResource(R.string.cancel_appointment),
                )
            }
        },
        onDismissRequest = { onClose() })
}
