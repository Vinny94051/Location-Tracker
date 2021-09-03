package ru.kiryanov.locationtracker

import android.app.Application
import android.content.Context
import ru.kiryanov.locationtracker.dagger.AppComponent
import ru.kiryanov.locationtracker.dagger.DaggerAppComponent

class LocationTrackerApp : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .context(context = this)
            .build()
    }
}