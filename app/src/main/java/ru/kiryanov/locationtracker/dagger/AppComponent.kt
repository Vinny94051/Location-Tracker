package ru.kiryanov.locationtracker.dagger

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kiryanov.locationtracker.presentation.LocationTrackerActivity

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(locationTrackerActivity: LocationTrackerActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context) : Builder

        fun build(): AppComponent
    }

}