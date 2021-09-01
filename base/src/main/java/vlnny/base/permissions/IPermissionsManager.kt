package vlnny.base.permissions

/**
 * Through this interface it is possible to check permissions
 *
 * Every method of this interface returns true if this permission is available
 */
interface IPermissionsManager {

    companion object {
        const val PERMISSION_REQUEST_CODE_LOCATION= 1111
    }

    /**
     * Returns true if Manifest.permission.WRITE_EXTERNAL_STORAGE is enabled
     */
    fun checkLocationPermissions(): Boolean

}