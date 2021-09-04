package ru.kiryanov.locationtracker.utils.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import java.lang.Exception
import javax.inject.Inject

class LocationTrackerImpl @Inject constructor(private val context: Context) : LocationTracker {

    companion object {
        /*
        * This value should changes depends on device power
        * */
        private const val REQUEST_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val locationRequest by lazy {
        LocationRequest.create()
            .apply {
                interval = LocationTracker.LOCATION_UPDATES_INTERVAL
                fastestInterval = LocationTracker.LOCATION_UPDATES_INTERVAL / 4
                priority = REQUEST_PRIORITY
            }
    }

    override fun setLocationListener(listener: (LocationResult) -> Unit) {
        val locationSettingsRequest = createLocationSettingsRequest()

        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(locationSettingsRequest)

        task.continueWith { _task -> taskContinueWith(_task, listener) }
        task.addOnFailureListener { exception -> taskOnFailureAction(exception, listener) }
    }

    @SuppressLint("MissingPermission")
    private fun taskContinueWith(
        task: Task<LocationSettingsResponse>,
        listener: (LocationResult) -> Unit
    ) {
        if (task.isSuccessful) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                listener.invoke(
                    if (location == null) {
                        LocationResult.Failure(error = LocationError.Unknown)
                    } else {
                        LocationResult.Success(location = location)
                    }
                )
            }
        }
    }

    private fun taskOnFailureAction(
        exception: Exception,
        listener: (LocationResult) -> Unit
    ) {
        Log.e("LocationTrackerImpl: ", exception.message.orEmpty())
        listener.invoke(
            LocationResult.Failure(error = LocationError.Unknown)
        )
    }

    private fun createLocationSettingsRequest() =
        LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
            .build()
}