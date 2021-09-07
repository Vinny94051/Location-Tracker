package ru.kiryanov.locationtracker.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kiryanov.locationtracker.presentation.LocationTrackerActivity
import ru.kiryanov.locationtracker.utils.location.LocationService
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(locationTrackerActivity: LocationTrackerActivity)
    fun inject(locationService: LocationService)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context) : Builder

        fun build(): AppComponent
    }

}