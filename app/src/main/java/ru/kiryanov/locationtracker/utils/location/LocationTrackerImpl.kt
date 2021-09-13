package ru.kiryanov.locationtracker.utils.location

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import com.google.android.gms.location.LocationResult as GoogleLocationResult
import ru.kiryanov.locationtracker.domain.location.LocationTracker
import ru.kiryanov.locationtracker.utils.toLocationResult
import java.lang.IllegalStateException
import java.lang.NullPointerException
import javax.inject.Inject

class LocationTrackerImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationTracker {

    private val _locationUpdates = MutableSharedFlow<LocationResult>()
    override val locationUpdates: SharedFlow<LocationResult> =
        _locationUpdates.asSharedFlow()

    private var locationCallback: LocationCallback? = null
    private var locationService: LocationService? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    private val locationUpdatesFlow: Flow<LocationResult> =
        callbackFlow {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(location: GoogleLocationResult) {
                    trySend(location.toLocationResult())
                }
            }.apply { requestLocationUpdates(this) }

            awaitClose { stopLocationUpdates() }
        }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder

            locationService = binder.service
            locationService?.subscribeLocationUpdates(this@LocationTrackerImpl)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            locationService?.unsubscribeLocationUpdates()
            locationService = null
        }
    }

    override suspend fun startLocationUpdates() {
        if (locationService == null) {
            throw NullPointerException("Firstly you must init location tracker with initLocationTracker method.")
        } else {
            locationUpdatesFlow.collect { _locationUpdates.emit(it) }
        }
    }

    override fun stopLocationUpdates() {
        locationService?.unsubscribeLocationUpdates()
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }

    override fun initLocationTracker(context: Context) {
        val serviceIntent = Intent(context, LocationService::class.java)
        context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(onLocationResultCallback: LocationCallback) {
        val locationRequest = LocationRequest.create()
            .apply {
                interval = LOCATION_UPDATES_INTERVAL
                fastestInterval = LOCATION_UPDATES_INTERVAL / 4
                priority = REQUEST_PRIORITY
            }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            onLocationResultCallback,
            Looper.getMainLooper()
        )
    }

    companion object {
        private const val REQUEST_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY

        private const val LOCATION_UPDATES_INTERVAL = 10000L
    }
}