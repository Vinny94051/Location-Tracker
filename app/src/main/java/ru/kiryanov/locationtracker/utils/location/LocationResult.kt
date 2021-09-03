package ru.kiryanov.locationtracker.utils.location

import android.location.Location

sealed class LocationResult {
    data class Success(val location : Location) : LocationResult()
    data class Failure(val error : LocationError) : LocationResult()
}

sealed class LocationError {
    object NoConnection : LocationError()
    object NoPermission : LocationError()
    object Unknown : LocationError()
}