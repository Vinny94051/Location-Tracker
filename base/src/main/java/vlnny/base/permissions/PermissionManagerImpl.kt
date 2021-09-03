package vlnny.base.permissions

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import vlnny.base.permissions.PermissionsManager.Companion.PERMISSION_REQUEST_CODE_LOCATION
import javax.inject.Inject

class PermissionManagerImpl @Inject constructor() : PermissionsManager {

    override fun checkLocationPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION

            ), PERMISSION_REQUEST_CODE_LOCATION
        )
    }

    override fun isLocationPermissionsGranted(activity: Activity): Boolean {
        return !(getCoarseLocationStatus(activity) ||
                getFineLocationStatus(activity))
    }

    private fun getCoarseLocationStatus(activity: Activity) = ContextCompat.checkSelfPermission(
        activity,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED

    private fun getFineLocationStatus(activity: Activity) = ContextCompat.checkSelfPermission(
        activity,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
}