package ru.kiryanov.locationtracker.presentation

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import vlnny.base.activity.BaseActivity
import javax.inject.Inject
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_location_tracker.*
import ru.kiryanov.locationtracker.LocationTrackerApp
import ru.kiryanov.locationtracker.R
import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.presentation.list.LocationHistoryAdapter
import vlnny.base.ext.hideActionBar
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
        viewModel.getLocations()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.locations.removeObserver(locationsObserver)
    }

    private fun initViews() {
        updateLocationInfo.setOnClickListener { loadLocation() }
        locationsList.adapter = adapter
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
}