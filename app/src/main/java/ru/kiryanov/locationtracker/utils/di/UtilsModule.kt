package ru.kiryanov.locationtracker.utils.di

import dagger.Binds
import dagger.Module
import ru.kiryanov.locationtracker.domain.location.LocationTracker
import ru.kiryanov.locationtracker.utils.location.LocationTrackerImpl
import ru.kiryanov.locationtracker.utils.preference.SharedPreferenceManagerImpl
import ru.kiryanov.locationtracker.utils.preference.SharedPreferencesManager
import vlnny.base.permissions.PermissionManagerImpl
import vlnny.base.permissions.PermissionsManager

@Module(includes = [UtilsProvidingModule::class])
interface UtilsModule {

    @Binds
    fun bindLocationTracker(locationTracker: LocationTrackerImpl): LocationTracker

    @Binds
    fun bindPermissionManager(permissionManagerImpl: PermissionManagerImpl): PermissionsManager

    @Binds
    fun bindSharedPreferenceManager(sharedPreferenceManagerImpl: SharedPreferenceManagerImpl): SharedPreferencesManager
}
