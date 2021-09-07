package ru.kiryanov.locationtracker.utils.location

interface LocationTracker {
    companion object {
        const val LOCATION_UPDATES_INTERVAL = 10000L
    }

    fun setLocationListener(listener: ((LocationResult) -> Unit))

    fun startLocationUpdates(listener: (LocationResult) -> Unit)

    fun stopLocationUpdates()
}