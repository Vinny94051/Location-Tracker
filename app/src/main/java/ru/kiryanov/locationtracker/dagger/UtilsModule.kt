package ru.kiryanov.locationtracker.dagger

import dagger.Binds
import dagger.Module
import ru.kiryanov.locationtracker.utils.location.LocationTracker
import ru.kiryanov.locationtracker.utils.location.LocationTrackerImpl
import vlnny.base.permissions.PermissionsManager
import vlnny.base.permissions.PermissionManagerImpl

@Module
interface UtilsModule {

    @Binds
    fun bindLocationTracker(locationTracker: LocationTrackerImpl): LocationTracker

    @Binds
    fun bindPermissionManager(permissionManagerImpl: PermissionManagerImpl): PermissionsManager
}