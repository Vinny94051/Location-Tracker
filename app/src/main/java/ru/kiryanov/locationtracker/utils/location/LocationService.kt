package ru.kiryanov.locationtracker.utils.location

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.kiryanov.locationtracker.LocationTrackerApp
import ru.kiryanov.locationtracker.R
import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.domain.usecase.SaveLocationUseCase
import ru.kiryanov.locationtracker.presentation.LocationTrackerActivity
import ru.kiryanov.locationtracker.presentation.toDomainLocation
import ru.kiryanov.locationtracker.utils.toText
import javax.inject.Inject

class LocationService : Service() {

    private val job by lazy { SupervisorJob() }
    private val scope by lazy { CoroutineScope(Dispatchers.Main + job) }

    private val locationTracker: LocationTracker by lazy {
        LocationTrackerImpl(this)
    }

    @Inject
    lateinit var saveLocationUseCase: SaveLocationUseCase

    private val localBinder = LocalBinder()

    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val onLocationListener: ((LocationResult) -> Unit) by lazy {
        { locationResult ->
            scope.launch {
                locationResult.toDomainLocation()?.let { saveLocationUseCase.saveLocation(it) }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        LocationTrackerApp.appComponent.inject(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Log.d(TAG, "onStartCommand()")

        val cancelLocationTrackingFromNotification =
            intent.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)

        if (cancelLocationTrackingFromNotification) {
            locationTracker.stopLocationUpdates()
            stopSelf()
        }

        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent?): IBinder {
        stopForeground(true)
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        stopForeground(true)
        super.onRebind(intent)
    }
    private var currentLocation: DomainLocation? = null

    override fun onUnbind(intent: Intent?): Boolean {
        val notification = generateNotification(currentLocation)
        startForeground(NOTIFICATION_ID, notification)
        return true
    }



    fun subscribeLocationUpdates() {
        startService(Intent(applicationContext, LocationService::class.java))
        locationTracker.startLocationUpdates {
            it.toDomainLocation()?.let { dl ->
                currentLocation = dl

                notificationManager.notify(
                    NOTIFICATION_ID,
                    generateNotification(currentLocation)
                )

                scope.launch {
                    saveLocationUseCase.saveLocation(dl)
                }
            }
        }
    }

    fun unsubscribeLocationUpdates() {
        locationTracker.stopLocationUpdates()
    }

    inner class LocalBinder : Binder() {
        val service: LocationService
            get() = this@LocationService
    }

    /*
    * Generates a BIG_TEXT_STYLE Notification that represent latest location.
    */
    private fun generateNotification(location: DomainLocation?): Notification {
        Log.d(TAG, "generateNotification()")

        // Main steps for building a BIG_TEXT_STYLE notification:
        //      0. Get data
        //      1. Create Notification Channel for O+
        //      2. Build the BIG_TEXT_STYLE
        //      3. Set up Intent / Pending Intent for notification
        //      4. Build and issue the notification

        // 0. Get data
        val mainNotificationText = location?.location?.toText() ?: getString(R.string.no_location_text)
        val titleText = location?.date.toString() //getString(R.string.app_name)

        // 1. Create Notification Channel for O+ and beyond devices (26+).
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, titleText, NotificationManager.IMPORTANCE_DEFAULT
            )

            // Adds NotificationChannel to system. Attempting to create an
            // existing notification channel with its original values performs
            // no operation, so it's safe to perform the below sequence.
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // 2. Build the BIG_TEXT_STYLE.
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(mainNotificationText)
            .setBigContentTitle(titleText)

        // 3. Set up main Intent/Pending Intents for notification.
        val launchActivityIntent = Intent(this, LocationTrackerActivity::class.java)

        val cancelIntent = Intent(this, LocationService::class.java)
        cancelIntent.putExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, true)

        val servicePendingIntent = PendingIntent.getService(
            this, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val activityPendingIntent = PendingIntent.getActivity(
            this, 0, launchActivityIntent, 0
        )

        // 4. Build and issue the notification.
        // Notification Channel Id is ignored for Android pre O (26).
        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentText(mainNotificationText)
            .setSmallIcon(R.drawable.ic_cancel)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.mipmap.ic_launcher, getString(R.string.launch_activity),
                activityPendingIntent
            )
            .addAction(
                R.drawable.ic_cancel,
                getString(R.string.stop_location_updates_button_text),
                servicePendingIntent
            )
            .build()
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"

        private const val TAG = "LocationService"

        private const val PACKAGE_NAME = "ru.kiryanov.locationtracker"

        private const val NOTIFICATION_ID = 12345678

        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"
    }
}