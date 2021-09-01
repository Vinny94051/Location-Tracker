package vlnny.base.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import vlnny.base.permissions.IPermissionsManager.Companion.PERMISSION_REQUEST_CODE_LOCATION

class PermissionManager(private var activity: Activity) : IPermissionsManager {
    override fun checkLocationPermissions(): Boolean {
        return if (getCoarseLocationStatus() || getFineLocationStatus()) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), PERMISSION_REQUEST_CODE_LOCATION
            )
            false
        } else
            true
    }

    private fun getCoarseLocationStatus() = ContextCompat.checkSelfPermission(
        activity,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED

    private fun getFineLocationStatus() = ContextCompat.checkSelfPermission(
        activity,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
}