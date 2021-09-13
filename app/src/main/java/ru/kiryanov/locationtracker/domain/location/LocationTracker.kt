package ru.kiryanov.locationtracker.domain.location

import android.content.Context
import kotlinx.coroutines.flow.SharedFlow
import ru.kiryanov.locationtracker.utils.location.LocationResult

interface LocationTracker {
    val locationUpdates: SharedFlow<LocationResult>

    fun initLocationTracker(context: Context)

    suspend fun startLocationUpdates()

    fun stopLocationUpdates()
}