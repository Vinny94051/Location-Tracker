package ru.kiryanov.locationtracker

import android.app.Application
import ru.kiryanov.locationtracker.di.AppComponent
import ru.kiryanov.locationtracker.di.DaggerAppComponent

class LocationTrackerApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

   private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .context(context = this)
            .build()
    }
}