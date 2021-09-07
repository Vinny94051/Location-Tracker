package ru.kiryanov.locationtracker.utils

import android.location.Location

fun Location?.toText() : String? {
   return this?.let { loc ->
       "Latitude: ${loc.latitude}\nLongitude: ${loc.longitude}"
   }
}