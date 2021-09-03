package ru.kiryanov.locationtracker.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import ru.kiryanov.locationtracker.utils.location.LocationResult
import vlnny.base.activity.BaseActivity
import javax.inject.Inject
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_location_tracker.*
import ru.kiryanov.locationtracker.LocationTrackerApp
import ru.kiryanov.locationtracker.R
import vlnny.base.ext.showToast
import vlnny.base.permissions.PermissionsManager

class LocationTrackerActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: LocationTrackerViewModel

    @Inject
    lateinit var permissionManager: PermissionsManager

    private val locationObserver by lazy {
        Observer<LocationResult> { locationResult ->
            when (locationResult) {
                is LocationResult.Success -> showLocation(locationResult.location)
                is LocationResult.Failure -> showToast(locationResult.error.toString())
            }
        }
    }

    override fun layoutId() = R.layout.activity_location_tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationTrackerApp.appComponent.inject(this)
        loadLocation()
    }

    override fun onStart() {
        super.onStart()
        viewModel.location.observe(this, locationObserver)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.location.removeObserver(locationObserver)
    }

    private fun initViews() {
        updateLocationInfo.setOnClickListener { loadLocation() }
    }

    private fun loadLocation() {
        with(permissionManager) {
            if (!isLocationPermissionsGranted(this@LocationTrackerActivity)) {
                createAlertDialog { _, _ ->
                    checkLocationPermissions(this@LocationTrackerActivity)
                    if (isLocationPermissionsGranted(this@LocationTrackerActivity)) {
                        viewModel.initLocationTracker()
                    }
                }.show()
            } else {
                viewModel.initLocationTracker()
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

    private fun showLocation(location: Location) {
        val locationText = "latitude: ${location.latitude}\nlongitude: ${location.longitude}"
        locationTextView.text = locationText
    }
}