package ru.kiryanov.locationtracker.utils.location

interface LocationTracker {
    companion object {
        const val LOCATION_UPDATES_INTERVAL = 1000L
    }

    fun setLocationListener(listener: ((LocationResult) -> Unit))
}