package ru.kiryanov.locationtracker.utils.di

import android.app.NotificationManager
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class MainCoroutineScope

@Module
class UtilsProvidingModule {

    @Provides
    fun provideFusedLocationProvider(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun provideNotificationManger(context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @MainCoroutineScope
    @Provides
    fun provideMainScope(): CoroutineScope = MainScope()
}