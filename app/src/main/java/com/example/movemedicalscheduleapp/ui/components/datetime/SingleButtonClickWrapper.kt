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