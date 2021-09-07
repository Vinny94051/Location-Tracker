package vlnny.base.permissions

import android.app.Activity

/**
 * Through this interface it is possible to check permissions
 */
interface PermissionsManager {

    companion object {
        const val PERMISSION_REQUEST_CODE_LOCATION = 1111
    }

    fun checkLocationPermissions(activity: Activity)

    /**
     * Returns true if location is enabled
     */
    fun isLocationPermissionsGranted(activity: Activity): Boolean

}