package com.example.movemedicalscheduleapp.ui.components.datetime

import java.time.LocalDateTime
import java.time.ZoneOffset

fun singleButtonClickWrapper(
    lastButtonClick: LocalDateTime?,
    updateLastButtonClick: (LocalDateTime) -> Unit,
    onButtonClick: () -> Unit
) {

    if (lastButtonClick != null &&
        (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) -
                lastButtonClick.toEpochSecond(ZoneOffset.UTC) < 2)
    ) {
        //DO NOTHING
    } else {
        updateLastButtonClick(LocalDateTime.now())
        onButtonClick()
    }
}