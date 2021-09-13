package ru.kiryanov.locationtracker.presentation

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.activity_location_tracker.*
import ru.kiryanov.locationtracker.LocationTrackerApp
import ru.kiryanov.locationtracker.R
import ru.kiryanov.locationtracker.presentation.list.LocationHistoryAdapter
import vlnny.base.activity.BaseActivity
import vlnny.base.ext.hideActionBar
import vlnny.base.ext.showSnack
import vlnny.base.permissions.PermissionsManager
import javax.inject.Inject

class LocationTrackerActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_location_tracker

    @Inject
    lateinit var viewModel: LocationTrackerViewModel

    @Inject
    lateinit var permissionManager: PermissionsManager

    @Inject
    lateinit var adapter: LocationHistoryAdapter

    private val permissionAlertAction by lazy {
        DialogInterface.OnClickListener { _, _ ->
            permissionManager.requestLocationPermissions(this)
            if (permissionManager.isLocationPermissionsGranted(this)) {
                showSnack(headTextView, getString(R.string.message_now_permission_granted))
            }
        }
    }

    private val locationTrackingCheckedChangeListener by lazy {
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            viewModel.locationTrackingClicked(isChecked)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocationTrackerApp.appComponent.inject(this)
        checkLocationPermissions()

        viewModel.initLocationTracker(this)
    }

    override fun onStart() {
        super.onStart()
        initViews()

        viewModel.locations.observe(this) { locations ->
            adapter.updateList(locations)
        }
    }

    private fun initViews() {
        locationsTracking.isChecked = viewModel.isLocationUpdatingNow()
        locationsTracking.setOnCheckedChangeListener(locationTrackingCheckedChangeListener)
        locationsList.adapter = adapter
    }

    private fun checkLocationPermissions() {
        if (!permissionManager.isLocationPermissionsGranted(this)) {
            createAlertDialog(permissionAlertAction).show()
        }
    }

    private fun createAlertDialog(action: DialogInterface.OnClickListener) =
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.title_location_usage))
            .setMessage(getString(R.string.permission_alert_message))
            .setPositiveButton(getString(R.string.button_ok), action)
            .setCancelable(false)
            .create()
}