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
// TODO: ("
//  1. К Бд прикрутить Flow
//  2. Более видный чек бокс
//  3. Преф менеджер мало-мальский
//  4. Вынести строки в ресурсы
//  5. Дименсы в ресурсы
//  6. Вынести create алерта?
//  7. Пройтись по LocationTracker
//  ")
class LocationTrackerActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: LocationTrackerViewModel

    @Inject
    lateinit var permissionManager: PermissionsManager

    private val adapter by lazy { LocationHistoryAdapter() }

    private val prefs by lazy { getSharedPreferences(PREFS, Context.MODE_PRIVATE) }

    private val locationsObserver by lazy {
        Observer<List<DomainLocation>> { locations ->
            adapter.updateList(locations)
        }
    }

    private var locationService: LocationService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.service
        }

        override fun onServiceDisconnected(name: ComponentName) {
            locationService = null
        }
    }

    override fun layoutId() = R.layout.activity_location_tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        hideActionBar()
        super.onCreate(savedInstanceState)
        LocationTrackerApp.appComponent.inject(this)

        checkLocationPermissions()
        bindLocationService()
    }

    override fun onStart() {
        super.onStart()
        initViews()

        with(viewModel) {
            locations.observe(this@LocationTrackerActivity, locationsObserver)
            getLocations()
        }
    }

    override fun onStop() {
        super.onStop()
        locationService?.unbindService(serviceConnection)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.locations.removeObserver(locationsObserver)
    }

    private fun initViews() {
        updateLocationInfo.setOnClickListener { viewModel.getLocations() }
        locationsTracking.isChecked = isServiceActive()
        locationsTracking.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                locationService?.subscribeLocationUpdates()
                updateIsSubscribedFlag(true)
            } else {
                locationService?.unsubscribeLocationUpdates()
                updateIsSubscribedFlag(false)
            }
        }
        locationsList.adapter = adapter
    }

    private fun checkLocationPermissions() {
        with(permissionManager) {
            if (!isLocationPermissionsGranted(this@LocationTrackerActivity)) {
                createAlertDialog { _, _ ->
                    checkLocationPermissions(this@LocationTrackerActivity)
                    if (isLocationPermissionsGranted(this@LocationTrackerActivity)) {
                        showSnack(headTextView, "Permissions now is granted")
                    }
                }.show()
            }
        }
    }

    private fun bindLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun updateIsSubscribedFlag(isSubscribed: Boolean) {
        prefs
            .edit()
            .putBoolean(IS_SUBSCRIBED, isSubscribed)
            .apply()
    }

    private fun isServiceActive() = prefs.getBoolean(IS_SUBSCRIBED, false)

    private fun createAlertDialog(action: DialogInterface.OnClickListener) =
        AlertDialog.Builder(this)
            .setTitle("Использование локации")
            .setMessage("Для использования приложения мы обязаны следить за вашим местоположением. \nОдобрите использование местоположения.")
            .setPositiveButton("ОК", action)
            .setCancelable(false)
            .create()

    companion object {
        private const val PREFS = "ru.kiryanov.prefs"

        private const val IS_SUBSCRIBED = "ru.kiryanov.is.subscribed"
    }
}