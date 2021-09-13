package ru.kiryanov.locationtracker.utils.location

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.kiryanov.locationtracker.LocationTrackerApp
import ru.kiryanov.locationtracker.R
import ru.kiryanov.locationtracker.domain.location.LocationTracker
import ru.kiryanov.locationtracker.domain.model.DomainLocation
import ru.kiryanov.locationtracker.domain.usecase.SaveLocationUseCase
import ru.kiryanov.locationtracker.presentation.LocationTrackerActivity
import ru.kiryanov.locationtracker.presentation.toDomainLocation
import ru.kiryanov.locationtracker.utils.di.MainCoroutineScope
import ru.kiryanov.locationtracker.utils.preference.SharedPreferencesManager
import ru.kiryanov.locationtracker.utils.toText
import java.util.*
import javax.inject.Inject

class LocationService : Service() {

    @Inject
    lateinit var saveLocationUseCase: SaveLocationUseCase

    @Inject
    lateinit var prefs: SharedPreferencesManager

    @Inject
    @MainCoroutineScope
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var notificationManager: NotificationManager

    private var currentLocation: DomainLocation? = null
        set(value) {
            field = value
            if (isInForeground) {
                notificationManager.notify(NOTIFICATION_ID, generateNotification(value))
            }
        }

    private var locationTrackerJob: Job? = null
    private var isInForeground: Boolean = false
    private val localBinder = LocalBinder()

    override fun onCreate() {
        super.onCreate()
        LocationTrackerApp.appComponent.inject(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(STOP_TRACKER_FROM_NOTIFY, false)

        if (cancelLocationTrackingFromNotification) {
            locationTrackerJob?.cancel()
            updateIsSubscribedFlag(false)
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        stopForegroundAndUpdateFlag()
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        stopForegroundAndUpdateFlag()
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        startForegroundAndUpdateFlag()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    fun subscribeLocationUpdates(locationTracker: LocationTracker) {
        startService(Intent(applicationContext, LocationService::class.java))
        locationTrackerJob = createLocationTrackerJob(locationTracker)
        updateIsSubscribedFlag(true)
    }

    fun unsubscribeLocationUpdates() {
        locationTrackerJob?.cancel()
        updateIsSubscribedFlag(false)
        stopSelf()
    }

    private fun startForegroundAndUpdateFlag() {
        startForeground(NOTIFICATION_ID, generateNotification(currentLocation))
        isInForeground = true
    }

    private fun stopForegroundAndUpdateFlag() {
        stopForeground(true)
        isInForeground = false
    }

    private fun createLocationTrackerJob(locationTracker: LocationTracker): Job {
        return locationTracker.locationUpdates
            .map { it.toDomainLocation() }
            .onEach { domainLocation ->
                domainLocation?.let { location ->
                    currentLocation = location
                    saveLocationUseCase.saveLocation(location)
                }
            }.launchIn(scope)
    }

    private fun updateIsSubscribedFlag(isSubscribed: Boolean) {
        prefs.save(IS_SUBSCRIBED, isSubscribed)
    }

    private fun generateNotification(location: DomainLocation?): Notification {
        Log.d(TAG, "generateNotification()")

        val notificationText = location?.location?.toText() ?: getString(R.string.no_location_text)
        val titleText = location?.date ?: getString(R.string.no_date_text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(notificationText)
            .setBigContentTitle(titleText)

        val launchActivityIntent = Intent(this, LocationTrackerActivity::class.java)

        val cancelIntent = Intent(this, LocationService::class.java)
            .apply {
                putExtra(STOP_TRACKER_FROM_NOTIFY, true)
            }

        val servicePendingIntent = PendingIntent.getService(
            this,
            0,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val activityPendingIntent = PendingIntent.getActivity(
            this,
            0,
            launchActivityIntent,
            0
        )

        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.ic_cancel)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.mipmap.ic_launcher,
                getString(R.string.launch_activity),
                activityPendingIntent
            )
            .addAction(
                R.drawable.ic_cancel,
                getString(R.string.stop_location_updates_button_text),
                servicePendingIntent
            )
            .build()
    }

    inner class LocalBinder : Binder() {
        val service: LocationService
            get() = this@LocationService
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "location_tracker_channel_01"

        private const val TAG = "LocationService"

        private const val PACKAGE_NAME = "ru.kiryanov.locationtracker"

        private const val NOTIFICATION_ID = 12345678

        const val IS_SUBSCRIBED = "ru.kiryanov.is.subscribed"

        private const val STOP_TRACKER_FROM_NOTIFY =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"
    }
}