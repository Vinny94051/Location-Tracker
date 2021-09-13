package ru.kiryanov.locationtracker.utils

import android.location.Location
import ru.kiryanov.locationtracker.utils.location.LocationResult
import com.google.android.gms.location.LocationResult as GoogleLocationResult

fun Location?.toText() : String? {
   return this?.let { loc ->
       "Latitude: ${loc.latitude}\nLongitude: ${loc.longitude}"
   }
}

fun GoogleLocationResult.toLocationResult(): LocationResult {
    return LocationResult.Success(location = lastLocation)
}