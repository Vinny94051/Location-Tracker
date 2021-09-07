package ru.kiryanov.locationtracker.presentation

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.os.IBinder
import vlnny.base.activity.BaseActivity
import javax.inject.Inject
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_location_tracker.*
import ru.kiryanov.locationtracker.LocationTrackerApp
import ru.kiryanov.locationtracker.R
import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.presentation.list.LocationHistoryAdapter
import ru.kiryanov.locationtracker.utils.location.LocationService
import vlnny.base.ext.hideActionBar
import vlnny.base.ext.showSnack
import vlnny.base.permissions.PermissionsManager

class LocationTrackerActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: LocationTrackerViewModel

    @Inject
    lateinit var permissionManager: PermissionsManager

    private val adapter by lazy { LocationHistoryAdapter() }

    private val locationsObserver by lazy {
        Observer<List<DomainLocation>> { locations ->
            adapter.updateList(locations)
        }
    }

    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var locationService: LocationService? = null

    // Monitors connection to the while-in-use service.
    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            locationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun layoutId() = R.layout.activity_location_tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        hideActionBar()
        super.onCreate(savedInstanceState)
        LocationTrackerApp.appComponent.inject(this)
        loadLocation()
    }

    override fun onStart() {
        super.onStart()
        initViews()
        viewModel.locations.observe(this, locationsObserver)
        val serviceIntent = Intent(this, LocationService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        locationService?.subscribeLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.locations.removeObserver(locationsObserver)
    }

    private fun initViews() {
        updateLocationInfo.setOnClickListener {
            loadLocation()
            viewModel.getLocations()
        }
        locationsList.adapter = adapter
    }

    private fun loadLocation() {
        with(permissionManager) {
            if (!isLocationPermissionsGranted(this@LocationTrackerActivity)) {
                createAlertDialog { _, _ ->
                    checkLocationPermissions(this@LocationTrackerActivity)
                    if (isLocationPermissionsGranted(this@LocationTrackerActivity)) {
                        showSnack(headTextView, "Permissions now is granted")
                    }
                }.show()
            } else {
                showSnack(headTextView, "Permissions was granted")
            }
        }
    }

    private fun createAlertDialog(action: DialogInterface.OnClickListener) =
        AlertDialog.Builder(this)
            .setTitle("Использование локации")
            .setMessage("Для использования приложения мы обязаны следить за вашим местоположением. \nОдобрите использование местоположения.")
            .setPositiveButton("ОК", action)
            .setCancelable(false)
            .create()
}