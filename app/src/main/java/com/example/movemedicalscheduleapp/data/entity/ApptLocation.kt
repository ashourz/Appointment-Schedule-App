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

package com.example.movemedicalscheduleapp.data.entity

import android.content.Context
import com.example.movemedicalscheduleapp.R

enum class ApptLocation(
    private val displayName: Int,
    val zipCode: Int) {
    SAN_DIEGO(
        displayName = R.string.appt_location_san_diego,
        zipCode =  91911
    ),
    ST_GEORGE(
        displayName = R.string.appt_location_st_george,
        zipCode = 84765
    ),
    PARK_CITY(
        displayName = R.string.appt_location_park_city,
        zipCode = 84060
    ),
    DALLAS(
        displayName = R.string.appt_location_dallas,
        zipCode = 75001
    ),
    MEMPHIS(
        displayName = R.string.appt_location_memphis,
        zipCode = 37501
    ),
    ORLANDO(
        displayName = R.string.appt_location_orlando,
        zipCode = 32789
    ),
    UNKNOWN(
        displayName = R.string.appt_location_unknown,
        zipCode = -1
    );

    companion object {
        fun getLocationByZipCode(zipCode: Int): ApptLocation {
            return ApptLocation.values().firstOrNull {
                it.zipCode == zipCode
            }?: UNKNOWN
        }
    }

    fun getDisplayName(context: Context) =
        context.getString(displayName)
}